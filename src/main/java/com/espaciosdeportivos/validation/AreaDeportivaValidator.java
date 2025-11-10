package com.espaciosdeportivos.validation;


import com.espaciosdeportivos.dto.AreaDeportivaDTO;
import org.springframework.stereotype.Component;
import com.espaciosdeportivos.validation.AreaDeportivaValidator.BusinessException;


import java.time.LocalTime;

@Component
public class AreaDeportivaValidator {

    public void validarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new BusinessException("El nombre del área es obligatorio.");
        }
        if (nombre.length() > 200) {
            throw new BusinessException("El nombre del área no puede exceder 100 caracteres.");
        }
    }

    public void validarDescripcion(String descripcion) {
        if (descripcion != null && descripcion.length() > 600) {
            throw new BusinessException("La descripción no puede exceder 400 caracteres.");
        }
    }

    public void validarHoras(LocalTime inicio, LocalTime fin) {
        if (inicio == null || fin == null) {
            throw new BusinessException("Las horas de inicio y fin son obligatorias.");
        }
        // Validar rango válido de horas (aunque LocalTime ya garantiza esto)
        if (inicio.isBefore(LocalTime.MIN) || inicio.isAfter(LocalTime.MAX) ||
            fin.isBefore(LocalTime.MIN) || fin.isAfter(LocalTime.MAX)) {
            throw new BusinessException("Las horas deben estar entre 00:00 y 23:59.");
        }
    }



    public void validarCoordenadas(Double lat, Double lng) {
        if (lat == null || lng == null) {
            throw new BusinessException("Las coordenadas (latitud/longitud) son obligatorias.");
        }
        if (lat < -90 || lat > 90) {
            throw new BusinessException("La latitud debe estar entre -90 y 90.");
        }
        if (lng < -180 || lng > 180) {
            throw new BusinessException("La longitud debe estar entre -180 y 180.");
        }
    }

    public void validarIds(Long idZona, Long idAdmin) {
        if (idZona == null || idZona <= 0) {
            throw new BusinessException("El ID de la zona debe ser positivo.");
        }
        if (idAdmin == null || idAdmin <= 0) {
            throw new BusinessException("El ID del administrador debe ser positivo.");
        }
    }

    public void validarEstado(Boolean estado) {
        if (estado == null) {
            throw new BusinessException("El estado (soft delete) es obligatorio.");
        }
    }

    public void validarArea(AreaDeportivaDTO dto) {
        validarNombre(dto.getNombreArea());
        validarDescripcion(dto.getDescripcionArea());
        validarHoras(dto.getHoraInicioArea(), dto.getHoraFinArea());
        validarCoordenadas(dto.getLatitud(), dto.getLongitud());
        validarIds(dto.getIdZona(), dto.getId());
        validarEstado(dto.getEstado());
    }

    public static class BusinessException extends RuntimeException {
        public BusinessException(String message) { super(message); }
    }
}
