package com.talentsync.jobservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Job Service Application
 * 
 * Handles job posting management and job listing.
 * 
 * Port: 8082
 */
@SpringBootApplication
@EnableDiscoveryClient
public class JobServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobServiceApplication.class, args);
    }

}
