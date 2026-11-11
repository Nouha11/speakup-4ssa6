package tn.esprit.speakup.certificationservice.dto;

import tn.esprit.speakup.certificationservice.enums.Langue;
import tn.esprit.speakup.certificationservice.enums.NiveauCECRL;
import tn.esprit.speakup.certificationservice.enums.StatutAttestation;

import java.time.LocalDateTime;

/**
 * Réponse renvoyée après création ou consultation d'une attestation.
 */
public record AttestationResponse(
        Long id,
        String numero,
        String apprenantId,
        String nomApprenant,
        Langue langue,
        NiveauCECRL niveauAtteint,
        LocalDateTime dateEmission,
        String codeVerification,
        StatutAttestation statut
) {}
