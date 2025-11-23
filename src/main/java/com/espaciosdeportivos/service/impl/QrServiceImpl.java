package com.espaciosdeportivos.service.impl;

import com.espaciosdeportivos.dto.QrDTO;
import com.espaciosdeportivos.model.Qr;
import com.espaciosdeportivos.model.Reserva;
import com.espaciosdeportivos.model.Invitado;
import com.espaciosdeportivos.model.UsuarioControl;
import com.espaciosdeportivos.repository.QrRepository;
import com.espaciosdeportivos.repository.ReservaRepository;
import com.espaciosdeportivos.repository.InvitadoRepository;
import com.espaciosdeportivos.repository.UsuarioControlRepository;
import com.espaciosdeportivos.service.IQrService;
import com.espaciosdeportivos.validation.QrValidator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class QrServiceImpl implements IQrService {

    private final QrRepository qrRepository;
    private final ReservaRepository reservaRepository;
    private final InvitadoRepository invitadoRepository;
    private final UsuarioControlRepository usuarioControlRepository;
    private final QrValidator qrValidator;

    @Override
    @Transactional(readOnly = true)
    public List<QrDTO> obtenerTodosLosQrs() {
        return qrRepository.findByEstadoTrue()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public QrDTO obtenerQrPorId(Long id) {
        Qr qr = qrRepository.findByIdQrAndEstadoTrue(id)
                .orElseThrow(() -> new RuntimeException("QR no encontrado con ID: " + id));
        return convertToDTO(qr);
    }

    @Override
    public QrDTO crearQr(@Valid QrDTO dto) {
        qrValidator.validarQr(dto);

        Reserva reserva = reservaRepository.findById(dto.getIdReserva())
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con ID: " + dto.getIdReserva()));

        Invitado invitado = invitadoRepository.findById(dto.getIdInvitado())
                .orElseThrow(() -> new RuntimeException("Invitado no encontrado con ID: " + dto.getIdInvitado()));

        UsuarioControl usuarioControl = usuarioControlRepository.findById(dto.getIdUsuarioControl())
                .orElseThrow(() -> new RuntimeException("UsuarioControl no encontrado con ID: " + dto.getIdUsuarioControl()));

        Qr entidad = toEntity(dto, reserva, invitado, usuarioControl);
        entidad.setIdQr(null);
        entidad.setEstado(Boolean.TRUE);

        return convertToDTO(qrRepository.save(entidad));
    }

    @Override
    public QrDTO actualizarQr(Long id, @Valid QrDTO dto) {
        Qr existente = qrRepository.findByIdQrAndEstadoTrue(id)
                .orElseThrow(() -> new RuntimeException("QR no encontrado con ID: " + id));

        qrValidator.validarQr(dto);

        Reserva reserva = reservaRepository.findById(dto.getIdReserva())
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con ID: " + dto.getIdReserva()));

        Invitado invitado = invitadoRepository.findById(dto.getIdInvitado())
                .orElseThrow(() -> new RuntimeException("Invitado no encontrado con ID: " + dto.getIdInvitado()));

        UsuarioControl usuarioControl = usuarioControlRepository.findById(dto.getIdUsuarioControl())
                .orElseThrow(() -> new RuntimeException("UsuarioControl no encontrado con ID: " + dto.getIdUsuarioControl()));

        existente.setCodigoQr(dto.getCodigoQr());
        existente.setFechaGeneracion(dto.getFechaGeneracion());
        existente.setFechaExpiracion(dto.getFechaExpiracion());
        existente.setEstado(dto.getEstado());
        existente.setDescripcion(dto.getDescripcion());
        existente.setReserva(reserva);
        existente.setInvitado(invitado);
        existente.setUsuarioControl(usuarioControl);

        return convertToDTO(qrRepository.save(existente));
    }

    @Override
    public QrDTO eliminarQr(Long id) {
        Qr existente = qrRepository.findByIdQrAndEstadoTrue(id)
                .orElseThrow(() -> new RuntimeException("QR no encontrado con ID: " + id));
        existente.setEstado(Boolean.FALSE);
        return convertToDTO(qrRepository.save(existente));
    }

    @Override
    public void eliminarQrFisicamente(Long id) {
        Qr existente = qrRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("QR no encontrado con ID: " + id));
        qrRepository.delete(existente);
    }

    //k pa front
    @Override
        public List<QrDTO> obtenerQrsPorReserva(Long idReserva) {
        List<Qr> qrs = qrRepository.findByReservaIdReserva(idReserva);
        return qrs.stream().map(this::convertToDto).collect(Collectors.toList());
        }



    @Override
    public Qr obtenerQrConBloqueo(Long id) {
        Qr qr = qrRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("QR no encontrado con ID: " + id));
        try {
            Thread.sleep(15000); // Simula espera
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return qr;
    }

    // ---------- mapping ----------
    private QrDTO convertToDTO(Qr qr) {
        return QrDTO.builder()
                .idQr(qr.getIdQr())
                .codigoQr(qr.getCodigoQr())
                .fechaGeneracion(qr.getFechaGeneracion())
                .fechaExpiracion(qr.getFechaExpiracion())
                .estado(qr.getEstado())
                .descripcion(qr.getDescripcion())
                .idReserva(qr.getReserva() != null ? qr.getReserva().getIdReserva() : null)
                .idInvitado(qr.getInvitado() != null ? qr.getInvitado().getId() : null)
                .idUsuarioControl(qr.getUsuarioControl() != null ? qr.getUsuarioControl().getId() : null)
                .build();
    }

    private Qr toEntity(QrDTO dto, Reserva reserva, Invitado invitado, UsuarioControl usuarioControl) {
        return Qr.builder()
                .idQr(dto.getIdQr())
                .codigoQr(dto.getCodigoQr())
                .fechaGeneracion(dto.getFechaGeneracion())
                .fechaExpiracion(dto.getFechaExpiracion())
                .estado(dto.getEstado() != null ? dto.getEstado() : Boolean.TRUE)
                .descripcion(dto.getDescripcion())
                .reserva(reserva)
                .invitado(invitado)
                .usuarioControl(usuarioControl)
                .build();
    }

    //k pa front
   private QrDTO convertToDto(Qr qr) {
    return QrDTO.builder()
        .idQr(qr.getIdQr())
        .codigoQr(qr.getCodigoQr())
        .fechaGeneracion(qr.getFechaGeneracion())
        .fechaExpiracion(qr.getFechaExpiracion())
        .estado(qr.getEstado())
        .descripcion(qr.getDescripcion())
        .idUsuarioControl(qr.getUsuarioControl() != null ? qr.getUsuarioControl().getId() : null)
        .idReserva(qr.getReserva() != null ? qr.getReserva().getIdReserva() : null)
        .idInvitado(qr.getInvitado() != null ? qr.getInvitado().getId() : null)
        .build();
}


}