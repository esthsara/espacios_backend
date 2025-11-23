package com.espaciosdeportivos.service.impl;

import com.espaciosdeportivos.dto.ReservaDTO;
import com.espaciosdeportivos.model.Cliente;
import com.espaciosdeportivos.model.Reserva;
import com.espaciosdeportivos.repository.ReservaRepository;
import com.espaciosdeportivos.repository.ClienteRepository;
import com.espaciosdeportivos.repository.PagoRepository;
import com.espaciosdeportivos.service.IReservaService;
import com.espaciosdeportivos.validation.ReservaValidator;
//import com.espaciosdeportivos.validation.ReservaValidator.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReservaServiceImpl implements IReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private ReservaValidator reservaValidator;

    private ReservaDTO mapToDTO(Reserva reserva) {
        Cliente cliente = reserva.getCliente();
        
        // Calcular información de pagos
        Double totalPagado = pagoRepository.sumMontoConfirmadoPorReserva(reserva.getIdReserva());
        Double saldoPendiente = reserva.getMontoTotal() - (totalPagado != null ? totalPagado : 0.0);
        Boolean pagadaCompleta = saldoPendiente <= 0;
        
        return ReservaDTO.builder()
                .idReserva(reserva.getIdReserva())
                .fechaCreacion(reserva.getFechaCreacion() != null ? 
                    reserva.getFechaCreacion().toLocalDate() : LocalDate.now())
                .fechaReserva(reserva.getFechaReserva())
                .horaInicio(reserva.getHoraInicio())
                .horaFin(reserva.getHoraFin())
                .estadoReserva(reserva.getEstadoReserva())
                .montoTotal(reserva.getMontoTotal())
                .observaciones(reserva.getObservaciones())
                .clienteId(cliente.getId()) // id_persona del cliente
                .duracionMinutos(reserva.getDuracionMinutos())
                .codigoReserva(reserva.getCodigoReserva())
                .nombreCliente(cliente.getNombre() + " " + cliente.getApellidoPaterno())
                .emailCliente(cliente.getEmail())
                .telefonoCliente(cliente.getTelefono())
                .categoriaCliente(cliente.getCategoria())
                .fechaActualizacion(reserva.getFechaActualizacion() != null ? 
                    reserva.getFechaActualizacion().toLocalDate() : null)
                .totalPagado(totalPagado != null ? totalPagado : 0.0)
                .saldoPendiente(saldoPendiente)
                .pagadaCompleta(pagadaCompleta)
                .build();
    }

    private Reserva mapToEntity(ReservaDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con ID: " + dto.getClienteId()));

        return Reserva.builder()
                .fechaReserva(dto.getFechaReserva())
                .horaInicio(dto.getHoraInicio())
                .horaFin(dto.getHoraFin())
                .estadoReserva(dto.getEstadoReserva())
                .montoTotal(dto.getMontoTotal())
                .observaciones(dto.getObservaciones())
                .codigoReserva(dto.getCodigoReserva())
                .cliente(cliente)
                .build();
    }

    @Override
    @Transactional
    public ReservaDTO crear(ReservaDTO dto) {
        reservaValidator.validarReserva(dto);
        
        // Validar disponibilidad
        if (!validarDisponibilidad(dto.getFechaReserva(), dto.getHoraInicio(), dto.getHoraFin())) {
            throw new IllegalArgumentException("No hay disponibilidad para el horario seleccionado");
        }

        Reserva reserva = mapToEntity(dto);
        
        // Generar código único si no se proporciona
        if (reserva.getCodigoReserva() == null) {
            reserva.setCodigoReserva(generarCodigoReserva());
        }
        
        // Establecer estado por defecto
        if (reserva.getEstadoReserva() == null) {
            reserva.setEstadoReserva(Reserva.EstadoReserva.PENDIENTE.name());
        }

        return mapToDTO(reservaRepository.save(reserva));
    }

    @Override
    @Transactional
    public ReservaDTO actualizar(Long id, ReservaDTO dto) {
        reservaValidator.validarReserva(dto);
        
        Reserva reservaExistente = reservaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reserva no encontrada con ID: " + id));

        // Validar que no se pueda modificar una reserva completada o cancelada
        if (!reservaExistente.esModificable()) {
            throw new IllegalArgumentException("No se puede modificar una reserva completada o cancelada");
        }

        // Validar disponibilidad si se cambia fecha/hora
        if (!reservaExistente.getFechaReserva().equals(dto.getFechaReserva()) ||
            !reservaExistente.getHoraInicio().equals(dto.getHoraInicio()) ||
            !reservaExistente.getHoraFin().equals(dto.getHoraFin())) {
            
            if (!validarDisponibilidad(dto.getFechaReserva(), dto.getHoraInicio(), dto.getHoraFin())) {
                throw new IllegalArgumentException("No hay disponibilidad para el nuevo horario seleccionado");
            }
        }

        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con ID: " + dto.getClienteId()));

        reservaExistente.setFechaReserva(dto.getFechaReserva());
        reservaExistente.setHoraInicio(dto.getHoraInicio());
        reservaExistente.setHoraFin(dto.getHoraFin());
        reservaExistente.setEstadoReserva(dto.getEstadoReserva());
        reservaExistente.setMontoTotal(dto.getMontoTotal());
        reservaExistente.setObservaciones(dto.getObservaciones());
        reservaExistente.setCliente(cliente);

        return mapToDTO(reservaRepository.save(reservaExistente));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean validarDisponibilidad(LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {
        List<Reserva> reservasSolapadas = reservaRepository.findReservasSolapadas(fecha, horaInicio, horaFin);
        return reservasSolapadas.isEmpty();
    }

    @Override
    @Transactional
    public ReservaDTO confirmarReserva(Long idReserva) {
        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new EntityNotFoundException("Reserva no encontrada con ID: " + idReserva));

        reservaValidator.validarConfirmacion(reserva.getEstadoReserva());

        reserva.setEstadoReserva(Reserva.EstadoReserva.CONFIRMADA.name());
        return mapToDTO(reservaRepository.save(reserva));
    }

    @Override
    @Transactional
    public ReservaDTO cancelarReserva(Long idReserva, String motivo) {
        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new EntityNotFoundException("Reserva no encontrada con ID: " + idReserva));

        reservaValidator.validarCancelacion(reserva.getEstadoReserva());

        reserva.setEstadoReserva(Reserva.EstadoReserva.CANCELADA.name());
        reserva.setObservaciones("CANCELADA: " + motivo + 
            (reserva.getObservaciones() != null ? ". " + reserva.getObservaciones() : ""));
        
        return mapToDTO(reservaRepository.save(reserva));
    }

    @Override
    @Transactional
    public ReservaDTO marcarComoEnCurso(Long idReserva) {
        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new EntityNotFoundException("Reserva no encontrada con ID: " + idReserva));

        if (!Reserva.EstadoReserva.CONFIRMADA.name().equals(reserva.getEstadoReserva())) {
            throw new IllegalArgumentException("Solo se pueden marcar como en curso reservas CONFIRMADAS");
        }

        reserva.setEstadoReserva(Reserva.EstadoReserva.EN_CURSO.name());
        return mapToDTO(reservaRepository.save(reserva));
    }

    @Override
    @Transactional
    public ReservaDTO marcarComoCompletada(Long idReserva) {
        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new EntityNotFoundException("Reserva no encontrada con ID: " + idReserva));

        if (!Reserva.EstadoReserva.EN_CURSO.name().equals(reserva.getEstadoReserva())) {
            throw new IllegalArgumentException("Solo se pueden completar reservas EN CURSO");
        }

        reserva.setEstadoReserva(Reserva.EstadoReserva.COMPLETADA.name());
        return mapToDTO(reservaRepository.save(reserva));
    }

    @Override
    @Transactional
    public ReservaDTO marcarComoNoShow(Long idReserva) {
        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new EntityNotFoundException("Reserva no encontrada con ID: " + idReserva));

        if (!Reserva.EstadoReserva.CONFIRMADA.name().equals(reserva.getEstadoReserva())) {
            throw new IllegalArgumentException("Solo se pueden marcar como no-show reservas CONFIRMADAS");
        }

        reserva.setEstadoReserva(Reserva.EstadoReserva.NO_SHOW.name());
        return mapToDTO(reservaRepository.save(reserva));
    }

    @Override
    public String generarCodigoReserva() {
        return "RES-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    // MÉTODOS DE CONSULTA
    @Override
    @Transactional(readOnly = true)
    public List<ReservaDTO> listarTodas() {
        return reservaRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ReservaDTO obtenerPorId(Long id) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reserva no encontrada con ID: " + id));
        return mapToDTO(reserva);
    }

    @Override
    @Transactional(readOnly = true)
    public ReservaDTO obtenerPorCodigoReserva(String codigoReserva) {
        Reserva reserva = reservaRepository.findByCodigoReserva(codigoReserva)
                .orElseThrow(() -> new EntityNotFoundException("Reserva no encontrada con código: " + codigoReserva));
        return mapToDTO(reserva);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reserva no encontrada con ID: " + id));
        
        // Validar que solo se puedan eliminar reservas canceladas o pendientes
        if (reserva.estaActiva()) {
            throw new IllegalArgumentException("No se puede eliminar una reserva confirmada o en curso");
        }
        
        reservaRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservaDTO> buscarPorCliente(Long idCliente) {
        return reservaRepository.findByClienteId(idCliente).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservaDTO> buscarPorEstado(String estado) {
        return reservaRepository.findByEstadoReserva(estado).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservaDTO> buscarPorRangoFechas(LocalDate inicio, LocalDate fin) {
        return reservaRepository.findByFechaReservaBetween(inicio, fin).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservaDTO> buscarReservasActivasDelCliente(Long clienteId) {
        return reservaRepository.findReservasActivasDelCliente(clienteId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservaDTO> obtenerReservasDelDia(LocalDate fecha) {
        return reservaRepository.findReservasConfirmadasDelDia(fecha).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /*@Override
    @Transactional(readOnly = true)
    public List<ReservaDTO> obtenerReservasProximas() {
        return reservaRepository.findReservasProximas().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }*/

    @Override
    @Transactional(readOnly = true)
    public Double calcularIngresosEnRango(LocalDate inicio, LocalDate fin) {
        Double ingresos = reservaRepository.calcularIngresosEnRango(inicio, fin);
        return ingresos != null ? ingresos : 0.0;
    }

    @Override
    public void validarFechaReserva(LocalDate fechaReserva) {
        if (fechaReserva.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("No se puede reservar una fecha pasada");
        }
    }
}