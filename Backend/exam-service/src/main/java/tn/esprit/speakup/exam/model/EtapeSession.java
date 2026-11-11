package tn.esprit.speakup.exam.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "etapes_session")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class EtapeSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int ordre;
    private String questionTexte;
    private Long enregistrementId;

    @Column(unique = true)
    private String analyseId; // Unique pour garantir l'idempotence RabbitMQ

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    private SessionExamen sessionExamen;
}