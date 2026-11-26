package com.example.gateway;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.context.annotation.Configuration;

// Partie 3 - Q3 : applique l'algorithme Random UNIQUEMENT a profile-service.
// Les autres services gardent l'algorithme par defaut (Round Robin).
@Configuration
@LoadBalancerClient(name = "profile-service", configuration = ProfileLoadBalancerConfig.class)
public class LoadBalancerConfig {
}
