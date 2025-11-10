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
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import java.nio.file.Path;
import java.nio.file.Files;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

import java.time.LocalDateTime;
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
    public QrDTO generarQrParaReserva(Long idReserva) {
        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con ID: " + idReserva));

                // 1️⃣ Definir información dentro del QR
                String nombreCliente = reserva.getCliente() != null ? reserva.getCliente().getNombre() + " " + reserva.getCliente().getApellidoPaterno() + " " + reserva.getCliente().getApellidoMaterno() : "";
                String nombreCancha = reserva.getCancha() != null ? reserva.getCancha().getNombre() : "";
                Double montoTotal = 0.0;
                if (reserva.getIncluidos() != null && !reserva.getIncluidos().isEmpty() && reserva.getIncluidos().get(0) != null) {
                        try {
                                montoTotal = reserva.getIncluidos().get(0).getMontoTotal() != null ? reserva.getIncluidos().get(0).getMontoTotal() : 0.0;
                        } catch (Exception ignored) {
                                montoTotal = 0.0;
                        }
                }

                String contenido = String.format(
                                "Reserva #%d\nCliente: %s\nCancha: %s\nFecha: %s\nMonto Total: %.2f Bs",
                                reserva.getIdReserva(),
                                nombreCliente,
                                nombreCancha,
                                reserva.getFechaReserva(),
                                montoTotal
                );

        try {
            // Generar imagen QR con ZXing
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            var bitMatrix = qrCodeWriter.encode(contenido, BarcodeFormat.QR_CODE, 300, 300);

            Path folder = Path.of("src/main/resources/static/qr");
            if (!Files.exists(folder)) Files.createDirectories(folder);

            String nombreArchivo = "qr_" + idReserva + "_" + UUID.randomUUID() + ".png";
            Path rutaArchivo = folder.resolve(nombreArchivo);
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", rutaArchivo);

            // 3️⃣ Crear entidad QR
            Qr qr = Qr.builder()
                    .codigoQr(nombreArchivo)
                    .urlQr("/qr/" + nombreArchivo)
                    .fechaGeneracion(LocalDateTime.now())
                    .fechaExpiracion(LocalDateTime.now().plusDays(7))
                    .estado(true)
                    .descripcion("QR para ingreso - Reserva #" + idReserva)
                    .reserva(reserva)
                    .invitado(reserva.getCliente()) // o un invitado específico si aplica
                    .usuarioControl(reserva.getCliente()) // o usuarioControl real
                    .build();

            qrRepository.save(qr);

            // 4️⃣ Devolver DTO
            return QrDTO.builder()
                    .idQr(qr.getIdQr())
                    .codigoQr(nombreArchivo)
                    .urlQr("/qr/" + nombreArchivo)
                    .fechaGeneracion(qr.getFechaGeneracion())
                    .fechaExpiracion(qr.getFechaExpiracion())
                    .estado(qr.getEstado())
                    .descripcion(qr.getDescripcion())
                    .idReserva(reserva.getIdReserva())
                    .idInvitado(qr.getInvitado().getId())
                    .idUsuarioControl(qr.getUsuarioControl().getId())
                    .build();

        } catch (WriterException | java.io.IOException e) {
            throw new RuntimeException("Error generando código QR: " + e.getMessage(), e);
        }
    }

    

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