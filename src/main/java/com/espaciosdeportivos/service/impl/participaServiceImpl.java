package com.espaciosdeportivos.service.impl;

import com.espaciosdeportivos.dto.participaDTO;
import com.espaciosdeportivos.model.*;
import com.espaciosdeportivos.repository.participaRepository;
import com.espaciosdeportivos.repository.InvitadoRepository;
import com.espaciosdeportivos.repository.ReservaRepository;
import com.espaciosdeportivos.service.IparticipaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class participaServiceImpl implements IparticipaService {

    private final participaRepository participaRepository;
    private final InvitadoRepository invitadoRepository;
    private final ReservaRepository reservaRepository;

    @Override
    @Transactional
    public participaDTO crear(participaDTO participaDTO) {
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

        // Crear la entidad participa (cambiando el nombre de la variable para evitar ambigüedad)
        participa nuevaParticipacion = participa.crear(invitado, reserva);
        
        // Setear campos opcionales del DTO
        if (participaDTO.getObservaciones() != null) {
            nuevaParticipacion.setObservaciones(participaDTO.getObservaciones());
        }

        participaRepository.save(nuevaParticipacion);
        return mapToDTO(nuevaParticipacion);
    }

    @Override
    @Transactional
    public participaDTO actualizar(Long idReserva, Long idInvitado, participaDTO participaDTO) {
        participa participacionExistente = obtenerParticipa(idReserva, idInvitado);
        
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
    public participaDTO confirmarInvitacion(Long idReserva, Long idInvitado) {
        participa participacion = obtenerParticipa(idReserva, idInvitado);
        
        Reserva reserva = participacion.getReserva();
        if (!reserva.estaActiva()) {
            throw new IllegalArgumentException("No se puede confirmar invitación a una reserva no activa");
        }

        participacion.setConfirmado(true);
        participaRepository.save(participacion);
        return mapToDTO(participacion);
    }

    @Override
    @Transactional
    public participaDTO registrarAsistencia(Long idReserva, Long idInvitado, Boolean asistio) {
        participa participacion = obtenerParticipa(idReserva, idInvitado);
        participacion.setAsistio(asistio);
        participaRepository.save(participacion);
        return mapToDTO(participacion);
    }

    @Override
    @Transactional
    public participaDTO marcarComoNotificado(Long idReserva, Long idInvitado) {
        participa participacion = obtenerParticipa(idReserva, idInvitado);
        participacion.setNotificado(true);
        participaRepository.save(participacion);
        return mapToDTO(participacion);
    }

    @Override
    @Transactional
    public void eliminar(Long idReserva, Long idInvitado) {
        participa participacion = obtenerParticipa(idReserva, idInvitado);
        participaRepository.delete(participacion);
    }

    // Métodos de consulta (readOnly = true)
    @Override
    @Transactional(readOnly = true)
    public List<participaDTO> findByReservaIdReserva(Long idReserva) {
        return participaRepository.findByReservaIdReserva(idReserva).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<participaDTO> findByReservaIdReservaAndConfirmado(Long idReserva, Boolean confirmado) {
        return participaRepository.findByReservaIdReservaAndConfirmado(idReserva, confirmado).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<participaDTO> findByInvitadoId(Long idInvitado) {
        return participaRepository.findByInvitadoId(idInvitado).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<participaDTO> findInvitadosConfirmadosPorReserva(Long idReserva) {
        return participaRepository.findInvitadosConfirmadosPorReserva(idReserva).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<participaDTO> findReservasActivasPorInvitado(Long idInvitado) {
        return participaRepository.findReservasActivasPorInvitado(idInvitado).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public participaDTO findByIds(Long idReserva, Long idInvitado) {
        participa participacion = obtenerParticipa(idReserva, idInvitado);
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

    private participa obtenerParticipa(Long idReserva, Long idInvitado) {
        participaId id = new participaId(idInvitado, idReserva);
        return participaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                    "Invitación no encontrada para reserva: " + idReserva + " e invitado: " + idInvitado));
    }

    private participaDTO mapToDTO(participa p) {
        Invitado invitado = p.getInvitado();
        Reserva reserva = p.getReserva();

        return participaDTO.builder()
                .idInvitado(invitado.getId())
                .idReserva(reserva.getIdReserva())
                .nombreInvitado(invitado.getNombre() + " " + 
                               (invitado.getApellidoPaterno() != null ? invitado.getApellidoPaterno() + " " : "") + 
                               invitado.getApellidoMaterno())
                .emailInvitado(invitado.getEmail())
                .telefonoInvitado(invitado.getTelefono())
                .verificadoInvitado(invitado.getVerificado())
                .fechaInvitacion(p.getFechaInvitacion())
                .asistio(p.getAsistio())
                .confirmado(p.getConfirmado())
                .notificado(p.getNotificado())
                .observaciones(p.getObservaciones())
                .codigoReserva(reserva.getCodigoReserva())
                .estadoReserva(reserva.getEstadoReserva())
                .build();
    }
}