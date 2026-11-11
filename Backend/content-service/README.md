# MS2 – content-service

Catalogue pédagogique de SpeakUp : **parcours**, **banque de sujets d'oral** (FR/EN, B1 → C2, par type de tâche, avec questions de relance) et **recommandations d'exercices** ciblant les erreurs récurrentes de l'apprenant.

| | |
|---|---|
| Responsable | Étudiant 2 – Mehdi Sghaier (@mehdi-sg) |
| Port | 8082 |
| Base | H2 (`jdbc:h2:mem:contentdb`, console `/h2-console`) |
| Swagger | http://localhost:8082/swagger-ui.html |
| Route Gateway | `/api/parcours/**`, `/api/sujets/**`, `/api/recommandations/**` → `lb://content-service` |
| Défi technique | Moteur de recommandation à règles (erreurs récurrentes × catalogue) |

## 1. Responsabilités (contexte borné)
- **Fait** : gérer le contenu rédigé par les formateurs (parcours, sujets, relances), tirer un sujet au sort, recommander des sujets.
- **Ne fait pas** : analyser l'audio (analysis-service), noter un examen (exam-service), gérer les comptes (profile-service / Keycloak).

## 2. Base de données – pourquoi H2
Contenu de faible volume, rédigé par les formateurs, relations simples (Parcours N-N SujetOral). H2 est embarqué : le service démarre seul, sans serveur de base, et un **script de seed versionné** (`data.sql`) recharge les mêmes sujets de démonstration à chaque démarrage. Limite assumée : en mémoire, les données sont perdues à l'arrêt – acceptable puisque le seed les recrée ; passage en mode fichier (`jdbc:h2:file:`) possible sans changer le code.

## 3. Entités
Diagramme : `Documentation/Diagrams/content-service-classes.png`.

| Entité | Attributs | Relations |
|---|---|---|
| **Parcours** | id : Long, titre, description, langue : Langue, niveau : NiveauCECRL, auteurId : String, dateCreation | contient 1..* SujetOral, ordonnés (table de jointure `parcours_sujet`) ; un sujet peut appartenir à 0..* parcours |
| **SujetOral** | id : Long, intitule, theme, langue, niveau, typeTache : TypeTache, documentDeclencheur, dureePreparationSec : int, dureeParoleSec : int, relances : List\<String\>, competencesCiblees : Set\<CategorieErreur\>, auteurId | appartient à 0..* Parcours ; proposé par 0..* Recommandation |
| **Recommandation** | id : Long, apprenantId : String, categorieCiblee : CategorieErreur, motif, priorite : int, dateGeneration | référence 1 SujetOral |

Énumérations : `Langue {FR, EN}` · `NiveauCECRL {B1, B2, C1, C2}` · `TypeTache {ENTRETIEN, MONOLOGUE, INTERACTION, EXPOSE_DEBAT, DESCRIPTION_IMAGE}` · `CategorieErreur {PRONONCIATION, FLUIDITE, INTONATION, LEXIQUE, GRAMMAIRE, COHERENCE}`.

- `apprenantId` et `auteurId` = identifiant Keycloak : **référence par identifiant vers profile-service, jamais de clé étrangère** (autre service, autre base).
- `relances` et `competencesCiblees` sont des `@ElementCollection` (tables `sujet_relance`, `sujet_competence`).
- `competencesCiblees` est la **clé du moteur de recommandation** : elle relie une erreur détectée par l'IA aux sujets qui la travaillent.

## 4. Contrat d'API (via la Gateway)

| Méthode | URI | Description | Rôle | Codes |
|---|---|---|---|---|
| GET | `/api/parcours?langue=&niveau=` | Lister / filtrer les parcours | tous | 200, 400 |
| POST | `/api/parcours` | Créer un parcours | FORMATEUR | 201 (+ `Location`), 400, 401, 403 |
| GET | `/api/parcours/{id}` | Consulter un parcours et ses sujets | tous | 200, 404 |
| PUT | `/api/parcours/{id}` | Modifier un parcours (son auteur) | FORMATEUR | 200, 400, 403, 404 |
| GET | `/api/sujets?langue=&niveau=&type=` | Rechercher des sujets | tous | 200, 400 |
| GET | `/api/sujets/{id}` | Consulter un sujet (appelé par recording-service) | tous | 200, 404 |
| GET | `/api/sujets/aleatoire?langue=&niveau=&type=` | Tirer un sujet au sort, avec relances (appelé par exam-service) | tous | 200, 400, 404 |
| POST | `/api/sujets` | Créer un sujet | FORMATEUR | 201 (+ `Location`), 400, 401, 403 |
| GET | `/api/recommandations?apprenantId=` | Exercices recommandés | APPRENANT (lui-même) / FORMATEUR | 200 (+ `X-Degraded: true` en mode dégradé), 400, 403 |

Codes communs : 401 jeton absent/invalide, 403 rôle insuffisant (Keycloak, S8).
`/recommandations` ne renvoie pas 404 pour un apprenant inconnu : content-service ne possède pas les apprenants ; une liste vide (200) est la réponse correcte.

## 5. Interactions

| Type | Partenaire | Données échangées | Justification |
|---|---|---|---|
| Appelle (sync, OpenFeign) | analysis-service – `GET /api/analyses/apprenants/{id}/erreurs-recurrentes` | catégories d'erreurs + fréquence | Démarrage à froid : aucune recommandation stockée → besoin de la réponse tout de suite |
| Consomme (async) | `analysis.completed` | apprenantId, langue, niveauEstime, scores par critère | Recalculer et **stocker** les recommandations après chaque analyse, sans que l'apprenant attende |
| Consomme (async) | `user.deleted` | userId | Droit à l'effacement : supprimer ses recommandations ; anonymiser `auteurId` des contenus d'un formateur supprimé (le contenu reste utile aux autres) |
| Appelé par (sync) | profile-service | `GET /api/parcours/{id}` | Vérifier un parcours avant de l'affecter à un groupe |
| Appelé par (sync) | recording-service | `GET /api/sujets/{id}` | Vérifier le sujet d'un enregistrement |
| Appelé par (sync) | exam-service | `GET /api/sujets/aleatoire` | Tirer le sujet d'une épreuve blanche |

**Moteur de recommandation (règles)** : pour chaque catégorie d'erreur dont le score est sous le seuil du niveau, sélectionner les sujets de même langue et niveau dont `competencesCiblees` contient cette catégorie, exclure ceux déjà proposés récemment, trier par gravité de l'erreur (`priorite`), garder les 5 premiers.

**Idempotence** : traiter deux fois le même `analysis.completed` remplace les recommandations de l'apprenant au lieu de les dupliquer.

## 6. Comportement en cas de panne
- **analysis-service indisponible** : Feign + Resilience4j (timeout 2 s, retry ×2, circuit breaker). Fallback = mode dégradé : sujets du niveau cible tirés au hasard, réponse 200 avec l'en-tête `X-Degraded: true`. L'apprenant n'est jamais bloqué.
- **RabbitMQ indisponible** : les événements restent dans les files durables et sont consommés au redémarrage ; les recommandations déjà stockées restent servies.
- **Message en échec** (payload invalide, erreur répétée) : 3 tentatives puis **Dead Letter Queue** pour analyse.
- **content-service lui-même en panne** : ses appelants appliquent leur repli (exam-service : cache ; recording-service : retry ×3 puis 503 ; profile-service : groupe créé sans parcours).

## 7. Lancement
```bash
./mvnw spring-boot:run        # Java 17 – démarre même sans Eureka ni Config Server
```
