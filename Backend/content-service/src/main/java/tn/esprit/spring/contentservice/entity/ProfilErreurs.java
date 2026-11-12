package tn.esprit.spring.contentservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.esprit.spring.contentservice.entity.enums.Langue;
import tn.esprit.spring.contentservice.entity.enums.TypeErreur;

import java.time.LocalDateTime;

/**
 * Copie locale des erreurs recurrentes d'un apprenant, alimentee par l'evenement analysis.completed (S7).
 * apprenantId = keycloakId : reference par identifiant, pas de cle etrangere.
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"apprenant_id", "langue", "type_erreur"}))
@Getter
@Setter
@NoArgsConstructor
public class ProfilErreurs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "apprenant_id", nullable = false, length = 64)
    private String apprenantId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 2)
    private Langue langue;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_erreur", nullable = false, length = 20)
    private TypeErreur typeErreur;

    private int occurrences;

    @Column(name = "derniere_mise_ajour")
    private LocalDateTime derniereMiseAJour;
}
