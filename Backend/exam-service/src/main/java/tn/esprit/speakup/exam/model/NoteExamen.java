package tn.esprit.speakup.exam.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "notes_examen")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class NoteExamen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double noteEstimee; // Sur 20
    private double prononciation;
    private double fluidite;
    private double intonation;
    private double lexique;
    private double grammaire;
    private double coherence;

    private Double noteAjusteeFormateur; // Nullable
    private String commentaireFormateur;
    private boolean validee;
    private boolean evenementPublie; // Pattern Outbox pour RabbitMQ

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    private SessionExamen sessionExamen;
}