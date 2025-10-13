package com.espaciosdeportivos.validation;

import com.espaciosdeportivos.dto.ReservaDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
public class ReservaValidator {
    
    private static final List<String> ESTADOS_VALIDOS = 
        List.of("PENDIENTE", "CONFIRMADA", "EN_CURSO", "COMPLETADA", "CANCELADA", "NO_SHOW");

    public void validarReserva(ReservaDTO dto) {
        validarFechas(dto.getFechaCreacion(), dto.getFechaReserva());
        validarHoras(dto.getHoraInicio(), dto.getHoraFin());
        validarEstado(dto.getEstadoReserva());
        validarMonto(dto.getMontoTotal());
        validarCliente(dto.getClienteId());
        validarDuracionMinima(dto.getHoraInicio(), dto.getHoraFin());
    }

    public void validarFechas(LocalDate fechaCreacion, LocalDate fechaReserva) {
        if (fechaCreacion == null || fechaReserva == null) {
            throw new BusinessException("Las fechas no pueden ser nulas.");
        }
        if (fechaCreacion.isAfter(LocalDate.now())) {
            throw new BusinessException("La fecha de creación no puede ser futura.");
        }
        if (fechaReserva.isBefore(LocalDate.now())) {
            throw new BusinessException("La fecha de la reserva no puede ser en el pasado.");
        }
        // Permitir reservas hasta 3 meses en adelante
        if (fechaReserva.isAfter(LocalDate.now().plusMonths(3))) {
            throw new BusinessException("No se pueden hacer reservas con más de 3 meses de anticipación.");
        }
    }

    public void validarHoras(LocalTime inicio, LocalTime fin) {
        if (inicio == null || fin == null) {
            throw new BusinessException("Las horas de inicio y fin son obligatorias.");
        }
        if (!inicio.isBefore(fin)) {
            throw new BusinessException("La hora de inicio debe ser anterior a la hora de fin.");
        }
        // Validar horario comercial (6:00 - 22:00)
        if (inicio.isBefore(LocalTime.of(6, 0)) || fin.isAfter(LocalTime.of(22, 0))) {
            throw new BusinessException("El horario debe estar entre las 6:00 y 22:00 horas.");
        }
    }

    public void validarDuracionMinima(LocalTime inicio, LocalTime fin) {
        long duracionMinutos = java.time.Duration.between(inicio, fin).toMinutes();
        if (duracionMinutos < 30) {
            throw new BusinessException("La duración mínima de la reserva es de 30 minutos.");
        }
        if (duracionMinutos > 240) {
            throw new BusinessException("La duración máxima de la reserva es de 4 horas.");
        }
    }

    public void validarEstado(String estado) {
        if (estado == null || estado.isBlank()) {
            throw new BusinessException("El estado de la reserva es obligatorio.");
        }
        if (!ESTADOS_VALIDOS.contains(estado.toUpperCase())) {
            throw new BusinessException("Estado inválido. Solo se permiten: " + ESTADOS_VALIDOS);
        }
    }

    public void validarMonto(Double monto) {
        if (monto == null || monto <= 0) {
            throw new BusinessException("El monto total debe ser mayor a 0.");
        }
        if (monto > 10000) {
            throw new BusinessException("El monto total no puede exceder los 10,000.");
        }
    }

    public void validarCliente(Long clienteId) {
        if (clienteId == null || clienteId <= 0) {
            throw new BusinessException("Debe especificar un cliente válido.");
        }
    }

    public void validarCancelacion(String estadoActual) {
        if ("CANCELADA".equals(estadoActual) || "COMPLETADA".equals(estadoActual)) {
            throw new BusinessException("No se puede cancelar una reserva ya cancelada o completada.");
        }
    }

    public void validarConfirmacion(String estadoActual) {
        if (!"PENDIENTE".equals(estadoActual)) {
            throw new BusinessException("Solo se pueden confirmar reservas en estado PENDIENTE.");
        }
    }

    public static class BusinessException extends RuntimeException {
        public BusinessException(String message) {
            super(message);
        }
    }
}