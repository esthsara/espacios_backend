package com.espaciosdeportivos.service.impl;

import com.espaciosdeportivos.config.FileStorageProperties;
import com.espaciosdeportivos.service.FileStorageService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {
    
    private final FileStorageProperties fileStorageProperties;
    private Path baseStorageLocation;
    
    @PostConstruct
    public void init() {
        this.baseStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();
        
        try {
            Files.createDirectories(this.baseStorageLocation);
            System.out.println("✅ Directorio base creado en: " + this.baseStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("No se pudo crear el directorio base para almacenar archivos", ex);
        }
    }
    
    @Override
    public String guardarArchivo(MultipartFile archivo, String subcarpeta) {
        String nombreOriginal = StringUtils.cleanPath(archivo.getOriginalFilename());
        
        try {
            if (nombreOriginal.contains("..")) {
                throw new RuntimeException("Nombre de archivo inválido: " + nombreOriginal);
            }
            
            // Limpiar y normalizar la subcarpeta
            String subcarpetaLimpia = subcarpeta.replaceAll("\\s+", "").toLowerCase();
            System.out.println("📁 Subcarpeta limpia: " + subcarpetaLimpia);
            
            // Crear subcarpeta específica
            Path subcarpetaPath = this.baseStorageLocation.resolve(subcarpetaLimpia);
            Files.createDirectories(subcarpetaPath);
            System.out.println("✅ Subcarpeta creada en: " + subcarpetaPath);
            
            // Generar nombre único
            String extension = "";
            if (nombreOriginal.contains(".")) {
                extension = nombreOriginal.substring(nombreOriginal.lastIndexOf("."));
            }
            
            String nombreArchivo = UUID.randomUUID().toString() + extension;
            Path targetLocation = subcarpetaPath.resolve(nombreArchivo);
            Files.copy(archivo.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            
            System.out.println("💾 Archivo guardado en: " + targetLocation);
            
            // Retornar ruta relativa: subcarpeta/nombreArchivo
            return subcarpetaLimpia + "/" + nombreArchivo;
            
        } catch (IOException ex) {
            throw new RuntimeException("No se pudo almacenar el archivo " + nombreOriginal + ": " + ex.getMessage(), ex);
        }
    }
    
    @Override
    public Resource cargarArchivo(String rutaArchivo) {
        try {
            Path filePath = this.baseStorageLocation.resolve(rutaArchivo).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists()) {
                System.out.println("📥 Archivo cargado: " + rutaArchivo);
                return resource;
            } else {
                throw new RuntimeException("Archivo no encontrado: " + rutaArchivo);
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException("Archivo no encontrado: " + rutaArchivo, ex);
        }
    }
    
    @Override
    public void eliminarArchivo(String rutaArchivo) {
        try {
            Path filePath = this.baseStorageLocation.resolve(rutaArchivo).normalize();
            boolean eliminado = Files.deleteIfExists(filePath);
            
            if (eliminado) {
                System.out.println("🗑️ Archivo eliminado: " + rutaArchivo);
                
                // Opcional: eliminar subcarpeta si está vacía
                eliminarSubcarpetaSiVacia(filePath.getParent());
            } else {
                System.out.println("⚠️ Archivo no existía: " + rutaArchivo);
            }
            
        } catch (IOException ex) {
            throw new RuntimeException("No se pudo eliminar el archivo: " + rutaArchivo, ex);
        }
    }
    
    @Override
    public String obtenerSubcarpetaPorTipoEntidad(String entidadTipo) {
        // Limpiar espacios y convertir a minúsculas
        String entidadLimpia = entidadTipo.replaceAll("\\s+", "").toLowerCase();
        String subcarpeta = fileStorageProperties.getBaseImgDir() + "/" + entidadLimpia;
        System.out.println("🔧 Subcarpeta generada para '" + entidadTipo + "': '" + subcarpeta + "'");
        return subcarpeta;
    }
    
    // ========== MÉTODOS ADICIONALES ==========
    
    @Override
    public boolean existeArchivo(String rutaArchivo) {
        Path filePath = this.baseStorageLocation.resolve(rutaArchivo).normalize();
        boolean existe = Files.exists(filePath);
        System.out.println("🔍 Archivo " + rutaArchivo + " existe: " + existe);
        return existe;
    }
    
    @Override
    public long obtenerTamanioArchivo(String rutaArchivo) {
        try {
            Path filePath = this.baseStorageLocation.resolve(rutaArchivo).normalize();
            long tamanio = Files.size(filePath);
            System.out.println("📏 Tamaño de " + rutaArchivo + ": " + tamanio + " bytes");
            return tamanio;
        } catch (IOException e) {
            throw new RuntimeException("Error obteniendo tamaño del archivo: " + rutaArchivo, e);
        }
    }
    
    @Override
    public String obtenerTipoMimeArchivo(String rutaArchivo) {
        try {
            Path filePath = this.baseStorageLocation.resolve(rutaArchivo).normalize();
            return Files.probeContentType(filePath);
        } catch (IOException e) {
            System.err.println("Error obteniendo tipo MIME: " + e.getMessage());
            return "application/octet-stream";
        }
    }
    
    @Override
    public boolean validarRutaArchivo(String rutaArchivo) {
        if (rutaArchivo == null || rutaArchivo.trim().isEmpty()) {
            return false;
        }
        
        Path filePath = this.baseStorageLocation.resolve(rutaArchivo).normalize();
        
        // Verificar que la ruta esté dentro del directorio base por seguridad
        boolean esValida = filePath.startsWith(this.baseStorageLocation) && 
               !rutaArchivo.contains("..") && 
               Files.exists(filePath);
        
        System.out.println("🔒 Ruta " + rutaArchivo + " válida: " + esValida);
        return esValida;
    }
    
    @Override
    public void limpiarArchivosTemporales() {
        try {
            // Ejemplo: eliminar archivos temporales antiguos
            System.out.println("🧹 Limpieza de archivos temporales iniciada");
            // Implementación específica según necesidades
        } catch (Exception e) {
            System.err.println("Error en limpieza de temporales: " + e.getMessage());
        }
    }
    
    @Override
    public List<String> listarArchivosEnDirectorio(String directorio) {
        try {
            Path dirPath = this.baseStorageLocation.resolve(directorio).normalize();
            if (Files.exists(dirPath) && Files.isDirectory(dirPath)) {
                return Files.list(dirPath)
                    .map(path -> path.getFileName().toString())
                    .collect(Collectors.toList());
            }
            return new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Error listando directorio: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    @Override
    public long calcularUsoDiscoDirectorio(String directorio) {
        try {
            Path dirPath = this.baseStorageLocation.resolve(directorio).normalize();
            if (Files.exists(dirPath) && Files.isDirectory(dirPath)) {
                return Files.walk(dirPath)
                    .filter(Files::isRegularFile)
                    .mapToLong(path -> {
                        try {
                            return Files.size(path);
                        } catch (IOException e) {
                            return 0L;
                        }
                    })
                    .sum();
            }
            return 0L;
        } catch (IOException e) {
            System.err.println("Error calculando uso de disco: " + e.getMessage());
            return 0L;
        }
    }
    
    @Override
    public boolean copiarArchivo(String rutaOrigen, String rutaDestino) {
        try {
            Path origenPath = this.baseStorageLocation.resolve(rutaOrigen).normalize();
            Path destinoPath = this.baseStorageLocation.resolve(rutaDestino).normalize();
            
            Files.copy(origenPath, destinoPath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("📋 Archivo copiado: " + rutaOrigen + " → " + rutaDestino);
            return true;
        } catch (IOException e) {
            System.err.println("Error copiando archivo: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean moverArchivo(String rutaOrigen, String rutaDestino) {
        try {
            Path origenPath = this.baseStorageLocation.resolve(rutaOrigen).normalize();
            Path destinoPath = this.baseStorageLocation.resolve(rutaDestino).normalize();
            
            Files.move(origenPath, destinoPath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("🚚 Archivo movido: " + rutaOrigen + " → " + rutaDestino);
            return true;
        } catch (IOException e) {
            System.err.println("Error moviendo archivo: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public String generarRutaUnica(String nombreOriginal, String subcarpeta) {
        String extension = "";
        if (nombreOriginal.contains(".")) {
            extension = nombreOriginal.substring(nombreOriginal.lastIndexOf("."));
        }
        
        String nombreArchivo = UUID.randomUUID().toString() + extension;
        return subcarpeta + "/" + nombreArchivo;
    }
    
    @Override
    public boolean validarTipoArchivo(MultipartFile archivo, String[] tiposPermitidos) {
        String extension = obtenerExtension(archivo.getOriginalFilename());
        return Arrays.asList(tiposPermitidos).contains(extension.toLowerCase());
    }

    @Override
    public boolean validarTamanioArchivo(MultipartFile archivo, long tamanioMaximo) {
        return archivo.getSize() <= tamanioMaximo;
    }
    
    @Override
    public String obtenerExtension(String nombreArchivo) {
        if (nombreArchivo == null || !nombreArchivo.contains(".")) {
            return "";
        }
        return nombreArchivo.substring(nombreArchivo.lastIndexOf(".") + 1).toLowerCase();
    }
    
    @Override
    public void mostrarEstadisticasAlmacenamiento() {
        try {
            long totalSize = Files.walk(this.baseStorageLocation)
                .filter(Files::isRegularFile)
                .mapToLong(path -> {
                    try {
                        return Files.size(path);
                    } catch (IOException e) {
                        return 0L;
                    }
                })
                .sum();
            
            long fileCount = Files.walk(this.baseStorageLocation)
                .filter(Files::isRegularFile)
                .count();
            
            System.out.println("📊 Estadísticas de almacenamiento:");
            System.out.println("   - Total archivos: " + fileCount);
            System.out.println("   - Espacio utilizado: " + (totalSize / (1024 * 1024)) + " MB");
            System.out.println("   - Directorio base: " + this.baseStorageLocation);
            
        } catch (IOException e) {
            System.err.println("Error obteniendo estadísticas: " + e.getMessage());
        }
    }
    
    @Override
    public String obtenerRutaAbsoluta(String rutaRelativa) {
        Path rutaAbsoluta = this.baseStorageLocation.resolve(rutaRelativa).normalize();
        return rutaAbsoluta.toString();
    }
    
    @Override
    public boolean crearDirectorioSiNoExiste(String directorio) {
        try {
            Path dirPath = this.baseStorageLocation.resolve(directorio).normalize();
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
                System.out.println("📁 Directorio creado: " + dirPath);
                return true;
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error creando directorio: " + e.getMessage());
            return false;
        }
    }
    
    // ========== MÉTODOS PRIVADOS ==========
    
    private void eliminarSubcarpetaSiVacia(Path subcarpetaPath) {
        try {
            if (Files.isDirectory(subcarpetaPath) && 
                !Files.list(subcarpetaPath).findAny().isPresent()) {
                Files.delete(subcarpetaPath);
                System.out.println("🗑️ Subcarpeta vacía eliminada: " + subcarpetaPath);
            }
        } catch (IOException e) {
            // No es crítico si no se puede eliminar la subcarpeta
            System.err.println("⚠️ No se pudo eliminar subcarpeta vacía: " + e.getMessage());
        }
    }
}