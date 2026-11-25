
package tn.esprit.spring.recordingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tn.esprit.spring.recordingservice.entity.Enregistrement;
import tn.esprit.spring.recordingservice.entity.enums.StatutEnregistrement;

import java.util.List;

@Repository
public interface EnregistrementRepository
        extends JpaRepository<Enregistrement, Long> {

    List<Enregistrement> findByUtilisateurId(Long utilisateurId);

    List<Enregistrement> findByUtilisateurIdAndSujetIdOrderByDateCreationDesc(
            Long utilisateurId,
            Long sujetId
    );

    List<Enregistrement> findByStatut(StatutEnregistrement statut);
}
