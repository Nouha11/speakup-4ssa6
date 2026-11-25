
package tn.esprit.spring.recordingservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import tn.esprit.spring.recordingservice.entity.EvenementOutbox;
import tn.esprit.spring.recordingservice.entity.enums.StatutEvenement;
import tn.esprit.spring.recordingservice.repository.EvenementOutboxRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EvenementOutboxService {

    private final EvenementOutboxRepository evenementOutboxRepository;

    // 1. Ajouter un événement
    public EvenementOutbox ajouter(EvenementOutbox evenement) {
        return evenementOutboxRepository.save(evenement);
    }

    // 2. Récupérer tous les événements
    public List<EvenementOutbox> recupererTous() {
        return evenementOutboxRepository.findAll();
    }

    // 3. Récupérer un événement par son ID
    public Optional<EvenementOutbox> recupererParId(Long id) {
        return evenementOutboxRepository.findById(id);
    }

    // 4. Récupérer les événements selon leur statut
    public List<EvenementOutbox> recupererParStatut(
            StatutEvenement statut) {
        return evenementOutboxRepository.findByStatut(statut);
    }

    // 5. Récupérer les événements en attente
    public List<EvenementOutbox> recupererEvenementsEnAttente() {
        return evenementOutboxRepository
                .findByStatutOrderByDateCreationAsc(
                        StatutEvenement.PENDING
                );
    }

    // 6. Marquer un événement comme publié
    public EvenementOutbox marquerCommePublie(Long id) {

        EvenementOutbox evenement =
                evenementOutboxRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException(
                                "Événement introuvable : " + id
                        ));

        evenement.setStatut(StatutEvenement.PUBLISHED);
        evenement.setDatePublication(LocalDateTime.now());

        return evenementOutboxRepository.save(evenement);
    }
}
