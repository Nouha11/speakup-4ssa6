package tn.esprit.speakup.certificationservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.speakup.certificationservice.entities.RegleValidation;
import tn.esprit.speakup.certificationservice.enums.NiveauCECRL;

import java.util.Optional;

@Repository
public interface RegleValidationRepository extends JpaRepository<RegleValidation, Long> {

    Optional<RegleValidation> findByNiveau(NiveauCECRL niveau);
}
