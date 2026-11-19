package tn.esprit.spring.progressservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.spring.progressservice.entity.AlerteFormateur;

import java.util.List;

@Repository
public interface AlerteFormateurRepository extends JpaRepository<AlerteFormateur, Long> {

    List<AlerteFormateur> findByGroupeId(Long groupeId);

    List<AlerteFormateur> findByGroupeIdAndTraitee(Long groupeId, boolean traitee);

    List<AlerteFormateur> findByTraitee(boolean traitee);
}
