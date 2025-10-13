package com.espaciosdeportivos.validation;


import com.espaciosdeportivos.dto.DisciplinaDTO;
import org.springframework.stereotype.Component;
//J
@Component
public class DisciplinaValidator {

    public void validarDisciplina(DisciplinaDTO dto) {
        validarNombre(dto.getNombre());
        validarDescripcion(dto.getDescripcion());
    }

    public void validarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new BusinessException("El nombre de la disciplina no puede estar vacío.");
        }

        if (!nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ\\s]+")) {
            throw new BusinessException("El nombre solo puede contener letras y espacios.");
        }

        if (nombre.length() > 100) {
            throw new BusinessException("El nombre no puede superar los 100 caracteres.");
        }
    }

    public void validarDescripcion(String descripcion) {
        if (descripcion != null && descripcion.length() > 500) {
            throw new BusinessException("La descripción no puede superar los 500 caracteres.");
        }

        if (descripcion != null && descripcion.equalsIgnoreCase("disciplina")) {
            throw new BusinessException("La descripción no puede ser genérica o igual al nombre.");
        }
    }

    public static class BusinessException extends RuntimeException {
        public BusinessException(String message) {
            super(message);
        }
    }
}
