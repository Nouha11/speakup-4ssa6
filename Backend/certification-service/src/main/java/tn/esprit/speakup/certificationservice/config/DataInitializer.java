package tn.esprit.speakup.certificationservice.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import tn.esprit.speakup.certificationservice.entities.NiveauEstime;
import tn.esprit.speakup.certificationservice.entities.RegleValidation;
import tn.esprit.speakup.certificationservice.enums.Langue;
import tn.esprit.speakup.certificationservice.enums.NiveauCECRL;
import tn.esprit.speakup.certificationservice.repositories.NiveauEstimeRepository;
import tn.esprit.speakup.certificationservice.repositories.RegleValidationRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Pré-charge des données de démonstration au démarrage.
 * Permet de tester POST /api/attestations immédiatement via Swagger.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final RegleValidationRepository regleRepo;
    private final NiveauEstimeRepository    niveauEstimeRepo;

    @Override
    public void run(String... args) {

        // ── Règles de validation ──────────────────────────────────────────────
        if (regleRepo.count() == 0) {
            regleRepo.saveAll(List.of(
                    RegleValidation.builder().niveau(NiveauCECRL.B1).noteMinimale(10.0).nbSessionsMin(1).build(),
                    RegleValidation.builder().niveau(NiveauCECRL.B2).noteMinimale(12.0).nbSessionsMin(2).build(),
                    RegleValidation.builder().niveau(NiveauCECRL.C1).noteMinimale(14.0).nbSessionsMin(2).build(),
                    RegleValidation.builder().niveau(NiveauCECRL.C2).noteMinimale(16.0).nbSessionsMin(3).build()
            ));
            log.info("✅ Règles de validation initialisées (B1→C2)");
        }

        // ── Niveau estimé de démonstration ───────────────────────────────────
        if (niveauEstimeRepo.count() == 0) {
            niveauEstimeRepo.saveAll(List.of(
                    NiveauEstime.builder()
                            .apprenantId("apprenant-demo-001")
                            .langue(Langue.FR)
                            .niveau(NiveauCECRL.B2)
                            .dateEstimation(LocalDateTime.now().minusDays(5))
                            .sourceSessionId(1L)
                            .build(),
                    NiveauEstime.builder()
                            .apprenantId("apprenant-demo-001")
                            .langue(Langue.EN)
                            .niveau(NiveauCECRL.C1)
                            .dateEstimation(LocalDateTime.now().minusDays(2))
                            .sourceSessionId(2L)
                            .build()
            ));
            log.info("✅ Niveaux estimés de démo initialisés (apprenant-demo-001 : B2/FR, C1/EN)");
        }
    }
}
