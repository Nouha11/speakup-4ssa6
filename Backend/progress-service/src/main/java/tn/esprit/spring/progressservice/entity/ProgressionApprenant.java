package tn.esprit.spring.progressservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.esprit.spring.progressservice.enums.Langue;
import tn.esprit.spring.progressservice.enums.Niveau;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProgressionApprenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String apprenantId;          // keycloakId (claim sub du JWT)

    @Enumerated(EnumType.STRING)
    private Langue langue;

    @Enumerated(EnumType.STRING)
    private Niveau niveauCible;

    @Enumerated(EnumType.STRING)
    private Niveau niveauEstime;         // copie en lecture seule (fournie par certification-service)

    private LocalDateTime derniereMiseAJour;

    @OneToMany(mappedBy = "progression", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IndicateurCritere> indicateurs = new ArrayList<>();
}
