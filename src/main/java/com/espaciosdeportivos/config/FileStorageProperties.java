package com.espaciosdeportivos.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Data;

@Component
@ConfigurationProperties(prefix = "file")
@Data
public class FileStorageProperties {
    private String uploadDir = "uploads";
    private String baseImgDir = "img"; // Sin espacios
    private long maxFileSize = 5242880;
    private String[] allowedExtensions = {"jpg", "jpeg", "png", "gif"};
}
//