package tn.esprit.spring.contentservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.spring.contentservice.entity.Parcours;
import tn.esprit.spring.contentservice.entity.enums.Langue;
import tn.esprit.spring.contentservice.entity.enums.NiveauCECRL;
import tn.esprit.spring.contentservice.repository.ParcoursRepository;
import tn.esprit.spring.contentservice.repository.SujetOralRepository;

import java.util.List;

@RestController
@RequestMapping("/api/parcours")
@RequiredArgsConstructor
@Tag(name = "Parcours", description = "Suites ordonnees de sujets creees par les formateurs")
public class ParcoursController {

    /** Corps de requete : un parcours et la liste ordonnee des id de ses sujets. */
    public record ParcoursRequest(String titre, String description, Langue langue, NiveauCECRL niveau,
                                  String auteurId, List<Long> sujetIds) {}

    private final ParcoursRepository repository;
    private final SujetOralRepository sujetRepository;

    @GetMapping
    @Operation(summary = "Lister les parcours (filtres optionnels)")
    public List<Parcours> lister(@RequestParam(required = false) Langue langue,
                                 @RequestParam(required = false) NiveauCECRL niveau) {
        return repository.findAll().stream()
                .filter(p -> langue == null || p.getLangue() == langue)
                .filter(p -> niveau == null || p.getNiveau() == niveau)
                .toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulter un parcours et ses sujets dans l'ordre (appele par profile-service)")
    public Parcours trouver(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parcours " + id + " introuvable"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Creer un parcours (role FORMATEUR a partir de S8)")
    public Parcours creer(@RequestBody ParcoursRequest req) {
        return repository.save(remplir(new Parcours(), req));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un parcours (son auteur, a partir de S8)")
    public Parcours modifier(@PathVariable Long id, @RequestBody ParcoursRequest req) {
        return repository.save(remplir(trouver(id), req));
    }

    private Parcours remplir(Parcours p, ParcoursRequest req) {
        p.setTitre(req.titre());
        p.setDescription(req.description());
        p.setLangue(req.langue());
        p.setNiveau(req.niveau());
        p.setAuteurId(req.auteurId());
        p.getSujets().clear();
        if (req.sujetIds() != null) {
            p.getSujets().addAll(sujetRepository.findAllById(req.sujetIds()));
        }
        return p;
    }
}
