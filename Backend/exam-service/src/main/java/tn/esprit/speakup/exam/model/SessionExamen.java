package tn.esprit.speakup.exam.model;

import jakarta.persistence.*;
import lombok.*;
import tn.esprit.speakup.exam.model.enums.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sessions_examen")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SessionExamen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String apprenantId; // keycloakId (claim sub du JWT)

    @Column(nullable = false)
    private Long sujetId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Langue langue;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Certification certification;

    @Column(nullable = false)
    private String niveauVise; // B1, B2, C1, C2

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutSession statut;

    private LocalDateTime debutPreparation;
    private LocalDateTime debutPassage;
    private LocalDateTime finPassage;

    @OneToMany(mappedBy = "sessionExamen", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EtapeSession> etapes = new ArrayList<>();

    @OneToOne(mappedBy = "sessionExamen", cascade = CascadeType.ALL, orphanRemoval = true)
    private NoteExamen noteExamen;
}