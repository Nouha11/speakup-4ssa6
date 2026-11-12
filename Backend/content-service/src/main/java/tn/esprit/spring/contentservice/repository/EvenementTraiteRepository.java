package tn.esprit.spring.contentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.spring.contentservice.entity.EvenementTraite;

public interface EvenementTraiteRepository extends JpaRepository<EvenementTraite, String> {
}
