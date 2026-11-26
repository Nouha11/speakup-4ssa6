package tn.esprit.spring.recordingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class RecordingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecordingServiceApplication.class, args);
	}
}