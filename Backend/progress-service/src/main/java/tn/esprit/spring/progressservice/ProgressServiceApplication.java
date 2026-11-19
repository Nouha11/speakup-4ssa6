package tn.esprit.spring.progressservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDiscoveryClient

public class ProgressServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProgressServiceApplication.class, args);
    }

}
