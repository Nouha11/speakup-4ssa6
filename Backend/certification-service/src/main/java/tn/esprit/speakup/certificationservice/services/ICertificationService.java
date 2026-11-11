package tn.esprit.speakup.certificationservice.services;

import tn.esprit.speakup.certificationservice.dto.AttestationRequest;
import tn.esprit.speakup.certificationservice.dto.AttestationResponse;
import tn.esprit.speakup.certificationservice.dto.NiveauEstimeResponse;
import tn.esprit.speakup.certificationservice.enums.Langue;
import tn.esprit.speakup.certificationservice.enums.NiveauCECRL;

import java.util.List;

public interface ICertificationService {

    // ── Attestations ──────────────────────────────────────────────────────────

    /** Vérifie l'éligibilité et émet une attestation (PDF + QR code simulés). */
    AttestationResponse emettreAttestation(AttestationRequest request);

    /** Récupère toutes les attestations d'un apprenant. */
    List<AttestationResponse> getAttestationsByApprenant(String apprenantId);

    /** Vérifie la validité d'une attestation via son code QR. */
    AttestationResponse verifierAttestation(String codeVerification);

    /** Révoque une attestation (statut → REVOQUEE). */
    AttestationResponse revoquerAttestation(Long id);

    // ── Niveau estimé ─────────────────────────────────────────────────────────

    /** Retourne le dernier niveau estimé pour un apprenant + langue. */
    NiveauEstimeResponse getNiveauEstime(String apprenantId, Langue langue);

    /** Retourne tous les niveaux estimés d'un apprenant (toutes langues). */
    List<NiveauEstimeResponse> getAllNiveauxEstimes(String apprenantId);

    /**
     * Appelé par le Listener RabbitMQ (exam.graded).
     * Met à jour ou crée le NiveauEstime de l'apprenant.
     */
    void mettreAJourNiveauEstime(String apprenantId, Langue langue,
                                  NiveauCECRL niveau, Long sessionId);
}
