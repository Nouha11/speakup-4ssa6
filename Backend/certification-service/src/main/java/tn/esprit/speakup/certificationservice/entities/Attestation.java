package tn.esprit.speakup.certificationservice.entities;

import jakarta.persistence.*;
import lombok.*;
import tn.esprit.speakup.certificationservice.enums.Langue;
import tn.esprit.speakup.certificationservice.enums.NiveauCECRL;
import tn.esprit.speakup.certificationservice.enums.StatutAttestation;

import java.time.LocalDateTime;

/**
 * Attestation de niveau délivrée à un apprenant.
 * Générée en PDF + QR code après vérification d'éligibilité.
 */
@Entity
@Table(name = "attestation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attestation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Numéro unique imprimé sur l'attestation (ex: CERT-2026-00042) */
    @Column(nullable = false, unique = true)
    private String numero;

    /** keycloakId de l'apprenant */
    @Column(nullable = false)
    private String apprenantId;

    /** Copie figée du nom (profile-service peut ne plus répondre après) */
    private String nomApprenant;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Langue langue;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NiveauCECRL niveauAtteint;

    private LocalDateTime dateEmission;

    /** Code unique embarqué dans le QR code pour vérification publique */
    @Column(nullable = false, unique = true)
    private String codeVerification;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutAttestation statut;
}
