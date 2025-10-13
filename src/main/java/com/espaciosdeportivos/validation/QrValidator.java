package com.espaciosdeportivos.validation;

import com.espaciosdeportivos.dto.QrDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Component
public class QrValidator {

    private static final Pattern CODIGO_QR_PATTERN = Pattern.compile("^[A-Z0-9_-]{6,30}$");

    // esto cambias/sera imagen K
    public void validarCodigoQr(String codigoQr) {
        if (codigoQr == null || !CODIGO_QR_PATTERN.matcher(codigoQr).matches()) {
            throw new BusinessException("Código QR no válido. Debe tener entre 6 y 30 caracteres alfanuméricos, guiones o guiones bajos.");
        }
    }

    public void validarFechas(LocalDateTime generacion, LocalDateTime expiracion) {
        if (generacion == null || expiracion == null) {
            throw new BusinessException("Las fechas de generación y expiración son obligatorias.");
        }
        if (expiracion.isBefore(generacion)) {
            throw new BusinessException("La fecha de expiración no puede ser anterior a la fecha de generación.");
        }
        if (expiracion.isBefore(LocalDateTime.now())) {
            throw new BusinessException("La fecha de expiración debe estar en el futuro.");
        }
    }

    public void validarEstado(Boolean estado) {
        if (estado == null) {
            throw new BusinessException("El estado del QR no puede ser nulo.");
        }
    }

    public void validarQr(QrDTO qrDTO) {
        validarCodigoQr(qrDTO.getCodigoQr());
        validarFechas(qrDTO.getFechaGeneracion(), qrDTO.getFechaExpiracion());
        validarEstado(qrDTO.getEstado());
        // se pued agregar validaciones para idReserva, idInvitado, idUsuarioControl desp
    }

    public static class BusinessException extends RuntimeException {
        public BusinessException(String message) {
            super(message);
        }
    }
}