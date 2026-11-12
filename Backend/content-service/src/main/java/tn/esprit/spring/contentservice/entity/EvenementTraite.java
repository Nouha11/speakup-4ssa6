package tn.esprit.spring.contentservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Table technique d'idempotence : un evenement RabbitMQ dont l'eventId est deja ici est ignore (S7).
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class EvenementTraite {

    @Id
    @Column(length = 64)
    private String eventId;

    @Column(length = 40)
    private String type;

    private LocalDateTime dateTraitement;
}
