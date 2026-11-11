package tn.esprit.spring.progressservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI progressServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Progress Service API")
                        .description("MS6 – Restitue la progression de l'apprenant : " +
                                "tableaux de bord, courbes par critère et alertes formateur.")
                        .version("1.0.0"));
    }
}
