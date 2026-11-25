
package tn.esprit.spring.recordingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.spring.recordingservice.entity.EvenementOutbox;
import tn.esprit.spring.recordingservice.entity.enums.StatutEvenement;

import java.util.List;

@Repository
public interface EvenementOutboxRepository
        extends JpaRepository<EvenementOutbox, Long> {

    List<EvenementOutbox> findByStatut(StatutEvenement statut);

    List<EvenementOutbox> findByStatutOrderByDateCreationAsc(
            StatutEvenement statut
    );

    List<EvenementOutbox> findByAggregateId(Long aggregateId);
}