
package tn.esprit.spring.recordingservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import tn.esprit.spring.recordingservice.entity.Enregistrement;
import tn.esprit.spring.recordingservice.repository.EnregistrementRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EnregistrementService {

    private final EnregistrementRepository enregistrementRepository;

    // Ajouter un nouvel enregistrement
    public Enregistrement ajouter(Enregistrement enregistrement) {
        return enregistrementRepository.save(enregistrement);
    }

    // Récupérer tous les enregistrements
    public List<Enregistrement> recupererTous() {
        return enregistrementRepository.findAll();
    }

    // Récupérer un enregistrement par son ID
    public Optional<Enregistrement> recupererParId(Long id) {
        return enregistrementRepository.findById(id);
    }

    // Récupérer les enregistrements d'un apprenant
    public List<Enregistrement> recupererParUtilisateur(Long utilisateurId) {
        return enregistrementRepository.findByUtilisateurId(utilisateurId);
    }

    // Récupérer les tentatives d'un apprenant pour un sujet
    public List<Enregistrement> recupererParUtilisateurEtSujet(
            Long utilisateurId, Long sujetId) {

        return enregistrementRepository
                .findByUtilisateurIdAndSujetIdOrderByDateCreationDesc(
                        utilisateurId, sujetId
                );
    }

    // Supprimer un enregistrement
    public void supprimer(Long id) {
        enregistrementRepository.deleteById(id);
    }
}
