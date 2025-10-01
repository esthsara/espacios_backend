package com.espaciosdeportivos.validation;


import com.espaciosdeportivos.dto.MacrodistritoDTO;
import org.springframework.stereotype.Component;


@Component
public class MacrodistritoValidator {
    //aqui ponemos el estado permitido
    private static final int NOMBRE_MAX_LENGTH = 200;
    private static final int DESCRIPCION_MAX_LENGTH = 600;

    public void validarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new BusinessException("El nombre del macrodistrito es obligatorio.");
        }
        if (nombre.length() > NOMBRE_MAX_LENGTH) {
            throw new BusinessException("El nombre no puede exceder 200 caracteres.");
        }
    }

    public void validarDescripcion(String descripcion) {
        if (descripcion != null && descripcion.length() > DESCRIPCION_MAX_LENGTH) {
            throw new BusinessException("La descripción no puede exceder 400 caracteres.");
        }
    }

    public void validarEstado(Boolean estado) {
        if (estado == null) {
            throw new BusinessException("El estado del macrodistrito no puede ser nulo.");
        }
    }

    public void validarMacrodistrito(MacrodistritoDTO dto) {
        validarNombre(dto.getNombre());
        validarDescripcion(dto.getDescripcion());
        validarEstado(dto.getEstado());
    }

    public static class BusinessException extends RuntimeException {
        public BusinessException(String message) {
            super(message);
        }
    }
}