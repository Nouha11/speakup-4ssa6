package tn.esprit.speakup.certificationservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.speakup.certificationservice.dto.NiveauEstimeResponse;
import tn.esprit.speakup.certificationservice.enums.Langue;
import tn.esprit.speakup.certificationservice.services.ICertificationService;

import java.util.List;

/**
 * Endpoints REST pour la consultation du niveau estimé (§7.5 – MS5).
 *
 * GET  /api/niveaux/{apprenantId}               → tous les niveaux d'un apprenant
 * GET  /api/niveaux/{apprenantId}/{langue}       → niveau pour une langue précise
 */
@RestController
@RequestMapping("/api/niveaux")
@RequiredArgsConstructor
@Tag(name = "Niveaux estimés", description = "Consultation du niveau CECRL estimé par apprenant")
public class NiveauEstimeController {

    private final ICertificationService certificationService;

    @GetMapping("/{apprenantId}")
    @Operation(summary = "Tous les niveaux estimés d'un apprenant (toutes langues)")
    public ResponseEntity<List<NiveauEstimeResponse>> getAll(@PathVariable String apprenantId) {
        return ResponseEntity.ok(certificationService.getAllNiveauxEstimes(apprenantId));
    }

    @GetMapping("/{apprenantId}/{langue}")
    @Operation(summary = "Niveau estimé d'un apprenant pour une langue donnée")
    public ResponseEntity<NiveauEstimeResponse> getByLangue(
            @PathVariable String apprenantId,
            @PathVariable Langue langue) {
        return ResponseEntity.ok(certificationService.getNiveauEstime(apprenantId, langue));
    }
}
