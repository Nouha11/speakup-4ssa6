
package tn.esprit.spring.recordingservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tn.esprit.spring.recordingservice.entity.ConsentementAudio;
import tn.esprit.spring.recordingservice.service.ConsentementAudioService;

import java.util.List;

@RestController
@RequestMapping("/api/consentements")
@RequiredArgsConstructor
public class ConsentementAudioController {

    private final ConsentementAudioService consentementAudioService;

    // 1. Ajouter un consentement
    @PostMapping
    public ResponseEntity<ConsentementAudio> ajouter(
            @RequestBody ConsentementAudio consentement) {

        ConsentementAudio nouveauConsentement =
                consentementAudioService.ajouter(consentement);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(nouveauConsentement);
    }

    // 2. Récupérer tous les consentements
    @GetMapping
    public ResponseEntity<List<ConsentementAudio>> recupererTous() {

        return ResponseEntity.ok(
                consentementAudioService.recupererTous()
        );
    }

    // 3. Récupérer un consentement par son ID
    @GetMapping("/{id}")
    public ResponseEntity<ConsentementAudio> recupererParId(
            @PathVariable Long id) {

        return consentementAudioService.recupererParId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 4. Récupérer le consentement d'un enregistrement
    @GetMapping("/enregistrement/{enregistrementId}")
    public ResponseEntity<ConsentementAudio> recupererParEnregistrement(
            @PathVariable Long enregistrementId) {

        return consentementAudioService
                .recupererParEnregistrement(enregistrementId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 5. Vérifier si un enregistrement possède un consentement
    @GetMapping("/enregistrement/{enregistrementId}/existe")
    public ResponseEntity<Boolean> possedeConsentement(
            @PathVariable Long enregistrementId) {

        return ResponseEntity.ok(
                consentementAudioService.possedeConsentement(enregistrementId)
        );
    }
}
