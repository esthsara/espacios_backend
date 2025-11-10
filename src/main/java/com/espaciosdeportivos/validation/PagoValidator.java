package com.espaciosdeportivos.validation;

import com.espaciosdeportivos.dto.PagoDTO;
//import com.espaciosdeportivos.model.Pago;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class PagoValidator {
    
    private static final List<String> TIPOS_PAGO_VALIDOS = List.of("PARCIAL", "TOTAL", "ANTICIPO");
    private static final List<String> METODOS_PAGO_VALIDOS = List.of("EFECTIVO", "TARJETA_CREDITO", "TARJETA_DEBITO", "TRANSFERENCIA", "QR");
    private static final List<String> ESTADOS_VALIDOS = List.of("PENDIENTE", "CONFIRMADO", "ANULADO", "RECHAZADO");

    public void validarPagoCreacion(PagoDTO dto) {
        validarMonto(dto.getMonto());
        validarFecha(dto.getFecha());
        validarTipoPago(dto.getTipoPago());
        validarMetodoPago(dto.getMetodoPago());
        validarEstado(dto.getEstado());
        validarIdReserva(dto.getIdReserva());
        validarCodigoTransaccion(dto.getCodigoTransaccion(), false);
        validarDescripcion(dto.getDescripcion());
    }

    public void validarPagoActualizacion(PagoDTO dto) {
        validarMonto(dto.getMonto());
        validarFecha(dto.getFecha());
        validarTipoPago(dto.getTipoPago());
        validarMetodoPago(dto.getMetodoPago());
        validarEstado(dto.getEstado());
        validarIdReserva(dto.getIdReserva());
        validarCodigoTransaccion(dto.getCodigoTransaccion(), true);
        validarDescripcion(dto.getDescripcion());
    }

    public void validarConfirmacionPago(String codigoTransaccion) {
        if (codigoTransaccion == null || codigoTransaccion.trim().isEmpty()) {
            throw new BusinessException("El código de transacción es obligatorio para confirmar el pago.");
        }
        if (codigoTransaccion.length() > 100) {
            throw new BusinessException("El código de transacción no puede exceder los 100 caracteres.");
        }
    }

    public void validarAnulacionPago(String razon) {
        if (razon == null || razon.trim().isEmpty()) {
            throw new BusinessException("La razón de anulación es obligatoria.");
        }
        if (razon.length() > 200) {
            throw new BusinessException("La razón de anulación no puede exceder los 200 caracteres.");
        }
    }

    public void validarMonto(Double monto) {
        if (monto == null) {
            throw new BusinessException("El monto no puede ser nulo.");
        }
        if (monto <= 0) {
            throw new BusinessException("El monto debe ser mayor a 0.");
        }
        if (monto > 1000000) {
            throw new BusinessException("El monto no puede exceder 1,000,000.");
        }
    }

    public void validarFecha(LocalDate fecha) {
        if (fecha == null) {
            throw new BusinessException("La fecha del pago es obligatoria.");
        }
        if (fecha.isAfter(LocalDate.now())) {
            throw new BusinessException("La fecha del pago no puede ser futura.");
        }
        if (fecha.isBefore(LocalDate.now().minusYears(1))) {
            throw new BusinessException("La fecha del pago no puede ser anterior a un año.");
        }
    }

    public void validarTipoPago(String tipoPago) {
        if (tipoPago == null || tipoPago.trim().isEmpty()) {
            throw new BusinessException("El tipo de pago es obligatorio.");
        }
        if (!TIPOS_PAGO_VALIDOS.contains(tipoPago.toUpperCase())) {
            throw new BusinessException("Tipo de pago inválido. Opciones permitidas: " + TIPOS_PAGO_VALIDOS);
        }
    }

    public void validarMetodoPago(String metodoPago) {
        if (metodoPago == null || metodoPago.trim().isEmpty()) {
            throw new BusinessException("El método de pago es obligatorio.");
        }
        if (!METODOS_PAGO_VALIDOS.contains(metodoPago.toUpperCase())) {
            throw new BusinessException("Método de pago inválido. Opciones permitidas: " + METODOS_PAGO_VALIDOS);
        }
    }

    public void validarEstado(String estado) {
        if (estado == null || estado.trim().isEmpty()) {
            throw new BusinessException("El estado del pago es obligatorio.");
        }
        if (!ESTADOS_VALIDOS.contains(estado.toUpperCase())) {
            throw new BusinessException("Estado de pago inválido. Opciones permitidas: " + ESTADOS_VALIDOS);
        }
    }

    public void validarIdReserva(Long idReserva) {
        if (idReserva == null) {
            throw new BusinessException("El ID de reserva es obligatorio.");
        }
        if (idReserva <= 0) {
            throw new BusinessException("El ID de reserva debe ser válido.");
        }
    }

    public void validarCodigoTransaccion(String codigoTransaccion, boolean esOpcional) {
        if (!esOpcional && codigoTransaccion == null) {
            throw new BusinessException("El código de transacción es obligatorio para este tipo de pago.");
        }
        
        if (codigoTransaccion != null) {
            if (codigoTransaccion.trim().isEmpty()) {
                throw new BusinessException("El código de transacción no puede estar vacío.");
            }
            if (codigoTransaccion.length() > 100) {
                throw new BusinessException("El código de transacción no puede exceder los 100 caracteres.");
            }
            if (!codigoTransaccion.matches("^[A-Za-z0-9_-]+$")) {
                throw new BusinessException("El código de transacción solo puede contener letras, números, guiones y guiones bajos.");
            }
        }
    }

    public void validarDescripcion(String descripcion) {
        if (descripcion != null && descripcion.length() > 500) {
            throw new BusinessException("La descripción no puede exceder los 500 caracteres.");
        }
    }

    public void validarTransicionEstado(String estadoActual, String nuevoEstado) {
        if (("CONFIRMADO".equals(estadoActual) || "ANULADO".equals(estadoActual)) && 
            !estadoActual.equals(nuevoEstado)) {
            throw new BusinessException("No se puede modificar un pago en estado '" + estadoActual + "'.");
        }
        
        if ("CONFIRMADO".equals(nuevoEstado) && !"PENDIENTE".equals(estadoActual)) {
            throw new BusinessException("Solo se puede confirmar un pago que esté en estado PENDIENTE.");
        }
        
        if ("ANULADO".equals(nuevoEstado) && !"PENDIENTE".equals(estadoActual)) {
            throw new BusinessException("Solo se puede anular un pago que esté en estado PENDIENTE.");
        }
    }

    public static class BusinessException extends RuntimeException {
        public BusinessException(String message) {
            super(message);
        }
    }
}