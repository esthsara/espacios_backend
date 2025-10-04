/*package com.espaciosdeportivos.config;

//import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import org.springframework.beans.factory.annotation.Value;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000") //  origen de  frontend
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true) // Permitir credenciales
                .maxAge(3600); // Tiempo de caché para la respuesta preflight
    }

    //J IMG
    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Esto expone las imágenes en http://localhost:8032/img/archivo.png
        registry.addResourceHandler("/img/**")
                .addResourceLocations("file:///" + uploadDir + "/");
    }
}*/

package com.espaciosdeportivos.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // Ruta base para subir y servir imágenes
    @Value("${file.upload-dir}")
    private String uploadDir;

    // Configuración CORS para el frontend
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000") // Cambia si tu frontend está en otra URL
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    // Servir imágenes desde la carpeta física
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Convierte la ruta a formato de archivo y asegura que las barras sean correctas
        String resourceLocation = "file:///" + uploadDir.replace("\\", "/") + "/";
        registry.addResourceHandler("/img/**")
                .addResourceLocations(resourceLocation);
    }
}
