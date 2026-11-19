package tn.esprit.spring.progressservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.spring.progressservice.entity.ProgressionApprenant;
import tn.esprit.spring.progressservice.enums.Langue;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProgressionApprenantRepository extends JpaRepository<ProgressionApprenant, Long> {

    Optional<ProgressionApprenant> findByApprenantIdAndLangue(String apprenantId, Langue langue);

    List<ProgressionApprenant> findByApprenantId(String apprenantId);

    void deleteByApprenantId(String apprenantId);
}
