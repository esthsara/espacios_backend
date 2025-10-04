/*package com.espaciosdeportivos.dto;

import java.io.Serializable;
import java.util.List;
import lombok.*;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DisciplinaDTO implements Serializable {

    private Long idDisciplina;

    @NotBlank(message = "El nombre de la disciplina es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String nombre;

    @Size(max = 255, message = "La descripción no puede exceder 255 caracteres")
    private String descripcion;

    @NotNull(message = "Debe indicar el estado de la disciplina")
    private Boolean estado;

    // Lista de imágenes asociadas
    private List<ImagenDTO> imagenes;
}
*/
// dto/DisciplinaDTO.java
package com.espaciosdeportivos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class DisciplinaDTO {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
    private String nombre;

    private String descripcion;

    private Boolean estado;

    private List<ImagenDTO> imagenes;
}
