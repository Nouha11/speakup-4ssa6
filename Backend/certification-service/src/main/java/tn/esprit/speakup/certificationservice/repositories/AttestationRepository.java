package tn.esprit.speakup.certificationservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.speakup.certificationservice.entities.Attestation;
import tn.esprit.speakup.certificationservice.enums.Langue;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttestationRepository extends JpaRepository<Attestation, Long> {

    Optional<Attestation> findByCodeVerification(String codeVerification);

    List<Attestation> findByApprenantId(String apprenantId);

    List<Attestation> findByApprenantIdAndLangue(String apprenantId, Langue langue);

    boolean existsByApprenantIdAndLangue(String apprenantId, Langue langue);
}
