package com.talentsync.eurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Eureka Server - Central Service Registry and Discovery
 * 
 * This server acts as the service registry where all microservices register themselves.
 * Other services can query this server to discover the location of services they need to communicate with.
 * 
 * Access the Eureka Dashboard at: http://localhost:8761/
 * 
 * All microservices must register with Eureka by adding:
 * - Dependency: spring-cloud-starter-netflix-eureka-client
 * - @EnableEurekaClient or @EnableDiscoveryClient annotation
 * - eureka.client.service-url.defaultZone configuration
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
    }

}
