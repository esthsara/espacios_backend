package com.espaciosdeportivos.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class sepracticaDTO implements Serializable {
    
    @NotNull(message = "El id_cancha es obligatorio")
    @Positive(message = "El id_cancha debe ser un valor positivo")
    private Long idCancha;

    @NotNull(message = "El id_disciplina es obligatorio")
    @Positive(message = "El id_disciplina debe ser un valor positivo")
    private Long idDisciplina;

    private String nivelDificultad; // "PRINCIPIANTE", "INTERMEDIO", "AVANZADO"
    
    private String recomendaciones;
}