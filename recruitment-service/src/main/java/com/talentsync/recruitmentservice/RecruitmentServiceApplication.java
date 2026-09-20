package com.talentsync.recruitmentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Recruitment Service Application
 * 
 * Handles interview scheduling, candidate shortlisting, and hiring decisions.
 * 
 * Port: 8084
 */
@SpringBootApplication
@EnableDiscoveryClient
public class RecruitmentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RecruitmentServiceApplication.class, args);
    }

}
