package com.espaciosdeportivos.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdminConfig {
    
    @Value("${app.admin.registration.password:passwordadmin}")
    private String adminRegistrationPassword;

    public String getAdminRegistrationPassword() {
        return adminRegistrationPassword;
    }
}