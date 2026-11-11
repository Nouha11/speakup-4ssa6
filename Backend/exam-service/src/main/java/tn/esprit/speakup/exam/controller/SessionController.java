package tn.esprit.speakup.exam.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.speakup.exam.model.SessionExamen;
import tn.esprit.speakup.exam.model.NoteExamen;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    // 1. Démarrer une session d'examen (APPRENANT)
    @PostMapping
    public ResponseEntity<String> creerSession(@RequestBody Object sessionRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body("Session creee avec succes");
    }

    // 2. Consulter l'état d'une session
    @GetMapping("/{id}")
    public ResponseEntity<String> getSession(@PathVariable Long id) {
        return ResponseEntity.ok("Details de la session " + id);
    }

    // 3. Démarrer la préparation (Transition CREEE -> EN_PREPARATION)
    @PostMapping("/{id}/preparation/start")
    public ResponseEntity<String> demarrerPreparation(@PathVariable Long id) {
        return ResponseEntity.ok("Preparation demarree");
    }

    // 4. Démarrer le passage oral (Transition EN_PREPARATION -> EN_PASSAGE)
    @PostMapping("/{id}/passage/start")
    public ResponseEntity<String> demarrerPassage(@PathVariable Long id) {
        return ResponseEntity.ok("Passage demarre");
    }

    // 5. Associer un enregistrement (202 Accepted : analyse asynchrone)
    @PostMapping("/{id}/reponses")
    public ResponseEntity<String> associerReponse(@PathVariable Long id, @RequestBody Object reponse) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Reponse recue, analyse en cours...");
    }

    // 6. Consulter la note de l'épreuve
    @GetMapping("/{id}/note")
    public ResponseEntity<String> getNote(@PathVariable Long id) {
        return ResponseEntity.ok("Note estimee");
    }

    // 7. Ajuster et valider la note (FORMATEUR)
    @PutMapping("/{id}/note")
    public ResponseEntity<String> ajusterNote(@PathVariable Long id, @RequestBody Object noteRequest) {
        return ResponseEntity.ok("Note validee par le formateur");
    }

    // 8. Historique des épreuves par apprenant
    @GetMapping
    public ResponseEntity<String> getHistorique(@RequestParam(required = false) String apprenantId) {
        return ResponseEntity.ok("Historique des sessions");
    }
}