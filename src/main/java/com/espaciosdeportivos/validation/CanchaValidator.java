package com.espaciosdeportivos.validation;

import com.espaciosdeportivos.dto.CanchaDTO;
import org.springframework.stereotype.Component;

@Component
public class CanchaValidator {

    public void validarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new BusinessException("El nombre de la cancha es obligatorio.");
        }
        if (nombre.length() > 200) {
            throw new BusinessException("El nombre no puede exceder 100 caracteres.");
        }
    }

    public void validarCosto(Double costoHora) {
        if (costoHora == null || costoHora <= 0) {
            throw new BusinessException("El costo por hora debe ser positivo.");
        }
    }

    public void validarCapacidad(Integer capacidad) {
        if (capacidad == null || capacidad <= 0) {
            throw new BusinessException("La capacidad debe ser positiva.");
        }
    }

    public void validarHoras(String inicio, String fin) {
        if (inicio == null || inicio.isBlank() || fin == null || fin.isBlank()) {
            throw new BusinessException("Las horas de inicio y fin son obligatorias (HH:mm).");
        }
        // Validación simple de formato y orden "HH:mm"
        if (!inicio.matches("^\\d{2}:\\d{2}$") || !fin.matches("^\\d{2}:\\d{2}$")) {
            throw new BusinessException("Formato de hora inválido. Use HH:mm.");
        }
        if (fin.compareTo(inicio) <= 0) {
            throw new BusinessException("La hora de fin debe ser posterior a la de inicio.");
        }
    }

    public void validarTexto(String valor, String campo, int maxLen) {
        if (valor == null || valor.isBlank()) {
            throw new BusinessException("El campo " + campo + " es obligatorio.");
        }
        if (valor.length() > maxLen) {
            throw new BusinessException("El campo " + campo + " no puede exceder " + maxLen + " caracteres.");
        }
    }

    public void validarIds(Long idArea) {
        if (idArea == null || idArea <= 0) {
            throw new BusinessException("El id del área deportiva debe ser positivo.");
        }
    }

    public void validarEstadoBool(Boolean estado) {
        if (estado == null) {
            throw new BusinessException("El estado booleano es obligatorio.");
        }
    }

    public void validarCancha(CanchaDTO dto) {
        validarNombre(dto.getNombre());
        validarCosto(dto.getCostoHora());
        validarCapacidad(dto.getCapacidad());
        validarTexto(dto.getEstado(), "estado", 100);
        validarTexto(dto.getMantenimiento(), "mantenimiento", 100);
        validarHoras(dto.getHoraInicio(), dto.getHoraFin());
        validarTexto(dto.getTipoSuperficie(), "tipoSuperficie", 100);
        validarTexto(dto.getTamano(), "tamano", 100);
        validarTexto(dto.getIluminacion(), "iluminacion", 100);
        validarTexto(dto.getCubierta(), "cubierta", 100);
        if (dto.getUrlImagen() != null && dto.getUrlImagen().length() > 800) {
            throw new BusinessException("La URL de imagen no puede exceder 800 caracteres.");
        }
        validarIds(dto.getIdAreadeportiva());
        validarEstadoBool(dto.getEstadobool());
    }

    public static class BusinessException extends RuntimeException {
        public BusinessException(String message) { super(message); }
    }
}
