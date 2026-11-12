package tn.esprit.spring.contentservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.esprit.spring.contentservice.entity.enums.TypeErreur;
import tn.esprit.spring.contentservice.entity.enums.Langue;
import tn.esprit.spring.contentservice.entity.enums.NiveauCECRL;
import tn.esprit.spring.contentservice.entity.enums.TypeTache;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Sujet d'oral de la banque : consigne, document declencheur, durees et questions de relance.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class SujetOral {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 300)
    private String intitule;

    @Column(length = 100)
    private String theme;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 2)
    private Langue langue;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 2)
    private NiveauCECRL niveau;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TypeTache typeTache;

    /** Texte ou description d'image a partir duquel l'apprenant parle. */
    @Column(length = 2000)
    private String documentDeclencheur;

    private int dureePreparationSec;

    private int dureeParoleSec;

    /** Questions de relance posees par l'examinateur, dans l'ordre (table sujet_relance). */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "sujet_relance", joinColumns = @JoinColumn(name = "sujet_id"))
    @OrderColumn(name = "ordre")
    @Column(name = "relance", length = 500)
    private List<String> relances = new ArrayList<>();

    /** Erreurs que ce sujet aide a travailler : cle du moteur de recommandation (table sujet_competence). */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "sujet_competence", joinColumns = @JoinColumn(name = "sujet_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "categorie", length = 20)
    private Set<TypeErreur> competencesCiblees = new HashSet<>();

    /** keycloakId du formateur auteur (reference vers profile-service). */
    @Column(name = "auteur_id", length = 64)
    private String auteurId;
}
