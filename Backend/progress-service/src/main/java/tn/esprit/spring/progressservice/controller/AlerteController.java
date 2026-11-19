package tn.esprit.spring.progressservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.progressservice.entity.AlerteFormateur;
import tn.esprit.spring.progressservice.service.IProgressionService;

import java.util.List;

@RestController
@RequestMapping("/api/alertes")
@RequiredArgsConstructor
@Tag(name = "Alertes", description = "Alertes de difficulté destinées au formateur")
public class AlerteController {

    private final IProgressionService progressionService;

    /**
     * GET /api/alertes?groupeId=&traitee=
     * Lister les alertes, avec filtres optionnels.
     */
    @GetMapping
    @Operation(summary = "Lister les alertes", description = "Retourne les alertes filtrées par groupe et/ou statut traité.")
    public ResponseEntity<List<AlerteFormateur>> getAlertes(
            @RequestParam(required = false) Long groupeId,
            @RequestParam(required = false) Boolean traitee) {
        return ResponseEntity.ok(progressionService.getAlertes(groupeId, traitee));
    }

    /**
     * PUT /api/alertes/{id}/traitee
     * Marquer une alerte comme traitée.
     */
    @PutMapping("/{id}/traitee")
    @Operation(summary = "Marquer alerte traitée", description = "Passe le champ 'traitee' d'une alerte à true.")
    public ResponseEntity<AlerteFormateur> marquerTraitee(@PathVariable Long id) {
        return ResponseEntity.ok(progressionService.marquerAlertTraitee(id));
    }
}
