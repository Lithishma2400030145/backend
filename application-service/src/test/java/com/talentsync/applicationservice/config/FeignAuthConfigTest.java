package com.talentsync.applicationservice.config;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.assertj.core.api.Assertions.assertThat;

class FeignAuthConfigTest {
    @AfterEach
    void clearRequestContext() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void forwardsAuthorizationHeader() {
        HttpServletRequest request = new MockHttpServletRequest();
        ((MockHttpServletRequest) request).addHeader("Authorization", "Bearer test-token");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        feign.RequestTemplate template = new feign.RequestTemplate();
        new FeignAuthConfig().bearerTokenForwardingInterceptor().apply(template);

        assertThat(template.headers().get("Authorization")).containsExactly("Bearer test-token");
    }
}