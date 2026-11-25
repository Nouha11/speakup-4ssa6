package tn.esprit.spring.recordingservice.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "consentement_audio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsentementAudio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "enregistrement_id",
            nullable = false,
            unique = true
    )
    private Enregistrement enregistrement;

    @Column(nullable = false)
    private Boolean accepte;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateConsentement;

    @Column(nullable = false, length = 50)
    private String versionPolitique;

    @PrePersist
    protected void onCreate() {
        if (dateConsentement == null) {
            dateConsentement = LocalDateTime.now();
        }
    }
}
