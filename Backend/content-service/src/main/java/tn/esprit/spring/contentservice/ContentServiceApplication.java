package tn.esprit.spring.contentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * MS2 - content-service : catalogue pedagogique de Vocalibre
 * (parcours, banque de sujets d'oral, recommandations d'exercices).
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class ContentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContentServiceApplication.class, args);
    }
}
