package com.espaciosdeportivos.dto;

import lombok.Data;
//import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ImagenDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idImagen;

    private Long idImagenRelacion;

    @NotBlank(message = "El nombre de archivo no puede estar vacío")
    @Size(max = 255, message = "El nombre de archivo no puede superar los 255 caracteres")
    private String nombreArchivo;

    // IMPORTANTE: Agregar ruta de almacenamiento (necesaria para el servicio)
    @NotBlank(message = "La ruta de almacenamiento no puede estar vacía")
    @Size(max = 500, message = "La ruta de almacenamiento no puede superar los 500 caracteres")
    private String rutaAlmacenamiento;

    @NotBlank(message = "La URL de acceso no puede estar vacía")
    private String urlAcceso;

    @NotBlank(message = "El tipo MIME no puede estar vacío")
    @Size(max = 100, message = "El tipo MIME no puede superar los 100 caracteres")
    private String tipoMime;

    @NotNull(message = "El tamaño en bytes no puede ser nulo")
    @Positive(message = "El tamaño en bytes debe ser positivo")
    private Long tamanioBytes;

    @NotNull(message = "El orden no puede ser nulo")
    @PositiveOrZero(message = "El orden debe ser positivo o cero")
    private Integer orden = 0;

    @NotNull(message = "El estado no puede ser nulo")
    private Boolean estado = true;

    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaActualizacion;


    // Constructores para flexibilidad
    public ImagenDTO() {
        this.estado = true;
        this.orden = 0;
    }

    public ImagenDTO(String nombreArchivo, String rutaAlmacenamiento, String tipoMime, Long tamanioBytes) {
        this.nombreArchivo = nombreArchivo;
        this.rutaAlmacenamiento = rutaAlmacenamiento;
        this.tipoMime = tipoMime;
        this.tamanioBytes = tamanioBytes;
        this.estado = true;
        this.orden = 0;
    }

    // Método helper para obtener extensión
    public String obtenerExtension() {
        if (nombreArchivo == null || !nombreArchivo.contains(".")) {
            return "";
        }
        return nombreArchivo.substring(nombreArchivo.lastIndexOf(".") + 1).toLowerCase();
    }

    // Método helper para verificar si es imagen
    public boolean esImagenValida() {
        String extension = obtenerExtension();
        return List.of("jpg", "jpeg", "png", "gif", "webp").contains(extension);
    }

    // Método helper para formatear tamaño
    public String obtenerTamanioFormateado() {
        if (tamanioBytes == null) return "0 B";
        
        if (tamanioBytes < 1024) {
            return tamanioBytes + " B";
        } else if (tamanioBytes < 1024 * 1024) {
            return String.format("%.1f KB", tamanioBytes / 1024.0);
        } else {
            return String.format("%.1f MB", tamanioBytes / (1024.0 * 1024.0));
        }
    }
    //
    /*COMPLETAR */
    public void setProblema(String string) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setProblema'");
    }
}