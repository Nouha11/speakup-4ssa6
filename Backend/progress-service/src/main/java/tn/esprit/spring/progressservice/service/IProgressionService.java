package tn.esprit.spring.progressservice.service;

import tn.esprit.spring.progressservice.entity.AlerteFormateur;
import tn.esprit.spring.progressservice.entity.IndicateurCritere;
import tn.esprit.spring.progressservice.entity.ProgressionApprenant;
import tn.esprit.spring.progressservice.enums.Critere;
import tn.esprit.spring.progressservice.enums.Langue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface IProgressionService {

    // --- Progression ---

    ProgressionApprenant getProgression(String apprenantId, Langue langue);

    List<IndicateurCritere> getCourbes(String apprenantId, Critere critere, LocalDateTime depuis);

    List<ProgressionApprenant> getSyntheseGroupe(Long groupeId);

    void deleteProgression(String apprenantId);

    // --- Indicateurs ---

    List<IndicateurCritere> getIndicateurs(String apprenantId, Critere critere);

    // --- Alertes ---

    List<AlerteFormateur> getAlertes(Long groupeId, Boolean traitee);

    AlerteFormateur marquerAlertTraitee(Long alerteId);

    // --- Statistiques ---

    Map<String, Object> getStatistiquesEtablissement();
}
