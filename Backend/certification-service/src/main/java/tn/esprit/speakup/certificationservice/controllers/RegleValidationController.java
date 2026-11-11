package tn.esprit.speakup.certificationservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.speakup.certificationservice.entities.RegleValidation;
import tn.esprit.speakup.certificationservice.repositories.RegleValidationRepository;

import java.util.List;

/**
 * Endpoints REST pour la gestion des règles de validation (§7.5 – MS5).
 *
 * GET    /api/regles        → lister toutes les règles
 * POST   /api/regles        → créer une règle
 * PUT    /api/regles/{id}   → modifier une règle
 * DELETE /api/regles/{id}   → supprimer une règle
 */
@RestController
@RequestMapping("/api/regles")
@RequiredArgsConstructor
@Tag(name = "Règles de validation", description = "Gestion des seuils d'éligibilité par niveau CECRL")
public class RegleValidationController {

    private final RegleValidationRepository regleRepo;

    @GetMapping
    @Operation(summary = "Lister toutes les règles de validation")
    public ResponseEntity<List<RegleValidation>> getAll() {
        return ResponseEntity.ok(regleRepo.findAll());
    }

    @PostMapping
    @Operation(summary = "Créer une nouvelle règle de validation")
    public ResponseEntity<RegleValidation> create(@Valid @RequestBody RegleValidation regle) {
        regle.setId(null); // force insert
        return ResponseEntity.status(HttpStatus.CREATED).body(regleRepo.save(regle));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour une règle de validation")
    public ResponseEntity<RegleValidation> update(@PathVariable Long id,
                                                   @Valid @RequestBody RegleValidation regle) {
        if (!regleRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        regle.setId(id);
        return ResponseEntity.ok(regleRepo.save(regle));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une règle de validation")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!regleRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        regleRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
