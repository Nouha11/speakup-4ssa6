package tn.esprit.spring.contentservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.spring.contentservice.entity.ProfilErreurs;
import tn.esprit.spring.contentservice.entity.SujetOral;
import tn.esprit.spring.contentservice.entity.enums.Langue;
import tn.esprit.spring.contentservice.entity.enums.TypeErreur;
import tn.esprit.spring.contentservice.repository.ProfilErreursRepository;
import tn.esprit.spring.contentservice.repository.SujetOralRepository;

import java.util.List;

@RestController
@RequestMapping("/api/recommandations")
@RequiredArgsConstructor
@Tag(name = "Recommandations", description = "Sujets cibles sur les erreurs recurrentes, calcules a la demande")
public class RecommandationController {

    /** Un sujet recommande et l'erreur qu'il aide a travailler. */
    public record Recommandation(TypeErreur erreurCiblee, int occurrences, SujetOral sujet) {}

    private final ProfilErreursRepository profilRepository;
    private final SujetOralRepository sujetRepository;

    @GetMapping
    @Operation(summary = "Sujets recommandes : erreurs frequentes x competencesCiblees (non stockes)")
    public List<Recommandation> recommander(@RequestParam String apprenantId,
                                            @RequestParam(required = false) Langue langue) {
        List<SujetOral> sujets = sujetRepository.findAll();
        return profilRepository.findByApprenantIdOrderByOccurrencesDesc(apprenantId).stream()
                .filter(e -> langue == null || e.getLangue() == langue)
                .flatMap((ProfilErreurs e) -> sujets.stream()
                        .filter(s -> s.getLangue() == e.getLangue()
                                && s.getCompetencesCiblees().contains(e.getTypeErreur()))
                        .map(s -> new Recommandation(e.getTypeErreur(), e.getOccurrences(), s)))
                .limit(5)
                .toList();
    }
}
