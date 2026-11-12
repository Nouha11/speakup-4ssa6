package tn.esprit.spring.contentservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.esprit.spring.contentservice.entity.enums.Langue;
import tn.esprit.spring.contentservice.entity.enums.NiveauCECRL;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Parcours pedagogique cree par un formateur : une suite ordonnee de sujets d'oral.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Parcours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String titre;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 2)
    private Langue langue;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 2)
    private NiveauCECRL niveau;

    /** keycloakId du formateur auteur (reference vers profile-service, pas de cle etrangere). */
    @Column(name = "auteur_id", length = 64)
    private String auteurId;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    /** Parcours N-N SujetOral, ordonne : la colonne "ordre" garde la position de chaque sujet. */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "parcours_sujet",
            joinColumns = @JoinColumn(name = "parcours_id"),
            inverseJoinColumns = @JoinColumn(name = "sujet_id"))
    @OrderColumn(name = "ordre")
    private List<SujetOral> sujets = new ArrayList<>();

    @PrePersist
    void onCreate() {
        this.dateCreation = LocalDateTime.now();
    }
}
