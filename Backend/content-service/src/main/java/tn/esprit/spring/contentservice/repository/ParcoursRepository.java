package tn.esprit.spring.contentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.spring.contentservice.entity.Parcours;

public interface ParcoursRepository extends JpaRepository<Parcours, Long> {
}
