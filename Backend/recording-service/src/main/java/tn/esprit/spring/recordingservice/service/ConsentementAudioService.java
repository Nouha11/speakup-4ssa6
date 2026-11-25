
package tn.esprit.spring.recordingservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tn.esprit.spring.recordingservice.entity.ConsentementAudio;
import tn.esprit.spring.recordingservice.repository.ConsentementAudioRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ConsentementAudioService {

    private final ConsentementAudioRepository consentementAudioRepository;

    // 1. Ajouter un consentement
    public ConsentementAudio ajouter(ConsentementAudio consentement) {

        Long enregistrementId = consentement.getEnregistrement().getId();

        // Vérifier si un consentement existe déjà
        if (consentementAudioRepository.existsByEnregistrement_Id(enregistrementId)) {
            throw new IllegalStateException(
                    "Un consentement existe déjà pour cet enregistrement."
            );
        }

        // Vérifier que le consentement est accepté
        if (!Boolean.TRUE.equals(consentement.getAccepte())) {
            throw new IllegalArgumentException(
                    "Le consentement audio doit être accepté pour envoyer un enregistrement."
            );
        }

        return consentementAudioRepository.save(consentement);
    }

    // 2. Récupérer un consentement par son ID
    @Transactional(readOnly = true)
    public Optional<ConsentementAudio> recupererParId(Long id) {
        return consentementAudioRepository.findById(id);
    }

    // 3. Récupérer le consentement d'un enregistrement
    @Transactional(readOnly = true)
    public Optional<ConsentementAudio> recupererParEnregistrement(Long enregistrementId) {
        return consentementAudioRepository.findByEnregistrement_Id(enregistrementId);
    }

    // 4. Vérifier si un enregistrement possède un consentement
    @Transactional(readOnly = true)
    public boolean possedeConsentement(Long enregistrementId) {
        return consentementAudioRepository.existsByEnregistrement_Id(enregistrementId);
    }

    // 5. Récupérer tous les consentements
    @Transactional(readOnly = true)
    public java.util.List<ConsentementAudio> recupererTous() {
        return consentementAudioRepository.findAll();
    }
}
