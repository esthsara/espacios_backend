package com.espaciosdeportivos.validation;

import com.espaciosdeportivos.dto.ComentarioDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Component
public class ComentarioValidator {

    private static final Pattern CONTENIDO_PATTERN = Pattern.compile("^[\\p{L}\\p{N}\\s.,!?¡¿-]{5,255}$");

    public void validarContenido(String contenido) {
        if (contenido == null || !CONTENIDO_PATTERN.matcher(contenido).matches()) {
            throw new BusinessException("Contenido no válido. Debe tener entre 5 y 255 caracteres y solo símbolos permitidos.");
        }
    }

    public void validarCalificacion(Integer calificacion) {
        if (calificacion == null || calificacion < 1 || calificacion > 5) {
            throw new BusinessException("Calificación fuera de rango. Debe estar entre 1 y 5.");
        }
    }

    /*public void validarFecha(LocalDate fecha) {
        if (fecha == null || fecha.isAfter(LocalDateTime.now())) {
            throw new BusinessException("Fecha no válida. No puede ser futura ni nula.");
        }
    }*/

    public void validarEstado(Boolean estado) {
        if (estado == null) {
            throw new BusinessException("El estado del comentario no puede ser nulo.");
        }
    }

    public void validarComentario(ComentarioDTO comentarioDTO) {
        validarContenido(comentarioDTO.getContenido());
        validarCalificacion(comentarioDTO.getCalificacion());
        //validarFecha(comentarioDTO.getFecha());
        validarEstado(comentarioDTO.getEstado());
    }

    public static class BusinessException extends RuntimeException {
        public BusinessException(String message) {
            super(message);
        }
    }
}