package tn.esprit.spring.recordingservice.entity;

import jakarta.persistence.*;
import lombok.*;
import tn.esprit.spring.recordingservice.entity.enums.StatutEvenement;

import java.time.LocalDateTime;

@Entity
@Table(name = "evenement_outbox")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvenementOutbox {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long aggregateId;

    @Column(nullable = false, length = 100)
    private String typeEvenement;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String payload;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatutEvenement statut;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    private LocalDateTime datePublication;

    @PrePersist
    protected void onCreate() {
        if (dateCreation == null) {
            dateCreation = LocalDateTime.now();
        }

        if (statut == null) {
            statut = StatutEvenement.PENDING;
        }
    }
}
