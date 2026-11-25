
package tn.esprit.spring.recordingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.spring.recordingservice.entity.ConsentementAudio;

import java.util.Optional;

@Repository
public interface ConsentementAudioRepository
        extends JpaRepository<ConsentementAudio, Long> {

    // Rechercher le consentement d'un enregistrement
    Optional<ConsentementAudio> findByEnregistrement_Id(Long enregistrementId);

    // Vérifier si un enregistrement possède déjà un consentement
    boolean existsByEnregistrement_Id(Long enregistrementId);
}
