package tn.esprit.spring.recordingservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tn.esprit.spring.recordingservice.entity.Enregistrement;
import tn.esprit.spring.recordingservice.service.EnregistrementService;

import java.util.List;

@RestController
@RequestMapping("/api/enregistrements")
@RequiredArgsConstructor
public class EnregistrementController {

    private final EnregistrementService enregistrementService;

    // 1. Ajouter un enregistrement
    @PostMapping
    public ResponseEntity<Enregistrement> ajouter(
            @RequestBody Enregistrement enregistrement) {

        Enregistrement nouvelEnregistrement =
                enregistrementService.ajouter(enregistrement);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(nouvelEnregistrement);
    }

    // 2. Récupérer tous les enregistrements
    @GetMapping
    public ResponseEntity<List<Enregistrement>> recupererTous() {

        return ResponseEntity.ok(
                enregistrementService.recupererTous()
        );
    }

    // 3. Récupérer un enregistrement par son ID
    @GetMapping("/{id}")
    public ResponseEntity<Enregistrement> recupererParId(
            @PathVariable Long id) {

        return enregistrementService.recupererParId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 4. Récupérer les enregistrements d'un utilisateur
    @GetMapping("/utilisateur/{utilisateurId}")
    public ResponseEntity<List<Enregistrement>> recupererParUtilisateur(
            @PathVariable Long utilisateurId) {

        return ResponseEntity.ok(
                enregistrementService.recupererParUtilisateur(utilisateurId)
        );
    }

    // 5. Récupérer les enregistrements d'un utilisateur pour un sujet
    @GetMapping("/utilisateur/{utilisateurId}/sujet/{sujetId}")
    public ResponseEntity<List<Enregistrement>> recupererParUtilisateurEtSujet(
            @PathVariable Long utilisateurId,
            @PathVariable Long sujetId) {

        return ResponseEntity.ok(
                enregistrementService.recupererParUtilisateurEtSujet(
                        utilisateurId, sujetId
                )
        );
    }
}
