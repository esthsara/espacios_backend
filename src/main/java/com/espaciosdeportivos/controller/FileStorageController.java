package com.espaciosdeportivos.controller;

import com.espaciosdeportivos.service.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/archivos")
@RequiredArgsConstructor
@Tag(name = "Gestión de Archivos", description = "API para operaciones directas con archivos de almacenamiento")
public class FileStorageController {

    private final FileStorageService fileStorageService;

    @Operation(summary = "Verificar existencia de archivo", description = "Verifica si un archivo existe en el sistema de almacenamiento")
    @GetMapping("/existe/{rutaArchivo:.+}")
    public ResponseEntity<Boolean> existeArchivo(
            @Parameter(description = "Ruta completa del archivo") 
            @PathVariable String rutaArchivo) {
        
        log.info("Verificando existencia de archivo: {}", rutaArchivo);
        
        try {
            boolean existe = fileStorageService.existeArchivo(rutaArchivo);
            return ResponseEntity.ok(existe);
        } catch (Exception e) {
            log.error("Error verificando archivo: {}", e.getMessage());
            return ResponseEntity.ok(false);
        }
    }

    @Operation(summary = "Obtener tamaño de archivo", description = "Obtiene el tamaño en bytes de un archivo específico")
    @GetMapping("/tamanio/{rutaArchivo:.+}")
    public ResponseEntity<Long> obtenerTamanioArchivo(
            @Parameter(description = "Ruta completa del archivo") 
            @PathVariable String rutaArchivo) {
        
        log.info("Obteniendo tamaño de archivo: {}", rutaArchivo);
        
        try {
            long tamanio = fileStorageService.obtenerTamanioArchivo(rutaArchivo);
            return ResponseEntity.ok(tamanio);
        } catch (Exception e) {
            log.error("Error obteniendo tamaño: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Validar ruta de archivo", description = "Valida si una ruta de archivo es segura y válida")
    @GetMapping("/validar-ruta/{rutaArchivo:.+}")
    public ResponseEntity<Boolean> validarRutaArchivo(
            @Parameter(description = "Ruta a validar") 
            @PathVariable String rutaArchivo) {
        
        log.info("Validando ruta de archivo: {}", rutaArchivo);
        
        try {
            boolean esValida = fileStorageService.validarRutaArchivo(rutaArchivo);
            return ResponseEntity.ok(esValida);
        } catch (Exception e) {
            log.error("Error validando ruta: {}", e.getMessage());
            return ResponseEntity.ok(false);
        }
    }

    @Operation(summary = "Obtener extensión de archivo", description = "Extrae la extensión de un nombre de archivo")
    @GetMapping("/extension")
    public ResponseEntity<String> obtenerExtension(
            @Parameter(description = "Nombre del archivo") 
            @RequestParam String nombreArchivo) {
        
        log.info("Obteniendo extensión de: {}", nombreArchivo);
        
        try {
            String extension = fileStorageService.obtenerExtension(nombreArchivo);
            return ResponseEntity.ok(extension);
        } catch (Exception e) {
            log.error("Error obteniendo extensión: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Descargar archivo directo", description = "Descarga un archivo directamente por su ruta")
    @GetMapping("/descargar/{rutaArchivo:.+}")
    public ResponseEntity<Resource> descargarArchivoDirecto(
            @Parameter(description = "Ruta completa del archivo") 
            @PathVariable String rutaArchivo) {
        
        log.info("Descargando archivo directo: {}", rutaArchivo);
        
        try {
            Resource resource = fileStorageService.cargarArchivo(rutaArchivo);
            
            String contentType = "application/octet-stream";
            String filename = rutaArchivo.substring(rutaArchivo.lastIndexOf("/") + 1);
            
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .body(resource);
        } catch (Exception e) {
            log.error("Error descargando archivo: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}