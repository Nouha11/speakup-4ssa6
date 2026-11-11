package tn.esprit.speakup.certificationservice.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.*;
import tn.esprit.speakup.certificationservice.enums.NiveauCECRL;

/**
 * Règle métier définissant les conditions d'éligibilité pour chaque niveau CECRL.
 * Ex : pour obtenir B2, il faut noteMinimale=14.0 sur au moins 2 sessions.
 */
@Entity
@Table(name = "regle_validation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegleValidation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private NiveauCECRL niveau;

    @DecimalMin("0.0")
    @DecimalMax("20.0")
    @Column(nullable = false)
    private Double noteMinimale;

    @Min(1)
    @Column(nullable = false)
    private Integer nbSessionsMin;
}
