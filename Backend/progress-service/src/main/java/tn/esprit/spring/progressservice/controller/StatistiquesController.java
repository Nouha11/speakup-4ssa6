package tn.esprit.spring.progressservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.progressservice.entity.IndicateurCritere;
import tn.esprit.spring.progressservice.enums.Critere;
import tn.esprit.spring.progressservice.service.IProgressionService;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Tag(name = "Statistiques & Indicateurs", description = "Indicateurs bruts et statistiques établissement")
public class StatistiquesController {

    private final IProgressionService progressionService;

    /**
     * GET /api/statistiques/etablissement
     * Indicateurs agrégés au niveau établissement.
     */
    @GetMapping("/api/statistiques/etablissement")
    @Operation(summary = "Statistiques établissement", description = "Retourne des indicateurs agrégés (nombre de progressions, alertes, etc.).")
    public ResponseEntity<Map<String, Object>> getStatistiquesEtablissement() {
        return ResponseEntity.ok(progressionService.getStatistiquesEtablissement());
    }

    /**
     * GET /api/indicateurs?apprenantId=&critere=
     * Indicateurs bruts d'un apprenant, filtrés par critère si précisé.
     */
    @GetMapping("/api/indicateurs")
    @Operation(summary = "Indicateurs bruts", description = "Retourne les indicateurs bruts d'un apprenant, filtrés par critère si précisé.")
    public ResponseEntity<List<IndicateurCritere>> getIndicateurs(
            @RequestParam String apprenantId,
            @RequestParam(required = false) Critere critere) {
        return ResponseEntity.ok(progressionService.getIndicateurs(apprenantId, critere));
    }
}
