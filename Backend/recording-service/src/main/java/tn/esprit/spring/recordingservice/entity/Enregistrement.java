package tn.esprit.spring.recordingservice.entity;

import jakarta.persistence.*;
import lombok.*;
import tn.esprit.spring.recordingservice.entity.enums.StatutEnregistrement;

import java.time.LocalDateTime;


@Entity
    @Table(name = "enregistrement")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class Enregistrement {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false)
        private Long utilisateurId;

        @Column(nullable = false)
        private Long sujetId;

        @Column(nullable = false, length = 255)
        private String nomFichier;

        @Column(nullable = false, length = 500)
        private String cheminStockage;

        @Column
        private Integer dureeSecondes;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false, length = 30)
        private StatutEnregistrement statut;

        @Column(nullable = false, updatable = false)
        private LocalDateTime dateCreation;

        @PrePersist
        protected void onCreate() {
            if (dateCreation == null) {
                dateCreation = LocalDateTime.now();
            }

            if (statut == null) {
                statut = StatutEnregistrement.UPLOADED;
            }
        }
    }

