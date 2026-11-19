package tn.esprit.spring.progressservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.spring.progressservice.entity.AlerteFormateur;
import tn.esprit.spring.progressservice.entity.IndicateurCritere;
import tn.esprit.spring.progressservice.entity.ProgressionApprenant;
import tn.esprit.spring.progressservice.enums.Critere;
import tn.esprit.spring.progressservice.enums.Langue;
import tn.esprit.spring.progressservice.repository.AlerteFormateurRepository;
import tn.esprit.spring.progressservice.repository.IndicateurCritereRepository;
import tn.esprit.spring.progressservice.repository.ProgressionApprenantRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProgressionServiceImpl implements IProgressionService {

    private final ProgressionApprenantRepository progressionRepo;
    private final IndicateurCritereRepository indicateurRepo;
    private final AlerteFormateurRepository alerteRepo;

    // ------------------------------------------------------------------ //
    //  Progression                                                         //
    // ------------------------------------------------------------------ //

    @Override
    public ProgressionApprenant getProgression(String apprenantId, Langue langue) {
        if (langue != null) {
            return progressionRepo.findByApprenantIdAndLangue(apprenantId, langue)
                    .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException(
                            "Aucune progression trouvée pour l'apprenant " + apprenantId));
        }
        return progressionRepo.findByApprenantId(apprenantId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException(
                        "Aucune progression trouvée pour l'apprenant " + apprenantId));
    }

    @Override
    public List<IndicateurCritere> getCourbes(String apprenantId, Critere critere, LocalDateTime depuis) {
        LocalDateTime from = (depuis != null) ? depuis : LocalDateTime.of(2000, 1, 1, 0, 0);
        return indicateurRepo.findByProgression_ApprenantIdAndCritereAndDateMesureAfter(
                apprenantId, critere, from);
    }

    @Override
    public List<ProgressionApprenant> getSyntheseGroupe(Long groupeId) {
        // Returns all progressions whose alerte references this groupeId.
        // A proper implementation would call profile-service (OpenFeign) to get
        // group members; for now we query through alertes to find known apprenantIds.
        List<AlerteFormateur> alertes = alerteRepo.findByGroupeId(groupeId);
        List<String> apprenantIds = alertes.stream()
                .map(AlerteFormateur::getApprenantId)
                .distinct()
                .toList();
        return apprenantIds.stream()
                .flatMap(id -> progressionRepo.findByApprenantId(id).stream())
                .toList();
    }

    @Override
    @Transactional
    public void deleteProgression(String apprenantId) {
        progressionRepo.deleteByApprenantId(apprenantId);
    }

    // ------------------------------------------------------------------ //
    //  Indicateurs                                                         //
    // ------------------------------------------------------------------ //

    @Override
    public List<IndicateurCritere> getIndicateurs(String apprenantId, Critere critere) {
        if (critere != null) {
            return indicateurRepo.findByProgression_ApprenantIdAndCritere(apprenantId, critere);
        }
        return indicateurRepo.findByProgression_ApprenantId(apprenantId);
    }

    // ------------------------------------------------------------------ //
    //  Alertes                                                             //
    // ------------------------------------------------------------------ //

    @Override
    public List<AlerteFormateur> getAlertes(Long groupeId, Boolean traitee) {
        if (groupeId != null && traitee != null) {
            return alerteRepo.findByGroupeIdAndTraitee(groupeId, traitee);
        }
        if (groupeId != null) {
            return alerteRepo.findByGroupeId(groupeId);
        }
        if (traitee != null) {
            return alerteRepo.findByTraitee(traitee);
        }
        return alerteRepo.findAll();
    }

    @Override
    @Transactional
    public AlerteFormateur marquerAlertTraitee(Long alerteId) {
        AlerteFormateur alerte = alerteRepo.findById(alerteId)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException(
                        "Alerte introuvable : " + alerteId));
        alerte.setTraitee(true);
        return alerteRepo.save(alerte);
    }

    // ------------------------------------------------------------------ //
    //  Statistiques établissement                                          //
    // ------------------------------------------------------------------ //

    @Override
    public Map<String, Object> getStatistiquesEtablissement() {
        long totalProgressions = progressionRepo.count();
        long totalAlertes = alerteRepo.count();
        long alertesNonTraitees = alerteRepo.findByTraitee(false).size();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalProgressions", totalProgressions);
        stats.put("totalAlertes", totalAlertes);
        stats.put("alertesNonTraitees", alertesNonTraitees);
        return stats;
    }
}
