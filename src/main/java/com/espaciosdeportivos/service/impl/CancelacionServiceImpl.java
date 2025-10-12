package com.espaciosdeportivos.service.impl;

import com.espaciosdeportivos.dto.CancelacionDTO;
import com.espaciosdeportivos.model.Cancelacion;
import com.espaciosdeportivos.model.Reserva;
import com.espaciosdeportivos.model.Cliente;
import com.espaciosdeportivos.repository.CancelacionRepository;
import com.espaciosdeportivos.repository.ReservaRepository;
import com.espaciosdeportivos.repository.ClienteRepository;
import com.espaciosdeportivos.service.ICancelacionService;
import com.espaciosdeportivos.validation.CancelacionValidator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CancelacionServiceImpl implements ICancelacionService {

    private final CancelacionRepository cancelacionRepository;
    private final ReservaRepository reservaRepository;
    private final ClienteRepository clienteRepository;
    private final CancelacionValidator cancelacionValidator;

    @Override
    @Transactional(readOnly = true)
    public List<CancelacionDTO> obtenerTodasLasCancelaciones() {
        return cancelacionRepository.findAll().stream()
                .filter(Cancelacion::getEstado)
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CancelacionDTO obtenerCancelacionPorId(Long id) {
        Cancelacion cancelacion = cancelacionRepository.findById(id)
                .filter(Cancelacion::getEstado)
                .orElseThrow(() -> new RuntimeException("Cancelación no encontrada o inactiva con ID: " + id));
        return convertToDTO(cancelacion);
    }

    @Override
    public CancelacionDTO crearCancelacion(@Valid CancelacionDTO dto) {
        cancelacionValidator.validarCancelacion(dto);

        Reserva reserva = reservaRepository.findById(dto.getIdReserva())
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con ID: " + dto.getIdReserva()));

        Cliente cliente = clienteRepository.findById(dto.getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + dto.getIdCliente()));

        Cancelacion entidad = toEntity(dto, reserva, cliente);
        entidad.setIdCancelacion(null);
        entidad.setEstado(Boolean.TRUE);

        return convertToDTO(cancelacionRepository.save(entidad));
    }

    @Override
    public CancelacionDTO actualizarCancelacion(Long id, @Valid CancelacionDTO dto) {
        Cancelacion existente = cancelacionRepository.findById(id)
                .filter(Cancelacion::getEstado)
                .orElseThrow(() -> new RuntimeException("Cancelación no encontrada o inactiva con ID: " + id));

        cancelacionValidator.validarCancelacion(dto);

        Reserva reserva = reservaRepository.findById(dto.getIdReserva())
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con ID: " + dto.getIdReserva()));

        Cliente cliente = clienteRepository.findById(dto.getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + dto.getIdCliente()));

        existente.setMotivo(dto.getMotivo());
        existente.setFechaCancelacion(dto.getFechaCancelacion());
        existente.setHoraCancelacion(dto.getHoraCancelacion());
        existente.setEstado(dto.getEstado());
        existente.setReserva(reserva);
        existente.setCliente(cliente);

        return convertToDTO(cancelacionRepository.save(existente));
    }

    @Override
    public CancelacionDTO eliminarCancelacion(Long id) {
        Cancelacion existente = cancelacionRepository.findById(id)
                .filter(Cancelacion::getEstado)
                .orElseThrow(() -> new RuntimeException("Cancelación no encontrada o inactiva con ID: " + id));
        existente.setEstado(Boolean.FALSE);
        return convertToDTO(cancelacionRepository.save(existente));
    }

    @Override
    public void eliminarCancelacionFisicamente(Long id) {
        Cancelacion existente = cancelacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cancelación no encontrada con ID: " + id));
        cancelacionRepository.delete(existente);
    }

    @Override
    public Cancelacion obtenerCancelacionConBloqueo(Long id) {
        Cancelacion cancelacion = cancelacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cancelación no encontrada con ID: " + id));
        try {
            Thread.sleep(15000); // Simula espera
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return cancelacion;
    }

    // ---------- mapping ----------
    private CancelacionDTO convertToDTO(Cancelacion c) {
        return CancelacionDTO.builder()
                .idCancelacion(c.getIdCancelacion())
                .motivo(c.getMotivo())
                .fechaCancelacion(c.getFechaCancelacion())
                .horaCancelacion(c.getHoraCancelacion())
                .estado(c.getEstado())
                .idReserva(c.getReserva() != null ? c.getReserva().getIdReserva() : null)
                .idCliente(c.getCliente() != null ? c.getCliente().getId() : null)
                .build();
    }

    private Cancelacion toEntity(CancelacionDTO d, Reserva reserva, Cliente cliente) {
        return Cancelacion.builder()
                .idCancelacion(d.getIdCancelacion())
                .motivo(d.getMotivo())
                .fechaCancelacion(d.getFechaCancelacion())
                .horaCancelacion(d.getHoraCancelacion())
                .estado(d.getEstado() != null ? d.getEstado() : Boolean.TRUE)
                .reserva(reserva)
                .cliente(cliente)
                .build();
    }
}