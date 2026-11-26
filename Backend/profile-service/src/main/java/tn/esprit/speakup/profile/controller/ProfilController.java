package tn.esprit.speakup.profile.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Squelette du contrat d'API (validation 1) : la logique metier arrive apres validation de la conception.
@RestController
@RequestMapping("/api/profils")
@Tag(name = "Profil", description = "Gestion des profils apprenants et formateurs")
public class ProfilController {

    // 1. Profil de l'utilisateur connecte, cree a la premiere requete a partir du JWT (200)
    @Operation(summary = "Récupérer le profil de l'utilisateur connecté")
    @ApiResponse(responseCode = "200", description = "Profil trouvé")
    @GetMapping("/me")
    public ResponseEntity<String> getMonProfil() {
        return ResponseEntity.ok("Profil de l'utilisateur connecte");
    }

    // 2. Modifier ses objectifs et preferences (200, 400)
    @Operation(summary = "Modifier ses objectifs et préférences")
    @ApiResponse(responseCode = "200", description = "Profil mis à jour")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    @PutMapping("/me")
    public ResponseEntity<String> modifierMonProfil(@RequestBody Object profilRequest) {
        return ResponseEntity.ok("Profil mis a jour");
    }

    // 3. Consulter un profil : soi-meme, formateur d'un de ses groupes, ADMIN (200, 403, 404)
    @Operation(summary = "Récupérer un profil par son keycloakId")
    @ApiResponse(responseCode = "200", description = "Profil trouvé")
    @ApiResponse(responseCode = "404", description = "Profil introuvable")
    @GetMapping("/{keycloakId}")
    public ResponseEntity<String> getProfil(@PathVariable String keycloakId) {
        return ResponseEntity.ok("Profil " + keycloakId);
    }

    // 4. Rechercher des profils : FORMATEUR (ses groupes), ADMIN (200, 400, 403)
    @Operation(summary = "Rechercher des profils (filtres optionnels)")
    @ApiResponse(responseCode = "200", description = "Liste des profils")
    @GetMapping
    public ResponseEntity<String> rechercherProfils(@RequestParam(required = false) Long groupeId,
                                                    @RequestParam(required = false) String langue,
                                                    @RequestParam(required = false) String niveau) {
        return ResponseEntity.ok("Liste des profils");
    }

    // 5. Effacer un profil : soi-meme ou ADMIN ; publie user.deleted (204, 403, 404, 409, 503)
    @Operation(summary = "Effacer un profil")
    @ApiResponse(responseCode = "204", description = "Profil effacé")
    @ApiResponse(responseCode = "404", description = "Profil introuvable")
    @DeleteMapping("/{keycloakId}")
    public ResponseEntity<Void> effacerProfil(@PathVariable String keycloakId) {
        return ResponseEntity.noContent().build();
    }
}
