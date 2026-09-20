package com.talentsync.profileservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Profile Service Application
 * 
 * Handles candidate and recruiter profile management.
 * 
 * Port: 8085
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ProfileServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProfileServiceApplication.class, args);
    }

}
