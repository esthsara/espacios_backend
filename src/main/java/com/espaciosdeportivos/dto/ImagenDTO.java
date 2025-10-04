/*package com.espaciosdeportivos.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.*;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImagenDTO implements Serializable {

    private Long idImagen;

    @NotBlank(message = "La URL de la imagen es obligatoria")
    @Size(max = 255, message = "La URL no debe superar los 255 caracteres")
    private String url;

    private LocalDateTime fechaCreacion;

    @NotNull(message = "Debe indicar si la imagen está activa")
    private Boolean activa;
}
*/
// dto/ImagenDTO.java
package com.espaciosdeportivos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImagenDTO {

    private Long id;

    @NotBlank(message = "La URL no puede estar vacía")
    private String url;

    @NotBlank(message = "El nombre de archivo es obligatorio")
    private String nombreArchivo;

    private Boolean estado = true; // true = activa, false = eliminada (borrado lógico)
}
