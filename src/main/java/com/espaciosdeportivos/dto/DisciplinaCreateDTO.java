// dto/DisciplinaCreateDTO.java
package com.espaciosdeportivos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Data
public class DisciplinaCreateDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
    private String nombre;

    private String descripcion;

    // Lista de imágenes a subir
    private List<MultipartFile> imagenes;
}
