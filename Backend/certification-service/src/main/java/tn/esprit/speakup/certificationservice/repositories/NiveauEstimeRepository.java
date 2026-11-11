package tn.esprit.speakup.certificationservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.speakup.certificationservice.entities.NiveauEstime;
import tn.esprit.speakup.certificationservice.enums.Langue;

import java.util.List;
import java.util.Optional;

@Repository
public interface NiveauEstimeRepository extends JpaRepository<NiveauEstime, Long> {

    /** Dernier niveau estimé d'un apprenant pour une langue donnée */
    Optional<NiveauEstime> findFirstByApprenantIdAndLangueOrderByDateEstimationDesc(
            String apprenantId, Langue langue);

    List<NiveauEstime> findByApprenantId(String apprenantId);
}
