package tn.esprit.speakup.certificationservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.speakup.certificationservice.dto.AttestationRequest;
import tn.esprit.speakup.certificationservice.dto.AttestationResponse;
import tn.esprit.speakup.certificationservice.services.ICertificationService;

import java.util.List;

/**
 * Endpoints REST pour la gestion des attestations (§7.5 – MS5).
 *
 * POST   /api/attestations                        → émettre une attestation
 * GET    /api/attestations/apprenant/{id}         → liste des attestations d'un apprenant
 * GET    /api/attestations/verifier/{code}        → vérifier via code QR (public)
 * PATCH  /api/attestations/{id}/revoquer          → révoquer une attestation
 */
@RestController
@RequestMapping("/api/attestations")
@RequiredArgsConstructor
@Tag(name = "Attestations", description = "Émission, consultation et révocation des attestations de niveau")
public class AttestationController {

    private final ICertificationService certificationService;

    @PostMapping
    @Operation(summary = "Émettre une attestation de niveau")
    public ResponseEntity<AttestationResponse> emettreAttestation(
            @Valid @RequestBody AttestationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(certificationService.emettreAttestation(request));
    }

    @GetMapping("/apprenant/{apprenantId}")
    @Operation(summary = "Lister les attestations d'un apprenant")
    public ResponseEntity<List<AttestationResponse>> getByApprenant(
            @PathVariable String apprenantId) {
        return ResponseEntity.ok(certificationService.getAttestationsByApprenant(apprenantId));
    }

    @GetMapping("/verifier/{codeVerification}")
    @Operation(summary = "Vérifier une attestation via son code QR (accès public)")
    public ResponseEntity<AttestationResponse> verifier(
            @PathVariable String codeVerification) {
        return ResponseEntity.ok(certificationService.verifierAttestation(codeVerification));
    }

    @PatchMapping("/{id}/revoquer")
    @Operation(summary = "Révoquer une attestation")
    public ResponseEntity<AttestationResponse> revoquer(@PathVariable Long id) {
        return ResponseEntity.ok(certificationService.revoquerAttestation(id));
    }
}
