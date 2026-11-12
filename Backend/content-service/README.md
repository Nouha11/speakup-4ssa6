# MS2 – content-service

Catalogue pédagogique de SpeakUp : **parcours**, **banque de sujets d'oral** (FR/EN, B1 → C2, par type de tâche, avec questions de relance) et **recommandations d'exercices** ciblant les erreurs récurrentes de l'apprenant.

| | |
|---|---|
| Responsable | Étudiant 2 – Mehdi Sghaier (@mehdi-sg) |
| Port | 8082 |
| Base | H2 (`jdbc:h2:mem:contentdb`, console `/h2-console`) |
| Swagger | http://localhost:8082/swagger-ui.html (springdoc ajouté en S5 avec les contrôleurs) |
| Route Gateway | `/api/parcours/**`, `/api/sujets/**`, `/api/recommandations/**` → `lb://content-service` |
| Défi technique | Moteur de recommandation à règles (erreurs récurrentes × catalogue) |

## 1. Responsabilités (contexte borné)
- **Fait** : gérer le contenu rédigé par les formateurs (parcours, sujets, relances), tirer un sujet au sort, recommander des sujets.
- **Ne fait pas** : analyser l'audio (analysis-service), noter un examen (exam-service), gérer les comptes (profile-service / Keycloak).

## 2. Base de données – pourquoi H2
Contenu de faible volume, rédigé par les formateurs, relations simples (Parcours N-N SujetOral). H2 est embarqué : le service démarre seul, sans serveur de base, et un **script de seed versionné** (`data.sql`) recharge les mêmes sujets de démonstration à chaque démarrage. Limite assumée : en mémoire, les données sont perdues à l'arrêt – acceptable puisque le seed les recrée ; passage en mode fichier (`jdbc:h2:file:`) possible sans changer le code.

## 3. Entités
Diagramme : `Documentation/Diagrams/content-service/content-service-classes-complet.png`.

| Entité | Attributs | Relations |
|---|---|---|
| **Parcours** | id : Long, titre, description, langue : Langue, niveau : NiveauCECRL, auteurId : String, dateCreation | contient 1..* SujetOral, ordonnés (table de jointure `parcours_sujet`) ; un sujet peut appartenir à 0..* parcours |
| **SujetOral** | id : Long, intitule, theme, langue, niveau, typeTache : TypeTache, documentDeclencheur, dureePreparationSec : int, dureeParoleSec : int, relances : List\<String\>, competencesCiblees : Set\<TypeErreur\>, auteurId | appartient à 0..* Parcours |
| **ProfilErreurs** | id : Long, apprenantId : String, langue, typeErreur : TypeErreur, occurrences : int, derniereMiseAJour | copie locale des erreurs de l'apprenant, alimentée par `analysis.completed` ; unique (apprenantId, langue, typeErreur) |
| *EvenementTraite* (table technique) | eventId : String (PK), type, dateTraitement | mémorise les événements déjà traités (idempotence) |

Énumérations : `Langue {FR, EN}` · `NiveauCECRL {B1, B2, C1, C2}` · `TypeTache {ENTRETIEN, MONOLOGUE, INTERACTION, EXPOSE_DEBAT, DESCRIPTION_IMAGE}` · `TypeErreur {PRONONCIATION, FLUIDITE, INTONATION, LEXIQUE, GRAMMAIRE, COHERENCE}`.

- `apprenantId` et `auteurId` = identifiant Keycloak : **référence par identifiant, jamais de clé étrangère** vers un autre service.
- `relances` et `competencesCiblees` sont des `@ElementCollection` (tables `sujet_relance`, `sujet_competence`).
- Les **recommandations ne sont pas stockées** : elles sont calculées à la demande en croisant `ProfilErreurs.typeErreur` avec `SujetOral.competencesCiblees`.

## 4. Contrat d'API (via la Gateway)

| Méthode | URI | Description | Rôle | Codes |
|---|---|---|---|---|
| GET | `/api/parcours?langue=&niveau=` | Lister / filtrer les parcours | tous | 200, 400, 401 |
| POST | `/api/parcours` | Créer un parcours | FORMATEUR | 201 (+ `Location`), 400, 401, 403 |
| GET | `/api/parcours/{id}` | Consulter un parcours et ses sujets (appelé par profile-service) | tous | 200, 401, 404 |
| PUT | `/api/parcours/{id}` | Modifier un parcours (son auteur) | FORMATEUR | 200, 400, 401, 403, 404 |
| GET | `/api/sujets?langue=&niveau=&type=` | Rechercher des sujets | tous | 200, 400, 401 |
| GET | `/api/sujets/{id}` | Consulter un sujet (appelé par recording-service) | tous | 200, 401, 404 |
| GET | `/api/sujets/aleatoire?langue=&niveau=&type=` | Tirer un sujet au sort, avec relances (appelé par exam-service) | tous | 200, 400, 401, 404 |
| POST | `/api/sujets` | Créer un sujet | FORMATEUR | 201 (+ `Location`), 400, 401, 403 |
| GET | `/api/recommandations?apprenantId=&langue=` | Sujets recommandés selon les erreurs récurrentes | APPRENANT (lui-même) / FORMATEUR | 200, 400, 401, 403 |

401 = jeton absent ou invalide, 403 = rôle insuffisant (Keycloak, S8).
`/recommandations` ne renvoie pas 404 pour un apprenant inconnu : content-service ne possède pas les apprenants. Sans erreur connue, il renvoie les sujets du niveau (200).

## 5. Interactions — service amont

content-service **n'appelle aucun autre service** : le graphe des appels synchrones reste sans cycle.

| Type | Partenaire | Données échangées | Justification |
|---|---|---|---|
| Appelé par (sync, OpenFeign) | profile-service | `GET /api/parcours/{id}` | Vérifier un parcours avant de l'affecter à un groupe |
| Appelé par (sync, OpenFeign) | recording-service | `GET /api/sujets/{id}` | Vérifier le sujet d'un enregistrement |
| Appelé par (sync, OpenFeign) | exam-service | `GET /api/sujets/aleatoire` | Tirer le sujet d'une épreuve blanche |
| Consomme (async, RabbitMQ) | `analysis.completed` (analysis-service) | eventId, apprenantId, langue, niveauEstime, erreursParType | Mettre à jour la copie locale `ProfilErreurs` sans que l'apprenant attende |
| Consomme (async, RabbitMQ) | `user.deleted` (profile-service) | eventId, userId | Droit à l'effacement : supprimer son `ProfilErreurs` ; anonymiser `auteurId` des contenus d'un formateur supprimé |

**Moteur de recommandation (règles)** : pour l'apprenant et la langue demandés, prendre ses types d'erreur les plus fréquents dans `ProfilErreurs`, sélectionner les sujets de même langue et niveau dont `competencesCiblees` contient ce type, trier par nombre d'occurrences, garder les 5 premiers.

**Idempotence** : avant de traiter un événement, vérifier si son `eventId` est dans `EvenementTraite` ; si oui, l'ignorer, sinon le traiter et l'enregistrer dans la même transaction.

## 6. Comportement en cas de panne
- **Autre service indisponible** : aucun impact, content-service n'a aucun appel sortant.
- **RabbitMQ indisponible** : les recommandations restent calculées sur la dernière copie locale `ProfilErreurs` ; les événements, en files durables, sont consommés au redémarrage.
- **Message en échec** (payload invalide, erreur répétée) : 3 tentatives puis **Dead Letter Queue**.
- **Événement reçu deux fois** : `eventId` déjà présent dans `EvenementTraite` → ignoré.
- **content-service lui-même en panne** : ses appelants appliquent leur repli (exam-service : sujet en cache ; recording-service : retry ×3 puis 503 ; profile-service : groupe créé sans parcours).

## 7. Lancement
```bash
./gradlew bootRun        # Java 17 – démarre même sans Eureka ni Config Server
```
