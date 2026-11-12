package tn.esprit.speakup.profile.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Squelette du contrat d'API (validation 1) : la logique metier arrive apres validation de la conception.
@RestController
@RequestMapping("/api/groupes")
public class GroupeController {

    // 6. Creer un groupe avec un parcours, FORMATEUR (201, 400, 403, 404)
    @PostMapping
    public ResponseEntity<String> creerGroupe(@RequestBody Object groupeRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body("Groupe cree");
    }

    // 7. Ajouter un apprenant par email, formateur du groupe (201, 400, 403, 404, 409, 503)
    @PostMapping("/{id}/membres")
    public ResponseEntity<String> ajouterMembre(@PathVariable Long id, @RequestBody Object membreRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body("Membre ajoute au groupe " + id);
    }

    // 8. Lister les membres d'un groupe ; appele par progress-service (200, 403, 404)
    @GetMapping("/{id}/membres")
    public ResponseEntity<String> getMembres(@PathVariable Long id) {
        return ResponseEntity.ok("Membres du groupe " + id);
    }
}
