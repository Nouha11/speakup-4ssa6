package tn.esprit.spring.progressservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.progressservice.entity.IndicateurCritere;
import tn.esprit.spring.progressservice.entity.ProgressionApprenant;
import tn.esprit.spring.progressservice.enums.Critere;
import tn.esprit.spring.progressservice.enums.Langue;
import tn.esprit.spring.progressservice.service.IProgressionService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/progression")
@RequiredArgsConstructor
@Tag(name = "Progression", description = "Synthèse et courbes de progression de l'apprenant")
public class ProgressionController {

    private final IProgressionService progressionService;

    /**
     * GET /api/progression/apprenants/{id}?langue=
     * Synthèse de progression d'un apprenant.
     */
    @GetMapping("/apprenants/{id}")
    @Operation(summary = "Synthèse de progression", description = "Retourne la progression globale d'un apprenant, filtrée par langue si précisée.")
    public ResponseEntity<ProgressionApprenant> getProgression(
            @PathVariable String id,
            @RequestParam(required = false) Langue langue) {
        return ResponseEntity.ok(progressionService.getProgression(id, langue));
    }

    /**
     * GET /api/progression/apprenants/{id}/courbes?critere=&depuis=
     * Série temporelle d'un critère.
     */
    @GetMapping("/apprenants/{id}/courbes")
    @Operation(summary = "Courbes par critère", description = "Retourne la série temporelle des indicateurs pour un critère donné, depuis une date facultative.")
    public ResponseEntity<List<IndicateurCritere>> getCourbes(
            @PathVariable String id,
            @RequestParam Critere critere,
            @RequestParam(required = false) LocalDateTime depuis) {
        return ResponseEntity.ok(progressionService.getCourbes(id, critere, depuis));
    }

    /**
     * GET /api/progression/groupes/{groupeId}/synthese
     * Vue formateur : progression de tous les apprenants d'un groupe.
     */
    @GetMapping("/groupes/{groupeId}/synthese")
    @Operation(summary = "Synthèse de groupe", description = "Vue formateur : liste les progressions des apprenants d'un groupe.")
    public ResponseEntity<List<ProgressionApprenant>> getSyntheseGroupe(
            @PathVariable Long groupeId) {
        return ResponseEntity.ok(progressionService.getSyntheseGroupe(groupeId));
    }

    /**
     * DELETE /api/progression/apprenants/{id}
     * Supprimer toutes les données de progression d'un apprenant (ADMIN).
     */
    @DeleteMapping("/apprenants/{id}")
    @Operation(summary = "Supprimer la progression", description = "Supprime toutes les données de progression d'un apprenant (ADMIN).")
    public ResponseEntity<Void> deleteProgression(@PathVariable String id) {
        progressionService.deleteProgression(id);
        return ResponseEntity.noContent().build();
    }
}
