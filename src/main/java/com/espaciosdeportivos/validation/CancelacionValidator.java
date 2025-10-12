package com.espaciosdeportivos.validation;

import com.espaciosdeportivos.dto.CancelacionDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class CancelacionValidator {

    public void validarMotivo(String motivo) {
        if (motivo == null || motivo.trim().length() < 5 || motivo.length() > 255) {
            throw new BusinessException("El motivo debe tener entre 5 y 255 caracteres.");
        }
    }

    public void validarFecha(LocalDate fecha) {
        if (fecha == null || fecha.isAfter(LocalDate.now())) {
            throw new BusinessException("La fecha de cancelación no puede ser futura ni nula.");
        }
    }

    public void validarHora(LocalTime hora) {
        if (hora == null) {
            throw new BusinessException("La hora de cancelación es obligatoria.");
        }
    }

    public void validarEstado(Boolean estado) {
        if (estado == null) {
            throw new BusinessException("El estado de la cancelación no puede ser nulo.");
        }
    }

    public void validarCancelacion(CancelacionDTO dto) {
        validarMotivo(dto.getMotivo());
        validarFecha(dto.getFechaCancelacion());
        validarHora(dto.getHoraCancelacion());
        validarEstado(dto.getEstado());
        // Puedes agregar validaciones para idCliente y idReserva si son obligatorios
    }

    public static class BusinessException extends RuntimeException {
        public BusinessException(String message) {
            super(message);
        }
    }
}