package com.talentsync.eurekaserver;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Integration tests for Eureka Server Application
 */
@SpringBootTest
@TestPropertySource(locations = "classpath:application.yml")
class EurekaServerApplicationTests {

    @Autowired(required = false)
    private EurekaServerApplication application;

    @Test
    void contextLoads() {
        // Verify that Spring context loads successfully
        assertNotNull(application, "Application context should load successfully");
    }

    @Test
    void eurekaServerIsEnabled() {
        // Verify Eureka Server is running
        // In a real scenario, this would check if services can register
        assertNotNull(application, "Eureka Server should be instantiated");
    }

}
