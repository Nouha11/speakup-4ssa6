# SpeakUp – Entraînement et évaluation de l'expression orale (FR / EN)

Mini-projet AWD (MT-41) – 2026-2027 – ESPRIT – Classe 4 SSA 6 – Enseignante : Rim Douss

## 1. Sujet
Application web distribuée qui fait parler l'apprenant, analyse sa prise de parole par IA (transcription, prononciation,
fluidité, intonation, lexique, grammaire, cohérence) et le prépare aux certifications CECRL (B1 → C2) en français et en anglais.
Le formateur suit ses groupes, réécoute les enregistrements et ajuste les notes.

L'identité (identifiant, mot de passe, email, rôles) est gérée par **Keycloak**. Les services ne stockent que les données métier
et référencent chaque personne par son `keycloakId` (claim `sub` du JWT).

## 2. Équipe
| Membre | GitHub | Microservice | Base | Lot commun |
|---|---|---|---|---|
| Oumeyma Radhouani | [@oumeyma-radhouani](https://github.com/oumeyma-radhouani) | MS1 `profile-service` | H2 | Keycloak + sécurité Gateway |
| Mehdi Sghaier | [@mehdi-sg](https://github.com/mehdi-sg) | MS2 `content-service` | H2 | Eureka, Config Server, docker-compose |
| Mohammed Tayssir Lamouri | [@tayssir20](https://github.com/tayssir20) | MS3 `recording-service` | MySQL | MS commun : pipeline audio (STT) |
| Sarra Jemai | [@jemaisarah](https://github.com/jemaisarah) | MS4 `exam-service` | MySQL | MS commun : scoring et feedback |
| Nourchene Khemiri | [@Nourchenekhemiri](https://github.com/Nourchenekhemiri) | MS5 `certification-service` | H2 | Frontend apprenant |
| Nouha Hamrouni | [@Nouha11](https://github.com/Nouha11) | MS6 `progress-service` | MySQL | Frontend formateur / admin |
| Groupe | | `analysis-service` (microservice commun) | MongoDB | |

## 3. Architecture
Diagramme global : `Documentation/Diagrams/` (à ajouter).

- **Infrastructure :** Angular, API Gateway (Spring Cloud Gateway), Eureka, Config Server, Keycloak, RabbitMQ, MinIO, Docker Compose.
- **Stack :** Java 17, Spring Boot 3, Spring Data JPA, springdoc-openapi, Maven. IA : Whisper, LanguageTool.
- **Synchrone (OpenFeign + Resilience4j) :** `profile`→`content`, `recording`→`content`, `exam`→`content`, `certification`→`profile`, `progress`→`profile`, `profile`→Keycloak.
- **Asynchrone (RabbitMQ) :** `audio.received`, `analysis.completed`, `exam.graded`, `level.estimated`, `user.deleted` (chacun avec un `eventId` pour l'idempotence).

## 4. Structure du dépôt
```
Backend/          un projet par microservice + api-gateway, eureka-server, config-server
Frontend/         application Angular
Documentation/    Presentation/ · Diagrams/ · Dossier_Technique_SpeakUp.docx
```

## 5. Lancement (validation 1)
Les projets sont créés ; la logique métier démarre à la séance 5. `docker-compose.yml` : séance 9.
```bash
cd Backend/<service> && ./mvnw spring-boot:run      # Java 17
```

## 6. Gestion de projet et Git
- Kanban : GitHub Projects – **lien à compléter**
- Branches `main` / `develop` / `feature/<service>-<fonctionnalité>`, Pull Request relue, Conventional Commits : voir `CONTRIBUTING.md`

## 7. Données et licences
Aucun apprenant réel n'est enregistré. Sources : Mozilla Common Voice (CC0), speechocean762 (CC BY 4.0),
Multilingual LibriSpeech (CC BY 4.0), voix synthétiques Piper, données générées (Faker, graine fixe) et enregistrements
consentis de l'équipe. Citer les auteurs des corpus CC BY ; ne pas reproduire les sujets officiels.
