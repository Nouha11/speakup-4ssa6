package tn.esprit.speakup.certificationservice.dto;

import tn.esprit.speakup.certificationservice.enums.Langue;
import tn.esprit.speakup.certificationservice.enums.NiveauCECRL;

import java.time.LocalDateTime;

/**
 * Réponse pour la consultation du niveau estimé d'un apprenant.
 */
public record NiveauEstimeResponse(
        Long id,
        String apprenantId,
        Langue langue,
        NiveauCECRL niveau,
        LocalDateTime dateEstimation,
        Long sourceSessionId
) {}
