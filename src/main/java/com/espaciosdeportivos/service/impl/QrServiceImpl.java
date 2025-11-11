package com.espaciosdeportivos.service.impl;

import com.espaciosdeportivos.dto.QrDTO;
import com.espaciosdeportivos.model.Qr;
import com.espaciosdeportivos.model.Reserva;
import com.espaciosdeportivos.model.Cliente;
import com.espaciosdeportivos.model.Invitado;
import com.espaciosdeportivos.model.Persona;
import com.espaciosdeportivos.model.UsuarioControl;
import com.espaciosdeportivos.repository.QrRepository;
import com.espaciosdeportivos.repository.ReservaRepository;
import com.espaciosdeportivos.repository.ClienteRepository;
import com.espaciosdeportivos.repository.InvitadoRepository;
import com.espaciosdeportivos.repository.PersonaRepository;
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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Map;
import java.util.LinkedHashMap;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class QrServiceImpl implements IQrService {

    private final QrRepository qrRepository;
    private final ReservaRepository reservaRepository;
    private final InvitadoRepository invitadoRepository;
    private final UsuarioControlRepository usuarioControlRepository;
    private final QrValidator qrValidator;
    private final PersonaRepository personaRepository;
    private final ClienteRepository clienteRepository;


    @Override
    public QrDTO generarQrParaReserva(Long idReserva, Long idPersona) {
        // 1️⃣ Buscar la reserva
        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con ID: " + idReserva));

        // 2️⃣ Buscar la persona (puede ser Cliente o Invitado)
        Persona persona = personaRepository.findById(idPersona)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada con ID: " + idPersona));

        // 3️⃣ Verificar que sea Cliente o Invitado
        boolean esCliente = persona instanceof Cliente;
        boolean esInvitado = persona instanceof Invitado;

        if (!esCliente && !esInvitado) {
                throw new IllegalArgumentException("Solo los clientes o invitados pueden tener un QR.");
        }

        // 4️⃣ Obtener el nombre completo
        String nombrePersona = persona.getNombre() + " " + persona.getApellidoPaterno() + " " + persona.getApellidoMaterno();

        // 5️⃣ Obtener datos de la reserva
        String nombreCliente = reserva.getCliente() != null 
                ? reserva.getCliente().getNombre() + " " + reserva.getCliente().getApellidoPaterno() + " " + reserva.getCliente().getApellidoMaterno() 
                : "";

        String nombreCancha = reserva.getCancha() != null ? reserva.getCancha().getNombre() : "";

        Double montoTotal = 0.0;
        if (reserva.getIncluidos() != null && !reserva.getIncluidos().isEmpty() && reserva.getIncluidos().get(0) != null) {
                try {
                montoTotal = reserva.getIncluidos().get(0).getMontoTotal() != null 
                        ? reserva.getIncluidos().get(0).getMontoTotal() 
                        : 0.0;
                } catch (Exception ignored) {
                montoTotal = 0.0;
                }
        }

                // Generar contenido del QR como JSON
                Map<String, Object> payload = new LinkedHashMap<>();
                payload.put("nombreReservador", nombreCliente);
                payload.put("nombreParticipante", nombrePersona);
                payload.put("nombreCancha", nombreCancha);
                payload.put("idReserva", reserva.getIdReserva());
                payload.put("horaInicio", reserva.getHoraInicio());
                //aqui podemos poner mas cosas como ubicacion pero hayq ue relacioanr
                payload.put("horaFin", reserva.getHoraFin());

                if (reserva.getCancha() != null) {
                        try {
                                payload.put("canchaId", reserva.getCancha().getIdCancha());
                        } catch (Exception ignored) {
                                payload.put("canchaId", null);
                        }
                } else {
                        payload.put("canchaId", null);
                }

                payload.put("fechaReserva", reserva.getFechaReserva() != null ? reserva.getFechaReserva().toString() : "");
                payload.put("montoTotal", montoTotal);


                ObjectMapper mapper = new ObjectMapper();
                String contenido;
                try {
                        contenido = mapper.writeValueAsString(payload);
                } catch (JsonProcessingException e) {
                        // Si falla 
                        log.warn("QRService: no se pudo serializar payload a JSON para reserva {} persona {}: {}", idReserva, idPersona, e.getMessage());
                        contenido = String.format(
                                "{\"reservaId\":%d, \"nombreReservador\":\"%s\", \"nombreParticipante\":\"%s\", " +
                                "\"nombreCancha\":\"%s\", \"horaInicio\":\"%s\", \"horaFin\":\"%s\", " +
                                "\"fechaReserva\":\"%s\", \"montoTotal\":%.2f}",
                                reserva.getIdReserva(),
                                nombreCliente != null ? nombreCliente : "",
                                nombrePersona != null ? nombrePersona : "",
                                nombreCancha != null ? nombreCancha : "",
                                reserva.getHoraInicio() != null ? reserva.getHoraInicio().toString() : "",
                                reserva.getHoraFin() != null ? reserva.getHoraFin().toString() : "",
                                reserva.getFechaReserva() != null ? reserva.getFechaReserva().toString() : "",
                                montoTotal
                        );
                }

                try {
                        // Si ya existe un QR para esta reserva y persona, no generar uno nuevo
                        if (qrRepository.existsByReserva_IdReservaAndPersona_Id(idReserva, idPersona)) {
                                // Devolver el primer QR existente como DTO
                                List<Qr> existing = qrRepository.findByReserva_IdReserva(idReserva).stream()
                                                .filter(q -> q.getPersona() != null && q.getPersona().getId().equals(idPersona))
                                                .toList();
                                if (!existing.isEmpty()) {
                                        return convertToDTO(existing.get(0));
                                }
                        }
                        log.info("QRService: generarQrParaReserva idReserva={} idPersona={}", idReserva, idPersona);
                        // 7️⃣ Generar imagen QR con ZXing
                QRCodeWriter qrCodeWriter = new QRCodeWriter();
                var bitMatrix = qrCodeWriter.encode(contenido, BarcodeFormat.QR_CODE, 300, 300);

                Path folder = Path.of("uploads", "img", "qr"); // Carpeta relativa
                if (!Files.exists(folder)) Files.createDirectories(folder);

                String nombreArchivo = "qr_" + idReserva + "_" + idPersona + "_" + UUID.randomUUID().toString().substring(0, 8) + ".png";
                Path rutaArchivo = folder.resolve(nombreArchivo);
                MatrixToImageWriter.writeToPath(bitMatrix, "PNG", rutaArchivo);
                log.info("QRService: imagen escrita en {}", rutaArchivo.toAbsolutePath());

                // 8️⃣ Crear entidad QR
                Qr qr = Qr.builder()
                        .codigoQr(nombreArchivo)
                        .urlQr("/uploads/img/qr/" + nombreArchivo) // Ruta para acceder
                        .fechaGeneracion(LocalDateTime.now())
                        .fechaExpiracion(LocalDateTime.now().plusDays(7)) // 7 días de expiración
                        .estado(true)
                        .descripcion("QR para ingreso - Reserva #" + idReserva + " - Persona: " + nombrePersona)
                        .reserva(reserva)
                        .persona(persona) // ✅ Ahora sí está definida
                        .esCliente(esCliente) // ✅ Asignar si es cliente o no
                        .usuarioControl(reserva.getCliente()) // Quién generó (el cliente que hizo la reserva)
                        .build();

                qrRepository.save(qr);
                log.info("QRService: registro QR guardado id={} codigo={} personaId={}", qr.getIdQr(), qr.getCodigoQr(), persona.getId());

                // 9️⃣ Devolver DTO
                return QrDTO.builder()
                        .idQr(qr.getIdQr())
                        .codigoQr(qr.getCodigoQr())
                        .urlQr(qr.getUrlQr())
                        .fechaGeneracion(qr.getFechaGeneracion())
                        .fechaExpiracion(qr.getFechaExpiracion())
                        .estado(qr.getEstado())
                        .descripcion(qr.getDescripcion())
                        .idReserva(reserva.getIdReserva())
                        .idPersona(persona.getId())
                        .esCliente(esCliente) // Valor booleano
                        .idUsuarioControl(qr.getUsuarioControl() != null ? qr.getUsuarioControl().getId() : null)
                        .build();

        } catch (WriterException | java.io.IOException e) {
                // Loguear el error pero no propagar una RuntimeException que pueda marcar la transacción para rollback
                log.error("QRService: error al generar QR para reserva {} persona {}: {}", idReserva, idPersona, e.getMessage(), e);
                return null;
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

        Persona invitado = invitadoRepository.findById(dto.getIdPersona())
                .orElseThrow(() -> new RuntimeException("Invitado no encontrado con ID: " + dto.getIdPersona()));

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

        Persona persona = invitadoRepository.findById(dto.getIdPersona())
                .orElseThrow(() -> new RuntimeException("Invitado no encontrado con ID: " + dto.getIdPersona()));

        UsuarioControl usuarioControl = usuarioControlRepository.findById(dto.getIdUsuarioControl())
                .orElseThrow(() -> new RuntimeException("UsuarioControl no encontrado con ID: " + dto.getIdUsuarioControl()));

        existente.setCodigoQr(dto.getCodigoQr());
        existente.setFechaGeneracion(dto.getFechaGeneracion());
        existente.setFechaExpiracion(dto.getFechaExpiracion());
        existente.setEstado(dto.getEstado());
        existente.setDescripcion(dto.getDescripcion());
        existente.setReserva(reserva);
        existente.setPersona(persona);
        existente.setUsuarioControl(usuarioControl);
        existente.setEsCliente(dto.getEsCliente()); 

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
    /*@Override
        public List<QrDTO> obtenerQrsPorReserva(Long idReserva) {
        List<Qr> qrs = qrRepository.findByReservaIdReserva(idReserva);
        return qrs.stream().map(this::convertToDto).collect(Collectors.toList());
        }*/



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

    //OBTNER QRS

@Override
@Transactional(readOnly = true)
public QrDTO obtenerQrPorCodigo(String codigoQr) {
        Qr qr = qrRepository.findByCodigoQrAndEstadoTrue(codigoQr)
                .orElseThrow(() -> new RuntimeException("QR no encontrado con código: " + codigoQr));
        return convertToDTO(qr);
}

@Override
@Transactional(readOnly = true)
        public List<QrDTO> obtenerQrsPorPersona(Long idPersona) {
        List<Qr> qrs = qrRepository.findByPersona_IdAndEstadoTrue(idPersona);
        return qrs.stream().map(this::convertToDTO).collect(Collectors.toList());
}

@Override
@Transactional(readOnly = true)
        public List<QrDTO> obtenerQrsPorReserva(Long idReserva) {
        List<Qr> qrs = qrRepository.findByReserva_IdReservaAndEstadoTrue(idReserva);
        return qrs.stream().map(this::convertToDTO).collect(Collectors.toList());
}


    // ---------- mapping ----------
    private QrDTO convertToDTO(Qr qr) {
        return QrDTO.builder()
                .idQr(qr.getIdQr())
                .codigoQr(qr.getCodigoQr())
                .fechaGeneracion(qr.getFechaGeneracion())
                .fechaExpiracion(qr.getFechaExpiracion())
                .estado(qr.getEstado())
                .urlQr(qr.getUrlQr())
                .descripcion(qr.getDescripcion())
                .idReserva(qr.getReserva() != null ? qr.getReserva().getIdReserva() : null)
                .idPersona(qr.getPersona() != null ? qr.getPersona().getId() : null)
                .esCliente(qr.getEsCliente())
                //.idInvitado(qr.getInvitado() != null ? qr.getInvitado().getId() : null)
                .idUsuarioControl(qr.getUsuarioControl() != null ? qr.getUsuarioControl().getId() : null)
                .build();
    }

    private Qr toEntity(QrDTO dto, Reserva reserva, Persona persona, UsuarioControl usuarioControl) {
        return Qr.builder()
                .idQr(dto.getIdQr())
                .codigoQr(dto.getCodigoQr())
                .fechaGeneracion(dto.getFechaGeneracion())
                .fechaExpiracion(dto.getFechaExpiracion())
                .urlQr(dto.getUrlQr())
                .estado(dto.getEstado() != null ? dto.getEstado() : Boolean.TRUE)
                .descripcion(dto.getDescripcion())
                .reserva(reserva)
                .persona(persona)
                .esCliente(dto.getEsCliente())
                .usuarioControl(usuarioControl)
                .build();
    }

    //k pa front
   private QrDTO convertToDto(Qr qr) {
    return QrDTO.builder()
        .idQr(qr.getIdQr())
        .codigoQr(qr.getCodigoQr())
        .urlQr(qr.getUrlQr())
        .fechaGeneracion(qr.getFechaGeneracion())
        .fechaExpiracion(qr.getFechaExpiracion())
        .estado(qr.getEstado())
        .esCliente(qr.getEsCliente())
        .descripcion(qr.getDescripcion())
        .idUsuarioControl(qr.getUsuarioControl() != null ? qr.getUsuarioControl().getId() : null)
        .idReserva(qr.getReserva() != null ? qr.getReserva().getIdReserva() : null)
        .build();
}


}