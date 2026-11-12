-- =====================================================================
-- Catalogue de demonstration de content-service (MS2)
-- Sujets originaux rediges par l'equipe (aucun sujet officiel reproduit).
-- Recharge a chaque demarrage (H2 en memoire).
-- =====================================================================

-- ---------- Sujets d'oral : FR ----------
INSERT INTO sujet_oral (id, intitule, theme, langue, niveau, type_tache, document_declencheur, duree_preparation_sec, duree_parole_sec, auteur_id) VALUES
(1, 'Présentez-vous et parlez de vos études ou de votre travail.', 'Vie quotidienne', 'FR', 'B1', 'ENTRETIEN', NULL, 0, 120, 'seed-formateur'),
(2, 'Vous voulez organiser un week-end avec un ami qui préfère rester en ville. Convainquez-le de partir à la campagne.', 'Loisirs', 'FR', 'B1', 'INTERACTION', 'Jeu de rôle : l''examinateur joue l''ami.', 120, 180, 'seed-formateur'),
(3, 'Le télétravail améliore-t-il la qualité de vie ? Donnez votre point de vue.', 'Travail', 'FR', 'B2', 'MONOLOGUE', 'Court texte : de plus en plus d''entreprises proposent deux jours de télétravail par semaine.', 600, 300, 'seed-formateur'),
(4, 'Décrivez la photo puis expliquez ce qu''elle vous inspire sur la place du sport dans la société.', 'Société', 'FR', 'B2', 'DESCRIPTION_IMAGE', 'Photo : un marathon urbain avec des coureurs de tous âges.', 300, 240, 'seed-formateur'),
(5, 'Faut-il limiter l''usage des réseaux sociaux chez les adolescents ? Exposé puis débat.', 'Numérique', 'FR', 'C1', 'EXPOSE_DEBAT', 'Dossier : deux articles aux positions opposées sur la régulation des réseaux sociaux.', 1800, 600, 'seed-formateur'),
(6, 'La croissance économique est-elle compatible avec la protection de l''environnement ?', 'Environnement', 'FR', 'C2', 'EXPOSE_DEBAT', 'Dossier : extraits d''un rapport sur la transition énergétique.', 1800, 720, 'seed-formateur');

-- ---------- Sujets d'oral : EN ----------
INSERT INTO sujet_oral (id, intitule, theme, langue, niveau, type_tache, document_declencheur, duree_preparation_sec, duree_parole_sec, auteur_id) VALUES
(7, 'Talk about a place you like to visit and explain why.', 'Travel', 'EN', 'B1', 'ENTRETIEN', NULL, 0, 120, 'seed-formateur'),
(8, 'You and a classmate must choose a gift for your teacher. Discuss the options and agree on one.', 'Daily life', 'EN', 'B1', 'INTERACTION', 'Options: a book, a plant, a concert ticket, a group photo album.', 60, 180, 'seed-formateur'),
(9, 'Compare the two photos and say which way of learning is more effective.', 'Education', 'EN', 'B2', 'DESCRIPTION_IMAGE', 'Photo 1: students in a lecture hall. Photo 2: students working on a project together.', 60, 120, 'seed-formateur'),
(10, 'Should public transport be free in big cities? Give your opinion with examples.', 'Society', 'EN', 'B2', 'MONOLOGUE', NULL, 300, 240, 'seed-formateur'),
(11, 'Artificial intelligence will create more jobs than it destroys. Discuss.', 'Technology', 'EN', 'C1', 'EXPOSE_DEBAT', NULL, 600, 480, 'seed-formateur'),
(12, 'To what extent should governments regulate what people eat?', 'Health', 'EN', 'C2', 'EXPOSE_DEBAT', NULL, 600, 600, 'seed-formateur');

-- ---------- Questions de relance (ordonnees) ----------
INSERT INTO sujet_relance (sujet_id, ordre, relance) VALUES
(1, 0, 'Pourquoi avez-vous choisi ce domaine ?'),
(1, 1, 'Que faites-vous pendant votre temps libre ?'),
(2, 0, 'Et s''il pleut ce week-end ?'),
(2, 1, 'Combien cela va-t-il coûter ?'),
(3, 0, 'Quels sont les risques pour la vie d''équipe ?'),
(3, 1, 'Le télétravail convient-il à tous les métiers ?'),
(4, 0, 'Pratiquez-vous un sport ? Lequel ?'),
(4, 1, 'Le sport doit-il être obligatoire à l''école ?'),
(5, 0, 'Qui devrait fixer ces limites : l''État, les familles ou les plateformes ?'),
(5, 1, 'Quels effets positifs des réseaux sociaux pouvez-vous citer ?'),
(6, 0, 'Pouvez-vous donner un exemple concret de croissance verte ?'),
(6, 1, 'Quel rôle doivent jouer les citoyens ?'),
(7, 0, 'Who do you usually go there with?'),
(7, 1, 'Would you recommend it to a tourist?'),
(8, 0, 'Which option is the most personal?'),
(8, 1, 'What if your budget were smaller?'),
(9, 0, 'Which way do you prefer to learn?'),
(9, 1, 'How will technology change classrooms?'),
(10, 0, 'Who should pay for it?'),
(10, 1, 'Would it really reduce traffic?'),
(11, 0, 'Which jobs are most at risk?'),
(11, 1, 'How should schools prepare young people?'),
(12, 0, 'Is a sugar tax fair?'),
(12, 1, 'Where is the line between protection and freedom?');

-- ---------- Competences ciblees (cle du moteur de recommandation) ----------
INSERT INTO sujet_competence (sujet_id, categorie) VALUES
(1, 'FLUIDITE'), (1, 'PRONONCIATION'),
(2, 'INTONATION'), (2, 'FLUIDITE'),
(3, 'COHERENCE'), (3, 'GRAMMAIRE'),
(4, 'LEXIQUE'), (4, 'COHERENCE'),
(5, 'COHERENCE'), (5, 'LEXIQUE'),
(6, 'LEXIQUE'), (6, 'GRAMMAIRE'),
(7, 'PRONONCIATION'), (7, 'FLUIDITE'),
(8, 'INTONATION'), (8, 'GRAMMAIRE'),
(9, 'LEXIQUE'), (9, 'FLUIDITE'),
(10, 'COHERENCE'), (10, 'GRAMMAIRE'),
(11, 'COHERENCE'), (11, 'LEXIQUE'),
(12, 'LEXIQUE'), (12, 'INTONATION');

-- ---------- Parcours ----------
INSERT INTO parcours (id, titre, description, langue, niveau, auteur_id, date_creation) VALUES
(1, 'Préparer le DELF B2 – oral', 'De l''entretien au monologue argumenté, en passant par la description d''image.', 'FR', 'B2', 'seed-formateur', CURRENT_TIMESTAMP),
(2, 'Speaking for B1 learners', 'Short interviews and role plays to build fluency and confidence.', 'EN', 'B1', 'seed-formateur', CURRENT_TIMESTAMP);

INSERT INTO parcours_sujet (parcours_id, sujet_id, ordre) VALUES
(1, 1, 0), (1, 4, 1), (1, 3, 2),
(2, 7, 0), (2, 8, 1);

-- Les id ont ete fixes a la main : on decale les compteurs pour que les prochains
-- enregistrements crees par l'API ne reutilisent pas un id deja pris.
-- ---------- Erreurs recurrentes d'un apprenant de demonstration (copie locale) ----------
INSERT INTO profil_erreurs (id, apprenant_id, langue, type_erreur, occurrences, derniere_mise_ajour) VALUES
(1, 'apprenant-demo', 'FR', 'GRAMMAIRE', 7, CURRENT_TIMESTAMP),
(2, 'apprenant-demo', 'FR', 'LEXIQUE', 4, CURRENT_TIMESTAMP),
(3, 'apprenant-demo', 'EN', 'PRONONCIATION', 5, CURRENT_TIMESTAMP);

ALTER TABLE sujet_oral ALTER COLUMN id RESTART WITH 100;
ALTER TABLE parcours ALTER COLUMN id RESTART WITH 100;
ALTER TABLE profil_erreurs ALTER COLUMN id RESTART WITH 100;
