package tn.esprit.speakup.certificationservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import tn.esprit.speakup.certificationservice.enums.Langue;

/**
 * Corps de la requête POST /api/attestations
 */
public record AttestationRequest(

        @NotBlank(message = "L'identifiant apprenant est obligatoire")
        String apprenantId,

        @NotNull(message = "La langue est obligatoire")
        Langue langue
) {}
