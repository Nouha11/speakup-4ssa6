package tn.esprit.spring.progressservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.esprit.spring.progressservice.enums.TypeAlerte;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlerteFormateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String apprenantId;          // keycloakId — référence vers Keycloak

    private Long groupeId;               // référence vers profile-service (pas de FK inter-services)

    @Enumerated(EnumType.STRING)
    private TypeAlerte type;

    private String message;

    private LocalDateTime dateCreation;

    private boolean traitee;

    @ManyToOne
    @JoinColumn(name = "progression_id") // générée à partir d'une ProgressionApprenant
    private ProgressionApprenant progression;
}
