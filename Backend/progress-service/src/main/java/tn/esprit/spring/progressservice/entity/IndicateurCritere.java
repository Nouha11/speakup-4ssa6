package tn.esprit.spring.progressservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.esprit.spring.progressservice.enums.Critere;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IndicateurCritere {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Critere critere;

    private Double valeur;

    private LocalDateTime dateMesure;

    private String sourceId;             // id de l'analyse source — utilisé pour l'idempotence

    @ManyToOne(optional = false)
    @JoinColumn(name = "progression_id")
    private ProgressionApprenant progression;
}
