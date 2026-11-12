package tn.esprit.spring.contentservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.spring.contentservice.entity.SujetOral;
import tn.esprit.spring.contentservice.entity.enums.Langue;
import tn.esprit.spring.contentservice.entity.enums.NiveauCECRL;
import tn.esprit.spring.contentservice.entity.enums.TypeTache;
import tn.esprit.spring.contentservice.repository.SujetOralRepository;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/api/sujets")
@RequiredArgsConstructor
@Tag(name = "Sujets d'oral", description = "Banque de sujets FR/EN, niveaux B1 a C2")
public class SujetOralController {

    private final SujetOralRepository repository;

    @GetMapping
    @Operation(summary = "Rechercher des sujets (filtres optionnels)")
    public List<SujetOral> rechercher(@RequestParam(required = false) Langue langue,
                                      @RequestParam(required = false) NiveauCECRL niveau,
                                      @RequestParam(required = false) TypeTache type) {
        return repository.findAll().stream()
                .filter(s -> langue == null || s.getLangue() == langue)
                .filter(s -> niveau == null || s.getNiveau() == niveau)
                .filter(s -> type == null || s.getTypeTache() == type)
                .toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulter un sujet (appele par recording-service)")
    public SujetOral trouver(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sujet " + id + " introuvable"));
    }

    @GetMapping("/aleatoire")
    @Operation(summary = "Tirer un sujet au sort avec ses relances (appele par exam-service)")
    public SujetOral aleatoire(@RequestParam(required = false) Langue langue,
                               @RequestParam(required = false) NiveauCECRL niveau,
                               @RequestParam(required = false) TypeTache type) {
        List<SujetOral> candidats = rechercher(langue, niveau, type);
        if (candidats.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aucun sujet ne correspond a ces criteres");
        }
        return candidats.get(ThreadLocalRandom.current().nextInt(candidats.size()));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Creer un sujet (role FORMATEUR a partir de S8)")
    public SujetOral creer(@RequestBody SujetOral sujet) {
        sujet.setId(null);
        return repository.save(sujet);
    }
}
