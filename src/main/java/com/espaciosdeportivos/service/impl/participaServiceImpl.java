package com.espaciosdeportivos.service.impl;

import com.espaciosdeportivos.dto.ParticipaDTO;
import com.espaciosdeportivos.model.*;
import com.espaciosdeportivos.repository.ParticipaRepository;
import com.espaciosdeportivos.repository.IncluyeRepository;
import com.espaciosdeportivos.repository.InvitadoRepository;
import com.espaciosdeportivos.repository.ReservaRepository;
import com.espaciosdeportivos.service.IparticipaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
@Transactional
@RequiredArgsConstructor
public class ParticipaServiceImpl implements IparticipaService {

    private final ParticipaRepository participaRepository;
    private final InvitadoRepository invitadoRepository;
    private final ReservaRepository reservaRepository;
    private final com.espaciosdeportivos.service.IQrService qrService;
    private final IncluyeRepository incluyeRepository;
    private static final Logger log = LoggerFactory.getLogger(Participa.class);





    @Override
    @Transactional
    public ParticipaDTO crear(ParticipaDTO participaDTO) {
        // Validar que no exista ya la invitación
        if (participaRepository.existsByInvitadoIdAndReservaIdReserva(
            participaDTO.getIdInvitado(), participaDTO.getIdReserva())) {
            throw new IllegalArgumentException("El invitado ya está invitado a esta reserva");
        }

        Reserva reserva = reservaRepository.findById(participaDTO.getIdReserva())
                .orElseThrow(() -> new EntityNotFoundException("Reserva no encontrada"));
        
        Invitado invitado = invitadoRepository.findById(participaDTO.getIdInvitado())
                .orElseThrow(() -> new EntityNotFoundException("Invitado no encontrado"));

        // Validar que la reserva esté en estado válido para invitar
        if (!reserva.esModificable()) {
            throw new IllegalArgumentException("No se pueden agregar invitados a una reserva completada o cancelada");
        }

        // Validar capacidad: solo se permiten (capacidad - 1) invitados (cliente ocupa 1)
        Cancha cancha = reserva.getCancha();
        if (cancha == null) {
            throw new IllegalStateException("Reserva sin cancha asociada");
        }
        int capacidad = cancha.getCapacidad() != null ? cancha.getCapacidad() : 0;
        int maxInvitados = Math.max(0, capacidad - 1);
        Long invitadosRegistrados = participaRepository.countByReservaIdReserva(reserva.getIdReserva());
        if (invitadosRegistrados != null && invitadosRegistrados >= maxInvitados) {
            throw new IllegalArgumentException("Capacidad de invitados alcanzada para esta reserva");
        }

        // Crear la entidad participa (cambiando el nombre de la variable para evitar ambigüedad)
        Participa nuevaParticipacion = Participa.crear(invitado, reserva);
        
        // Setear campos opcionales del DTO
        if (participaDTO.getObservaciones() != null) {
            nuevaParticipacion.setObservaciones(participaDTO.getObservaciones());
        }

        participaRepository.save(nuevaParticipacion);
        return mapToDTO(nuevaParticipacion);
    }

    @Override
    @Transactional
    public ParticipaDTO actualizar(Long idReserva, Long idInvitado, ParticipaDTO participaDTO) {
        Participa participacionExistente = obtenerParticipa(idReserva, idInvitado);
        
        // Actualizar solo los campos permitidos
        if (participaDTO.getAsistio() != null) {
            participacionExistente.setAsistio(participaDTO.getAsistio());
        }
        if (participaDTO.getConfirmado() != null) {
            participacionExistente.setConfirmado(participaDTO.getConfirmado());
        }
        if (participaDTO.getNotificado() != null) {
            participacionExistente.setNotificado(participaDTO.getNotificado());
        }
        if (participaDTO.getObservaciones() != null) {
            participacionExistente.setObservaciones(participaDTO.getObservaciones());
        }

        participaRepository.save(participacionExistente);
        return mapToDTO(participacionExistente);
    }

    @Override
    @Transactional
    public ParticipaDTO confirmarInvitacion(Long idReserva, Long idInvitado) {
        Participa participacion = obtenerParticipa(idReserva, idInvitado);
        
        Reserva reserva = participacion.getReserva();
        if (!reserva.estaActiva()) {
            throw new IllegalArgumentException("No se puede confirmar invitación a una reserva no activa");
        }

        // Antes de confirmar, validar capacidad de asistentes confirmados
        Cancha cancha = reserva.getCancha();
        if (cancha == null) {
            throw new IllegalStateException("Reserva sin cancha asociada");
        }
        int capacidad = cancha.getCapacidad() != null ? cancha.getCapacidad() : 0;
        int maxInvitados = Math.max(0, capacidad - 1);
        Long invitadosConfirmados = participaRepository.countByReservaIdReservaAndConfirmado(idReserva, Boolean.TRUE);
        if (invitadosConfirmados != null && invitadosConfirmados >= maxInvitados) {
            throw new IllegalArgumentException("No se puede confirmar: capacidad de invitados alcanzada");
        }

        participacion.setConfirmado(true);
        participacion.setNotificado(true);
        participaRepository.save(participacion);

        // Generar QR para el invitado confirmado (no bloquear la transacción si falla)
        try {
            qrService.generarQrParaReserva(idReserva, idInvitado);
        } catch (Exception e) {
            log.error("Error generando QR tras confirmación de invitado {} en reserva {}", idInvitado, idReserva, e);
        }
        // Actualizar contador de invitados confirmados en Incluye
        try {
            var optIncluye = incluyeRepository.findByReservaIdReserva(idReserva);
            if (optIncluye.isPresent()) {
                Incluye incluye = optIncluye.get();
                if (incluye.getInvitadosConfirmados() == null) incluye.setInvitadosConfirmados(0);
                incluye.setInvitadosConfirmados(incluye.getInvitadosConfirmados() + 1);
                // guardar
                incluyeRepository.save(incluye);
            }
        } catch (Exception e) {
            log.warn("No se pudo actualizar invitadosConfirmados en Incluye para reserva {}: {}", idReserva, e.getMessage());
        }
        return mapToDTO(participacion);
    }

    @Override
    @Transactional
    public ParticipaDTO registrarAsistencia(Long idReserva, Long idInvitado, Boolean asistio) {
        Participa participacion = obtenerParticipa(idReserva, idInvitado);
        participacion.setAsistio(asistio);
        participaRepository.save(participacion);
        return mapToDTO(participacion);
    }

    @Override
    @Transactional
    public ParticipaDTO marcarComoNotificado(Long idReserva, Long idInvitado) {
        Participa participacion = obtenerParticipa(idReserva, idInvitado);
        participacion.setNotificado(true);
        participaRepository.save(participacion);
        return mapToDTO(participacion);
    }

    @Override
    @Transactional
    public void eliminar(Long idReserva, Long idInvitado) {
        Participa participacion = obtenerParticipa(idReserva, idInvitado);
        participaRepository.delete(participacion);
    }

    // Métodos de consulta (readOnly = true)
    @Override
    @Transactional(readOnly = true)
    public List<ParticipaDTO> findByReservaIdReserva(Long idReserva) {
        return participaRepository.findByReservaIdReserva(idReserva).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipaDTO> findByReservaIdReservaAndConfirmado(Long idReserva, Boolean confirmado) {
        return participaRepository.findByReservaIdReservaAndConfirmado(idReserva, confirmado).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipaDTO> findByInvitadoId(Long idInvitado) {
        return participaRepository.findByInvitadoId(idInvitado).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipaDTO> findInvitadosConfirmadosPorReserva(Long idReserva) {
        return participaRepository.findInvitadosConfirmadosPorReserva(idReserva).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipaDTO> findReservasActivasPorInvitado(Long idInvitado) {
        return participaRepository.findReservasActivasPorInvitado(idInvitado).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ParticipaDTO findByIds(Long idReserva, Long idInvitado) {
        Participa participacion = obtenerParticipa(idReserva, idInvitado);
        return mapToDTO(participacion);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByInvitadoIdAndReservaIdReserva(Long idInvitado, Long idReserva) {
        return participaRepository.existsByInvitadoIdAndReservaIdReserva(idInvitado, idReserva);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countByReservaIdReserva(Long idReserva) {
        return participaRepository.countByReservaIdReserva(idReserva);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countByReservaIdReservaAndAsistio(Long idReserva, Boolean asistio) {
        return participaRepository.countByReservaIdReservaAndAsistio(idReserva, asistio);
    }

    private Participa obtenerParticipa(Long idReserva, Long idInvitado) {
        ParticipaId id = new ParticipaId(idInvitado, idReserva);
        return participaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                    "Invitación no encontrada para reserva: " + idReserva + " e invitado: " + idInvitado));
    }

    private ParticipaDTO mapToDTO(Participa p) {
        Invitado invitado = p.getInvitado();
        Reserva reserva = p.getReserva();

        return ParticipaDTO.builder()
                .idInvitado(invitado.getId())
                .idReserva(reserva.getIdReserva())
                .asistio(p.getAsistio())
                .confirmado(p.getConfirmado())
                .notificado(p.getNotificado())
                .observaciones(p.getObservaciones())
                .build();
    }
}