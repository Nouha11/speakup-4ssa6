
package tn.esprit.spring.recordingservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tn.esprit.spring.recordingservice.entity.EvenementOutbox;
import tn.esprit.spring.recordingservice.entity.enums.StatutEvenement;
import tn.esprit.spring.recordingservice.service.EvenementOutboxService;

import java.util.List;

@RestController
@RequestMapping("/api/outbox")
@RequiredArgsConstructor
public class EvenementOutboxController {

    private final EvenementOutboxService evenementOutboxService;

    // 1. Récupérer tous les événements
    @GetMapping
    public ResponseEntity<List<EvenementOutbox>> recupererTous() {

        return ResponseEntity.ok(
                evenementOutboxService.recupererTous()
        );
    }

    // 2. Récupérer un événement par son ID
    @GetMapping("/{id}")
    public ResponseEntity<EvenementOutbox> recupererParId(
            @PathVariable Long id) {

        return evenementOutboxService.recupererParId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. Récupérer les événements selon leur statut
    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<EvenementOutbox>> recupererParStatut(
            @PathVariable StatutEvenement statut) {

        return ResponseEntity.ok(
                evenementOutboxService.recupererParStatut(statut)
        );
    }

    // 4. Récupérer les événements en attente
    @GetMapping("/pending")
    public ResponseEntity<List<EvenementOutbox>> recupererEnAttente() {

        return ResponseEntity.ok(
                evenementOutboxService.recupererEvenementsEnAttente()
        );
    }

    // 5. Marquer un événement comme publié
    @PutMapping("/{id}/publier")
    public ResponseEntity<EvenementOutbox> marquerCommePublie(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                evenementOutboxService.marquerCommePublie(id)
        );
    }
}
