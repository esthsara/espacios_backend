package com.espaciosdeportivos.validation;

import com.espaciosdeportivos.dto.EquipamientoDTO;
import org.springframework.stereotype.Component;

@Component
public class EquipamientoValidator {

    public void validarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new BusinessException("El nombre del equipamiento es obligatorio.");
        }
        if (nombre.length() > 100) {
            throw new BusinessException("El nombre no puede exceder 100 caracteres.");
        }
    }

    public void validarTipo(String tipo) {
        if (tipo == null || tipo.isBlank()) {
            throw new BusinessException("El tipo de equipamiento es obligatorio.");
        }
        if (tipo.length() > 100) {
            throw new BusinessException("El tipo no puede exceder 100 caracteres.");
        }
    }

    public void validarDescripcion(String descripcion) {
        if (descripcion != null && descripcion.length() > 400) {
            throw new BusinessException("La descripción no puede exceder 400 caracteres.");
        }
    }

    public void validarEstadoTexto(String estado) {
        if (estado == null || estado.isBlank()) {
            throw new BusinessException("El estado del equipamiento es obligatorio.");
        }
        if (estado.length() > 100) {
            throw new BusinessException("El estado no puede exceder 100 caracteres.");
        }
        // Opcional: validar valores permitidos (DISPONIBLE, EN_USO, MANTENIMIENTO, etc.)
    }

    public void validarEstado(Boolean estado) {
        if (estado == null) {
            throw new BusinessException("El estado booleano es obligatorio.");
        }
    }

    public void validarEquipamiento(EquipamientoDTO dto) {
        validarNombre(dto.getNombreEquipamiento());
        validarTipo(dto.getTipoEquipamiento());
        validarDescripcion(dto.getDescripcion());
        validarEstado(dto.getEstado());
        validarEstado(dto.getEstado());
        if (dto.getUrlImagen() != null && dto.getUrlImagen().length() > 800) {
            throw new BusinessException("La URL de imagen no puede exceder 800 caracteres.");
        }
    }

    public static class BusinessException extends RuntimeException {
        public BusinessException(String message) { super(message); }
    }
}
