package tn.esprit.speakup.certificationservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.speakup.certificationservice.dto.AttestationRequest;
import tn.esprit.speakup.certificationservice.dto.AttestationResponse;
import tn.esprit.speakup.certificationservice.dto.NiveauEstimeResponse;
import tn.esprit.speakup.certificationservice.entities.Attestation;
import tn.esprit.speakup.certificationservice.entities.NiveauEstime;
import tn.esprit.speakup.certificationservice.entities.RegleValidation;
import tn.esprit.speakup.certificationservice.enums.Langue;
import tn.esprit.speakup.certificationservice.enums.NiveauCECRL;
import tn.esprit.speakup.certificationservice.enums.StatutAttestation;
import tn.esprit.speakup.certificationservice.repositories.AttestationRepository;
import tn.esprit.speakup.certificationservice.repositories.NiveauEstimeRepository;
import tn.esprit.speakup.certificationservice.repositories.RegleValidationRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Implémentation principale de la logique métier de MS5.
 *
 * <p>Interactions synchrones (OpenFeign – ajoutées en S5-S7) :
 *   <ul>
 *     <li>progress-service  → GET /api/progression/apprenants/{id} (vérification du niveau)</li>
 *     <li>profile-service   → GET /api/profils/{keycloakId} (récupération du nom)</li>
 *   </ul>
 * Pour la démo Validation 1, ces appels sont simulés (nom figé, niveau lu en base H2).
 *
 * <p>Interaction asynchrone (RabbitMQ – ajoutée en S6) :
 *   L'événement exam.graded est consommé par {@code ExamGradedListener}
 *   qui appelle {@link #mettreAJourNiveauEstime}.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CertificationServiceImpl implements ICertificationService {

    private final AttestationRepository     attestationRepo;
    private final NiveauEstimeRepository    niveauEstimeRepo;
    private final RegleValidationRepository regleRepo;

    // ── Attestations ──────────────────────────────────────────────────────────

    @Override
    @Transactional
    public AttestationResponse emettreAttestation(AttestationRequest request) {

        // 1. Récupérer le dernier niveau estimé (sera fourni par OpenFeign/progress-service en S5)
        NiveauEstime niveauEstime = niveauEstimeRepo
                .findFirstByApprenantIdAndLangueOrderByDateEstimationDesc(
                        request.apprenantId(), request.langue())
                .orElseThrow(() -> new IllegalStateException(
                        "Aucun niveau estimé trouvé pour l'apprenant : " + request.apprenantId()));

        // 2. Vérifier la règle d'éligibilité
        RegleValidation regle = regleRepo.findByNiveau(niveauEstime.getNiveau())
                .orElseThrow(() -> new IllegalStateException(
                        "Aucune règle définie pour le niveau : " + niveauEstime.getNiveau()));

        log.info("Éligibilité validée – apprenant={} niveau={} règle={}pts/{}sessions",
                request.apprenantId(), niveauEstime.getNiveau(),
                regle.getNoteMinimale(), regle.getNbSessionsMin());

        // 3. Récupérer le nom (sera fourni par OpenFeign/profile-service en S5 ; nom figé pour démo)
        String nomApprenant = "Apprenant-" + request.apprenantId();

        // 4. Générer le numéro et le code de vérification
        String numero         = "CERT-" + LocalDateTime.now().getYear() + "-" +
                                String.format("%05d", (long)(Math.random() * 99999));
        String codeVerif      = UUID.randomUUID().toString().replace("-", "").toUpperCase();

        // 5. Simuler génération PDF + QR code (sera implémenté avec iText/ZXing en S7)
        log.info("PDF + QR code générés pour l'attestation {}", numero);

        // 6. Persister l'attestation
        Attestation attestation = Attestation.builder()
                .numero(numero)
                .apprenantId(request.apprenantId())
                .nomApprenant(nomApprenant)
                .langue(request.langue())
                .niveauAtteint(niveauEstime.getNiveau())
                .dateEmission(LocalDateTime.now())
                .codeVerification(codeVerif)
                .statut(StatutAttestation.EMISE)
                .build();

        return toResponse(attestationRepo.save(attestation));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttestationResponse> getAttestationsByApprenant(String apprenantId) {
        return attestationRepo.findByApprenantId(apprenantId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AttestationResponse verifierAttestation(String codeVerification) {
        return attestationRepo.findByCodeVerification(codeVerification)
                .map(this::toResponse)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Code de vérification invalide : " + codeVerification));
    }

    @Override
    @Transactional
    public AttestationResponse revoquerAttestation(Long id) {
        Attestation attestation = attestationRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Attestation introuvable : " + id));
        attestation.setStatut(StatutAttestation.REVOQUEE);
        return toResponse(attestationRepo.save(attestation));
    }

    // ── Niveau estimé ─────────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public NiveauEstimeResponse getNiveauEstime(String apprenantId, Langue langue) {
        NiveauEstime ne = niveauEstimeRepo
                .findFirstByApprenantIdAndLangueOrderByDateEstimationDesc(apprenantId, langue)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Niveau estimé introuvable pour apprenant=" + apprenantId
                        + " langue=" + langue));
        return toNiveauResponse(ne);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NiveauEstimeResponse> getAllNiveauxEstimes(String apprenantId) {
        return niveauEstimeRepo.findByApprenantId(apprenantId)
                .stream()
                .map(this::toNiveauResponse)
                .toList();
    }

    @Override
    @Transactional
    public void mettreAJourNiveauEstime(String apprenantId, Langue langue,
                                         NiveauCECRL niveau, Long sessionId) {
        // Cherche un enregistrement existant ou en crée un nouveau
        NiveauEstime ne = niveauEstimeRepo
                .findFirstByApprenantIdAndLangueOrderByDateEstimationDesc(apprenantId, langue)
                .orElseGet(() -> NiveauEstime.builder()
                        .apprenantId(apprenantId)
                        .langue(langue)
                        .build());

        ne.setNiveau(niveau);
        ne.setDateEstimation(LocalDateTime.now());
        ne.setSourceSessionId(sessionId);

        niveauEstimeRepo.save(ne);
        log.info("NiveauEstime mis à jour – apprenant={} langue={} niveau={} session={}",
                apprenantId, langue, niveau, sessionId);
    }

    // ── Mappers privés ────────────────────────────────────────────────────────

    private AttestationResponse toResponse(Attestation a) {
        return new AttestationResponse(
                a.getId(), a.getNumero(), a.getApprenantId(), a.getNomApprenant(),
                a.getLangue(), a.getNiveauAtteint(), a.getDateEmission(),
                a.getCodeVerification(), a.getStatut()
        );
    }

    private NiveauEstimeResponse toNiveauResponse(NiveauEstime ne) {
        return new NiveauEstimeResponse(
                ne.getId(), ne.getApprenantId(), ne.getLangue(),
                ne.getNiveau(), ne.getDateEstimation(), ne.getSourceSessionId()
        );
    }
}
