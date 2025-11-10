package com.espaciosdeportivos.validation;

import com.espaciosdeportivos.dto.ReservaDTO;
import com.espaciosdeportivos.model.Cancha;
import com.espaciosdeportivos.model.Reserva;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Component
public class ReservaValidator {

    private static final List<String> ESTADOS_VALIDOS = List.of(
        "PENDIENTE", "CONFIRMADA", "EN_CURSO", "COMPLETADA", "CANCELADA"
    );

    public void validarReserva(ReservaDTO dto) {
        validarFechas(dto.getFechaCreacion(), dto.getFechaReserva());
        validarHoras(dto.getHoraInicio(), dto.getHoraFin());
        validarEstado(dto.getEstadoReserva());
        //validarMonto(dto.getMontoTotal());
        validarCliente(dto.getClienteId());
        validarDuracion(dto.getHoraInicio(), dto.getHoraFin());
    }


    /*public void validarReprogramacion(Reserva reservaExistente, ReservaDTO nuevaReserva) {
        if (!reservaExistente.puedeReprogramar()) {
            throw new BusinessException("Solo se puede reprogramar con 8+ horas de anticipación");
        }
        validarDuracion(nuevaReserva.getHoraInicio(), nuevaReserva.getHoraFin());
    }*/

   /*  public void validarReprogramacionHorario(Cancha cancha, LocalDate fecha, LocalTime inicio, LocalTime fin) {
        boolean existeConflicto = reservaRepository.existeConflictoHorario(
            cancha.getIdCancha(), fecha, inicio, fin
        );

        if (existeConflicto) {
            throw new IllegalArgumentException("El horario seleccionado ya está ocupado para esta cancha");
        }
    }*/


    /*public void validarCancelacion(Reserva reserva) {
        if (!reserva.puedeCancelar()) {
            throw new BusinessException("Solo se puede cancelar con 12+ horas de anticipación");
        }
        validarCancelacion(reserva.getEstadoReserva());
    }

    public void validarCancelacion(String estadoActual) {
        if ("CANCELADA".equals(estadoActual) || "COMPLETADA".equals(estadoActual)) {
            throw new BusinessException("No se puede cancelar una reserva ya cancelada o completada");
        }
    }*/

    /*public void validarConfirmacion(String estadoActual) {
        if (!"PENDIENTE".equals(estadoActual)) {
            throw new BusinessException("Solo se pueden confirmar reservas en estado PENDIENTE");
        }
    }*/

    // ---------- Validaciones internas ----------


    private void validarFechas(LocalDateTime fechaCreacion, LocalDate fechaReserva) {
        if (fechaCreacion == null || fechaReserva == null) {
            throw new BusinessException("Las fechas no pueden ser nulas.");
        }
        if (fechaCreacion.isAfter(LocalDateTime.now())) {
            throw new BusinessException("La fecha de creación no puede ser futura.");
        }
        if (fechaReserva.isBefore(LocalDate.now())) {
            throw new BusinessException("La fecha de la reserva no puede ser en el pasado.");
        }
        if (fechaReserva.isAfter(LocalDate.now().plusMonths(3))) {
            throw new BusinessException("No se pueden hacer reservas con más de 3 meses de anticipación.");
        }
    }

    private void validarHoras(LocalTime inicio, LocalTime fin) {
        if (inicio == null || fin == null) {
            throw new BusinessException("Las horas de inicio y fin son obligatorias.");
        }
        if (!inicio.isBefore(fin)) {
            throw new BusinessException("La hora de inicio debe ser anterior a la hora de fin.");
        }
        /*if (inicio.isBefore(LocalTime.of(6, 0)) || fin.isAfter(LocalTime.of(22, 0))) {
            throw new BusinessException("El horario debe estar entre las 6:00 y 22:00 horas.");
        }*/
    }

    private void validarDuracion(LocalTime inicio, LocalTime fin) {
        long duracionMinutos = java.time.Duration.between(inicio, fin).toMinutes();
        if (duracionMinutos < 30) {
            throw new BusinessException("La duración mínima de la reserva es de 30 minutos.");
        }
        /*if (duracionMinutos > 480) {
            throw new BusinessException("La duración máxima de la reserva es de 12 horas.");
        }*/
    }

    private void validarEstado(String estado) {
        if (estado == null || estado.isBlank()) {
            throw new BusinessException("El estado de la reserva es obligatorio.");
        }
        if (!ESTADOS_VALIDOS.contains(estado.toUpperCase())) {
            throw new BusinessException("Estado inválido. Solo se permiten: " + ESTADOS_VALIDOS);
        }
    }

    /*private void validarMonto(Double monto) {
        if (monto == null || monto <= 0) {
            throw new BusinessException("El monto total debe ser mayor a 0.");
        }
        if (monto > 10000) {
            throw new BusinessException("El monto total no puede exceder los 10,000.");
        }*/
    //}

    private void validarCliente(Long clienteId) {
        if (clienteId == null || clienteId <= 0) {
            throw new BusinessException("Debe especificar un cliente válido.");
        }
    }

    // ---------- Excepción personalizada ----------
    public static class BusinessException extends RuntimeException {
        public BusinessException(String message) {
            super(message);
        }
    }
}
