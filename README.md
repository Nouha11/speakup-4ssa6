# SpeakUp – Plateforme d'entraînement et d'évaluation de l'expression orale (FR / EN)

Mini-projet AWD – Année universitaire 2026-2027 – ESPRIT
Classe / groupe : 4 SSA 6 – Enseignante : Rim Douss

## 1. Sujet
Application web distribuée qui fait parler l'apprenant, analyse sa prise de parole par IA
(transcription, prononciation, fluidité, intonation, lexique, grammaire, cohérence) et le prépare aux
certifications alignées sur le CECRL (B1 → C2). Le formateur suit ses groupes et ajuste les notes.
L'identité (identifiant, mot de passe, email, rôles) est gérée par **Keycloak** ; les services stockent
uniquement les informations métier et référencent la personne par son `keycloakId`.

## 2. Équipe
| Membre | GitHub | Microservice | Base | Lot commun |
|---|---|---|---|---|
| Étudiant 1 | [@Oumeyma Radhouani](https://github.com/oumeyma-radhouani) | MS1 `profile-service` | H2 | Keycloak + sécurité Gateway |
| Étudiant 2 | [@Nourchene Khemiri](https://github.com/Nourchenekhemiri) | MS2 `content-service` | H2 | Eureka, Config Server, docker-compose |
| Étudiant 3 | [@](https://github.com/oumeyma-radhouani) | MS3 `recording-service` | MySQL | MS commun : pipeline audio (STT) |
| Étudiant 4 | [@](https://github.com/oumeyma-radhouani) | MS4 `exam-service` | MySQL | MS commun : scoring et feedback |
| Étudiant 5 | [@](https://github.com/oumeyma-radhouani) | MS5 `certification-service` | H2 | Frontend apprenant |
| Étudiant 6 | [@Nouha Hamrouni](https://github.com/Nouha11) | MS6 `progress-service` | MySQL | Frontend formateur / admin |
| Groupe | | `analysis-service` (microservice commun) | MongoDB | |

## 3. Architecture
Diagramme d'architecture globale : `Documentation/Diagrams/` (à ajouter).
Infrastructure : API Gateway (Spring Cloud Gateway), Eureka, Config Server, Keycloak, RabbitMQ, MinIO, Docker Compose.
Synchrone : OpenFeign (+ Resilience4j). Asynchrone : RabbitMQ (`audio.received`, `analysis.completed`, `exam.graded`, `user.deleted`).

## 4. Structure du dépôt
```
Backend/          un projet par microservice + api-gateway, eureka-server, config-server
Frontend/         application Angular
Documentation/    Presentation/ · Diagrams/ · templates/ · Dossier_Technique_SpeakUp.docx
scripts/          init-service.sh (génère le projet Spring Boot d'UN service)
.github/          CODEOWNERS, modèles de Pull Request et de ticket
```

## 5. Lancement (état à la validation 1)
Les projets des microservices sont créés ; la logique métier démarre à la séance 5.
```bash
cd Backend/<service> && ./mvnw spring-boot:run      # Java 17
```
Le `docker-compose.yml` global sera ajouté en séance 9.

## 6. Gestion de projet et Git
- Outil : GitHub Projects (Kanban) – **lien à compléter**
- Règles de branches, commits et Pull Requests : voir `CONTRIBUTING.md`

## 7. Données de démonstration et licences
Corpus publics (Mozilla Common Voice – CC0, speechocean762 – CC BY 4.0, Multilingual LibriSpeech – CC BY 4.0),
voix synthétiques, données générées (graine fixe) et enregistrements consentis de l'équipe.
Aucun apprenant réel n'est enregistré. Citer les auteurs des corpus CC BY ; ne pas reproduire les sujets officiels.
