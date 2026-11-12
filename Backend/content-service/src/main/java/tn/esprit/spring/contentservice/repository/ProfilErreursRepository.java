package tn.esprit.spring.contentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.spring.contentservice.entity.ProfilErreurs;

import java.util.List;

public interface ProfilErreursRepository extends JpaRepository<ProfilErreurs, Long> {

    List<ProfilErreurs> findByApprenantIdOrderByOccurrencesDesc(String apprenantId);
}
