package tn.esprit.speakup.profile.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Squelette du contrat d'API (validation 1) : la logique metier arrive apres validation de la conception.
@RestController
@RequestMapping("/api/profils")
public class ProfilController {

    // 1. Profil de l'utilisateur connecte, cree a la premiere requete a partir du JWT (200)
    @GetMapping("/me")
    public ResponseEntity<String> getMonProfil() {
        return ResponseEntity.ok("Profil de l'utilisateur connecte");
    }

    // 2. Modifier ses objectifs et preferences (200, 400)
    @PutMapping("/me")
    public ResponseEntity<String> modifierMonProfil(@RequestBody Object profilRequest) {
        return ResponseEntity.ok("Profil mis a jour");
    }

    // 3. Consulter un profil : soi-meme, formateur d'un de ses groupes, ADMIN (200, 403, 404)
    @GetMapping("/{keycloakId}")
    public ResponseEntity<String> getProfil(@PathVariable String keycloakId) {
        return ResponseEntity.ok("Profil " + keycloakId);
    }

    // 4. Rechercher des profils : FORMATEUR (ses groupes), ADMIN (200, 400, 403)
    @GetMapping
    public ResponseEntity<String> rechercherProfils(@RequestParam(required = false) Long groupeId,
                                                    @RequestParam(required = false) String langue,
                                                    @RequestParam(required = false) String niveau) {
        return ResponseEntity.ok("Liste des profils");
    }

    // 5. Effacer un profil : soi-meme ou ADMIN ; publie user.deleted (204, 403, 404, 409, 503)
    @DeleteMapping("/{keycloakId}")
    public ResponseEntity<Void> effacerProfil(@PathVariable String keycloakId) {
        return ResponseEntity.noContent().build();
    }
}
