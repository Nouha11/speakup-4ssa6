package tn.esprit.speakup.certificationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Point d'entrée de MS5 – certification-service.
 *
 * <p>Démarrage : ./mvnw spring-boot:run
 * <ul>
 *   <li>Swagger  → http://localhost:8085/swagger-ui.html</li>
 *   <li>H2 Console → http://localhost:8085/h2-console  (JDBC: jdbc:h2:mem:certdb)</li>
 *   <li>Health   → http://localhost:8085/actuator/health</li>
 * </ul>
 *
 * <p>Dépendances à ajouter en S5-S7 :
 *   Eureka Client, Config Client, OpenFeign, Resilience4j, Spring AMQP (RabbitMQ).
 */
@SpringBootApplication
public class CertificationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CertificationServiceApplication.class, args);
    }
}
