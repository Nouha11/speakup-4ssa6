package tn.esprit.spring.progressservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.spring.progressservice.entity.IndicateurCritere;
import tn.esprit.spring.progressservice.enums.Critere;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IndicateurCritereRepository extends JpaRepository<IndicateurCritere, Long> {

    List<IndicateurCritere> findByProgression_ApprenantIdAndCritereAndDateMesureAfter(
            String apprenantId, Critere critere, LocalDateTime depuis);

    List<IndicateurCritere> findByProgression_ApprenantId(String apprenantId);

    List<IndicateurCritere> findByProgression_ApprenantIdAndCritere(String apprenantId, Critere critere);
}
