package tn.esprit.speakup.certificationservice.entities;

import jakarta.persistence.*;
import lombok.*;
import tn.esprit.speakup.certificationservice.enums.Langue;
import tn.esprit.speakup.certificationservice.enums.NiveauCECRL;

import java.time.LocalDateTime;

/**
 * Représente le niveau estimé d'un apprenant dans une langue donnée.
 * Mis à jour de façon asynchrone via l'événement exam.graded (RabbitMQ).
 */
@Entity
@Table(name = "niveau_estime")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NiveauEstime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** keycloakId de l'apprenant (UUID fourni par Keycloak) */
    @Column(nullable = false)
    private String apprenantId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Langue langue;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NiveauCECRL niveau;

    private LocalDateTime dateEstimation;

    /** ID de la session d'examen source (référence exam-service) */
    private Long sourceSessionId;
}
