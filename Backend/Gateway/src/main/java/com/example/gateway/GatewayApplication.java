package com.example.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                // Partie 3 - Methode 1 : une route dynamique (lb:// = via Eureka) par microservice
                .route("profile-service", r -> r.path("/api/profils/**", "/api/groupes/**")
                        .uri("lb://profile-service"))
                .route("content-service", r -> r.path("/api/parcours/**", "/api/sujets/**", "/api/recommandations/**")
                        .uri("lb://content-service"))
                .route("recording-service", r -> r.path("/api/enregistrements/**", "/api/consentements/**", "/api/outbox/**")
                        .uri("lb://recording-service"))
                .route("exam-service", r -> r.path("/api/sessions/**")
                        .uri("lb://exam-service"))
                .route("certification-service", r -> r.path("/api/attestations/**", "/api/niveaux/**", "/api/regles/**")
                        .uri("lb://certification-service"))
                .route("progress-service", r -> r.path("/api/progression/**", "/api/alertes/**",
                                "/api/statistiques/**", "/api/indicateurs/**")
                        .uri("lb://progress-service"))

                // Partie 4 : la doc OpenAPI de chaque microservice, exposee pour le Swagger unifie
                .route("profile-service-docs", r -> r.path("/profile-service/v3/api-docs")
                        .filters(f -> f.rewritePath("/profile-service/(?<path>.*)", "/${path}"))
                        .uri("lb://profile-service"))
                .route("content-service-docs", r -> r.path("/content-service/v3/api-docs")
                        .filters(f -> f.rewritePath("/content-service/(?<path>.*)", "/${path}"))
                        .uri("lb://content-service"))
                .route("recording-service-docs", r -> r.path("/recording-service/v3/api-docs")
                        .filters(f -> f.rewritePath("/recording-service/(?<path>.*)", "/${path}"))
                        .uri("lb://recording-service"))
                .route("exam-service-docs", r -> r.path("/exam-service/v3/api-docs")
                        .filters(f -> f.rewritePath("/exam-service/(?<path>.*)", "/${path}"))
                        .uri("lb://exam-service"))
                // certification-service publie sa doc sur /api-docs (springdoc.api-docs.path)
                .route("certification-service-docs", r -> r.path("/certification-service/v3/api-docs")
                        .filters(f -> f.rewritePath("/certification-service/v3/api-docs", "/api-docs"))
                        .uri("lb://certification-service"))
                .route("progress-service-docs", r -> r.path("/progress-service/v3/api-docs")
                        .filters(f -> f.rewritePath("/progress-service/(?<path>.*)", "/${path}"))
                        .uri("lb://progress-service"))
                .build();
    }
}
