package com.espaciosdeportivos.service.impl;

import com.espaciosdeportivos.dto.PagoDTO;
import com.espaciosdeportivos.model.Incluye;
import com.espaciosdeportivos.model.Pago;
import com.espaciosdeportivos.model.Reserva;
import com.espaciosdeportivos.repository.IncluyeRepository;
import com.espaciosdeportivos.repository.PagoRepository;
import com.espaciosdeportivos.repository.ReservaRepository;
import com.espaciosdeportivos.service.IPagoService;
import com.espaciosdeportivos.validation.PagoValidator;
import com.espaciosdeportivos.validation.PagoValidator.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder.In;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PagoServiceImpl implements IPagoService {

    private PagoRepository pagoRepository;
    private ReservaRepository reservaRepository;
    private PagoValidator pagoValidator;
    private final IncluyeRepository incluyeRepository;





    @Override
    @Transactional
    public PagoDTO crear(PagoDTO dto) {
        pagoValidator.validarPagoCreacion(dto);
        
        Reserva reserva = reservaRepository.findById(dto.getIdReserva())
                .orElseThrow(() -> new EntityNotFoundException("Reserva no encontrada con id: " + dto.getIdReserva()));
        Incluye incluye = incluyeRepository.findByReservaIdReserva(dto.getIdReserva())
                .orElseThrow(() -> new EntityNotFoundException("Incluye no encontrado con id de reserva: " + dto.getIdReserva()));

        Double montoPagado = pagoRepository.sumMontoConfirmadoPorReserva(reserva.getIdReserva());
        Double saldoPendiente = incluye.getMontoTotal() - (montoPagado != null ? montoPagado : 0.0);
        
        if (dto.getMonto() > saldoPendiente) {
            throw new IllegalArgumentException(
                String.format("El monto del pago (%.2f) excede el saldo pendiente (%.2f)", 
                    dto.getMonto(), saldoPendiente)
            );
        }

        // Validar código de transacción único
        if (dto.getCodigoTransaccion() != null && 
            pagoRepository.existsByCodigoTransaccion(dto.getCodigoTransaccion())) {
            throw new IllegalArgumentException("El código de transacción ya existe");
        }

        Pago pago = mapToEntity(dto);

        //Pago pago = convertToEntity(dto);
        
        // Establecer estado por defecto
        if (pago.getEstado() == null) {
            pago.setEstado(Pago.EstadoPago.PENDIENTE.name());
        }
        
        return mapToDTO(pagoRepository.save(pago));
    }

    @Override
    @Transactional
    public PagoDTO actualizar(Long id, PagoDTO dto) {
        pagoValidator.validarPagoActualizacion(dto);
        
        Pago pagoExistente = pagoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pago no encontrado con id: " + id));

        // Validar transición de estado
        pagoValidator.validarTransicionEstado(pagoExistente.getEstado(), dto.getEstado());

        // Validar que solo se puedan modificar pagos pendientes
        if (!pagoExistente.getEstado().equals(Pago.EstadoPago.PENDIENTE.name())) {
            throw new IllegalArgumentException("Solo se pueden modificar pagos en estado PENDIENTE");
        }

        Reserva reserva = reservaRepository.findById(dto.getIdReserva())
                .orElseThrow(() -> new EntityNotFoundException("Reserva no encontrada con id: " + dto.getIdReserva()));

        // Validar código de transacción único si se cambia
        if (dto.getCodigoTransaccion() != null && 
            !dto.getCodigoTransaccion().equals(pagoExistente.getCodigoTransaccion()) &&
            pagoRepository.existsByCodigoTransaccion(dto.getCodigoTransaccion())) {
            throw new IllegalArgumentException("El código de transacción ya existe");
        }

        // Actualizar entidad
        pagoExistente.setMonto(dto.getMonto());
        pagoExistente.setFecha(dto.getFecha());
        pagoExistente.setTipoPago(dto.getTipoPago());
        pagoExistente.setMetodoPago(dto.getMetodoPago());
        pagoExistente.setEstado(dto.getEstado());
        pagoExistente.setCodigoTransaccion(dto.getCodigoTransaccion());
        pagoExistente.setDescripcion(dto.getDescripcion());
        pagoExistente.setReserva(reserva);

        return mapToDTO(pagoRepository.save(pagoExistente));
    }

    @Override
    @Transactional(readOnly = true)
    public PagoDTO obtenerPorId(Long id) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pago no encontrado con id: " + id));
        return mapToDTO(pago);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pago no encontrado con id: " + id));
        
        // Validar que solo se puedan eliminar pagos pendientes
        if (!pago.getEstado().equals(Pago.EstadoPago.PENDIENTE.name())) {
            throw new IllegalArgumentException("Solo se pueden eliminar pagos en estado PENDIENTE");
        }
        
        pagoRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PagoDTO> listarTodos() {
        return pagoRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // BÚSQUEDAS BÁSICAS
    @Override
    @Transactional(readOnly = true)
    public List<PagoDTO> buscarPorEstado(String estado) {
        return pagoRepository.findByEstado(estado).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PagoDTO> buscarPorMetodo(String metodoPago) {
        return pagoRepository.findByMetodoPago(metodoPago).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PagoDTO> buscarPorTipo(String tipoPago) {
        return pagoRepository.findByTipoPago(tipoPago).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PagoDTO> buscarPorFecha(LocalDate fecha) {
        return pagoRepository.findByFecha(fecha).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PagoDTO> buscarPorRangoFechas(LocalDate inicio, LocalDate fin) {
        return pagoRepository.findByFechaBetween(inicio, fin).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // BÚSQUEDAS ESPECÍFICAS
    @Override
    @Transactional(readOnly = true)
    public PagoDTO obtenerPorCodigoTransaccion(String codigoTransaccion) {
        Pago pago = pagoRepository.findByCodigoTransaccion(codigoTransaccion)
                .orElseThrow(() -> new EntityNotFoundException("Pago no encontrado con código: " + codigoTransaccion));
        return mapToDTO(pago);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PagoDTO> buscarPorReserva(Long idReserva) {
        return pagoRepository.findByReservaIdReserva(idReserva).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PagoDTO> buscarPorReservaYEstado(Long idReserva, String estado) {
        return pagoRepository.findByReservaIdReservaAndEstado(idReserva, estado).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PagoDTO> buscarPorCliente(Long idCliente) {
        return pagoRepository.findByClienteId(idCliente).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PagoDTO> buscarPorClienteYEstado(Long idCliente, String estado) {
        return pagoRepository.findByClienteIdAndEstado(idCliente, estado).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // OPERACIONES DE NEGOCIO
    @Override
    @Transactional
    public PagoDTO confirmarPago(Long idPago, String codigoTransaccion) {
        pagoValidator.validarConfirmacionPago(codigoTransaccion);
        
        Pago pago = pagoRepository.findById(idPago)
                .orElseThrow(() -> new EntityNotFoundException("Pago no encontrado con id: " + idPago));
        
        if (!Pago.EstadoPago.PENDIENTE.name().equals(pago.getEstado())) {
            throw new BusinessException("Solo se pueden confirmar pagos en estado PENDIENTE");
        }
        
        pago.setEstado(Pago.EstadoPago.CONFIRMADO.name());
        pago.setCodigoTransaccion(codigoTransaccion);
        
        return mapToDTO(pagoRepository.save(pago));
    }

    @Override
    @Transactional
    public PagoDTO anularPago(Long idPago, String razon) {
        pagoValidator.validarAnulacionPago(razon);
        
        Pago pago = pagoRepository.findById(idPago)
                .orElseThrow(() -> new EntityNotFoundException("Pago no encontrado con id: " + idPago));
        
        if (!Pago.EstadoPago.PENDIENTE.name().equals(pago.getEstado())) {
            throw new BusinessException("Solo se pueden anular pagos en estado PENDIENTE");
        }
        
        pago.setEstado(Pago.EstadoPago.ANULADO.name());
        pago.setDescripcion("ANULADO: " + razon + ". " + 
                           (pago.getDescripcion() != null ? pago.getDescripcion() : ""));
        
        return mapToDTO(pagoRepository.save(pago));
    }

    @Override
    @Transactional
    public PagoDTO rechazarPago(Long idPago, String razon) {
        Pago pago = pagoRepository.findById(idPago)
                .orElseThrow(() -> new EntityNotFoundException("Pago no encontrado con id: " + idPago));
        
        if (!Pago.EstadoPago.PENDIENTE.name().equals(pago.getEstado())) {
            throw new BusinessException("Solo se pueden rechazar pagos en estado PENDIENTE");
        }
        
        pago.setEstado(Pago.EstadoPago.RECHAZADO.name());
        pago.setDescripcion("RECHAZADO: " + razon + ". " + 
                           (pago.getDescripcion() != null ? pago.getDescripcion() : ""));
        
        return mapToDTO(pagoRepository.save(pago));
    }

    // REPORTES Y CONSULTAS
    @Override
    @Transactional(readOnly = true)
    public Double obtenerTotalPagosConfirmadosPorFecha(LocalDate fecha) {
        Double total = pagoRepository.sumMontoTotalPorFecha(fecha);
        return total != null ? total : 0.0;
    }

    @Override
    @Transactional(readOnly = true)
    public Double obtenerTotalPagosConfirmadosPorRango(LocalDate inicio, LocalDate fin) {
        Double total = pagoRepository.sumMontoTotalPorRango(inicio, fin);
        return total != null ? total : 0.0;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PagoDTO> obtenerPagosConfirmadosEnRango(LocalDate inicio, LocalDate fin) {
        return pagoRepository.findPagosConfirmadosEnRango(inicio, fin).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Double obtenerSaldoPendienteReserva(Long idReserva) {

        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new EntityNotFoundException("Reserva no encontrada con id: " + idReserva));
        Incluye incluye = incluyeRepository.findByReservaIdReserva(idReserva)
                .orElseThrow(() -> new EntityNotFoundException("Incluye no encontrado con id de reserva: " + idReserva));

        Double montoPagado = pagoRepository.sumMontoConfirmadoPorReserva(idReserva);
        return incluye.getMontoTotal() - (montoPagado != null ? montoPagado : 0.0);
    }

    // VALIDACIONES
    @Override
    @Transactional(readOnly = true)
    public boolean validarCodigoTransaccionUnico(String codigoTransaccion) {
        if (codigoTransaccion == null) return true;
        return !pagoRepository.existsByCodigoTransaccion(codigoTransaccion);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePagoConMismoMontoYReserva(Double monto, Long idReserva) {
        return pagoRepository.existsByMontoAndReservaIdReserva(monto, idReserva);
    }
    //mapeo

    private PagoDTO mapToDTO(Pago pago) {

        Reserva reserva = pago.getReserva();
        Incluye incluye = incluyeRepository.findByReservaIdReserva(reserva.getIdReserva())
                .orElseThrow(() -> new EntityNotFoundException("Incluye no encontrado con id de reserva: " + reserva.getIdReserva()));
        
        // Calcular saldo pendiente
        Double montoPagado = pagoRepository.sumMontoConfirmadoPorReserva(reserva.getIdReserva());
        Double saldoPendiente = incluye.getMontoTotal() - (montoPagado != null ? montoPagado : 0.0);
        
        return PagoDTO.builder()
                .idPago(pago.getIdPago())
                .monto(pago.getMonto())
                .fecha(pago.getFecha())
                .tipoPago(pago.getTipoPago())
                .metodoPago(pago.getMetodoPago())
                .estado(pago.getEstado())
                .codigoTransaccion(pago.getCodigoTransaccion())
                .descripcion(pago.getDescripcion())
                .idReserva(pago.getReserva() != null ? pago.getReserva().getIdReserva() : null)
                .build();
    }

    private Pago mapToEntity(PagoDTO dto) {
        
        Reserva reserva = reservaRepository.findById(dto.getIdReserva())
                .orElseThrow(() -> new EntityNotFoundException("Reserva no encontrada con id: " + dto.getIdReserva()));

        return Pago.builder()
                .monto(dto.getMonto())
                .fecha(dto.getFecha())
                .tipoPago(dto.getTipoPago())
                .metodoPago(dto.getMetodoPago())
                .estado(dto.getEstado())
                .codigoTransaccion(dto.getCodigoTransaccion())
                .descripcion(dto.getDescripcion())
                .reserva(reserva)
                .build();
    }
}