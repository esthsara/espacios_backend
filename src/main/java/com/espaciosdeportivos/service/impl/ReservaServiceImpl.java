package com.espaciosdeportivos.service.impl;

import com.espaciosdeportivos.dto.AreaDeportivaDTO;
import com.espaciosdeportivos.dto.CancelacionDTO;
import com.espaciosdeportivos.dto.CanchaDTO;
import com.espaciosdeportivos.dto.ClienteDTO;
import com.espaciosdeportivos.dto.DisciplinaDTO;
import com.espaciosdeportivos.dto.EquipamientoDTO;
import com.espaciosdeportivos.dto.ImagenDTO;
import com.espaciosdeportivos.dto.PagoDTO;
import com.espaciosdeportivos.dto.ReprogramacionDTO;
import com.espaciosdeportivos.dto.ReservaDTO;
import com.espaciosdeportivos.model.AreaDeportiva;
import com.espaciosdeportivos.model.Cancelacion;
import com.espaciosdeportivos.model.Cancha;
import com.espaciosdeportivos.model.Cliente;
import com.espaciosdeportivos.model.Disciplina;
import com.espaciosdeportivos.model.Dispone;
import com.espaciosdeportivos.model.Incluye;
import com.espaciosdeportivos.model.Pago;
import com.espaciosdeportivos.model.Qr;
import com.espaciosdeportivos.model.Reserva;
import com.espaciosdeportivos.model.Sepractica;
import com.espaciosdeportivos.model.Participa;
import com.espaciosdeportivos.repository.ReservaRepository;
import com.espaciosdeportivos.repository.IncluyeRepository;
import com.espaciosdeportivos.repository.sepracticaRepository;
import com.espaciosdeportivos.repository.AreaDeportivaRepository;
import com.espaciosdeportivos.repository.CancelacionRepository;
import com.espaciosdeportivos.repository.CanchaRepository;
import com.espaciosdeportivos.repository.ClienteRepository;
import com.espaciosdeportivos.repository.DisciplinaRepository;
import com.espaciosdeportivos.repository.PagoRepository;
import com.espaciosdeportivos.repository.QrRepository;
import com.espaciosdeportivos.service.IReservaService;
import com.espaciosdeportivos.validation.ReservaValidator;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.time.Duration;
import java.util.Optional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Comparator;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ReservaServiceImpl implements IReservaService {

    private final ReservaRepository reservaRepository;
    private final CanchaRepository canchaRepository;
    private final ClienteRepository clienteRepository;
    private final DisciplinaRepository disciplinaRepository;
    private final AreaDeportivaRepository areaDeportivaRepository;
    private final PagoRepository pagoRepository;
    private final QrRepository qrRepository;
    private final com.espaciosdeportivos.repository.ParticipaRepository participaRepository;
    private final ReservaValidator reservaValidator;
    private final CancelacionRepository cancelacionRepository;
    private final IncluyeRepository incluyeRepository;



    //listar todas las reservas
    @Override
    @Transactional(readOnly = true)
    public List<ReservaDTO> listarTodas() {
        return reservaRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    //obtener por id
    @Override
    @Transactional(readOnly = true)
    public ReservaDTO obtenerPorId(Long id) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reserva no encontrada con ID: " + id));
        return convertToDTO(reserva);
    }

    //crear reservas
    @Override
    @Transactional
    public ReservaDTO crear(ReservaDTO dto) {
        reservaValidator.validarReserva(dto);
        validarFechaReserva(dto.getFechaReserva());

        if (!validarDisponibilidad(dto.getFechaReserva(), dto.getHoraInicio(), dto.getHoraFin())) {
            throw new IllegalArgumentException("No hay disponibilidad para el horario seleccionado");
        }

        Reserva reserva = convertToEntity(dto);
        
        if (reserva.getEstadoReserva() == null || reserva.getEstadoReserva().isEmpty()) {
            reserva.setEstadoReserva(Reserva.EstadoReserva.PENDIENTE.name());
        }

        return convertToDTO(reservaRepository.save(reserva));
    }

    //actuaalizar reservas
    @Override
    @Transactional
    public ReservaDTO actualizar(Long id, ReservaDTO dto) {
        reservaValidator.validarReserva(dto);
        validarFechaReserva(dto.getFechaReserva());

        Reserva existente = reservaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reserva no encontrada con ID: " + id));

        if (!existente.esModificable()) {
            throw new IllegalArgumentException("No se puede modificar una reserva completada o cancelada");
        }

        if (!existente.getFechaReserva().equals(dto.getFechaReserva()) ||
            !existente.getHoraInicio().equals(dto.getHoraInicio()) ||
            !existente.getHoraFin().equals(dto.getHoraFin())) {
            if (!validarDisponibilidad(dto.getFechaReserva(), dto.getHoraInicio(), dto.getHoraFin())) {
                throw new IllegalArgumentException("No hay disponibilidad para el nuevo horario");
            }
        }

        existente.setFechaReserva(dto.getFechaReserva());
        existente.setHoraInicio(dto.getHoraInicio());
        existente.setHoraFin(dto.getHoraFin());
        existente.setEstadoReserva(dto.getEstadoReserva());
        //existente.setMontoTotal(dto.getMontoTotal());
        existente.setObservaciones(dto.getObservaciones());
        existente.setCliente(clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con ID: " + dto.getClienteId())));

        return convertToDTO(reservaRepository.save(existente));
    }


    //eliminar reserva
    @Override
    @Transactional
    public void eliminar(Long id) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reserva no encontrada con ID: " + id));
        if (reserva.estaActiva()) {
            throw new IllegalArgumentException("No se puede eliminar una reserva activa");
        }
        reservaRepository.deleteById(id);
    }


    ///reservas/horario-disponible
    public List<String> obtenerHorasDisponibles(Long idCancha, LocalDate fecha) {
        Cancha cancha = canchaRepository.findById(idCancha)
                .orElseThrow(() -> new EntityNotFoundException("Cancha no encontrada"));

        AreaDeportiva area = cancha.getAreaDeportiva();
        if (area == null) {
            throw new EntityNotFoundException("Área deportiva no asociada a la cancha");
        }

        LocalTime apertura = area.getHoraInicioArea();
        LocalTime cierre = area.getHoraFinArea();

        // 1️⃣ Obtener reservas de esa cancha en esa fecha
        List<Reserva> reservas = reservaRepository.findByCanchaAndFecha(idCancha, fecha);

        // Ordenar reservas por hora de inicio
        reservas.sort(Comparator.comparing(Reserva::getHoraInicio));

        List<String> horariosDisponibles = new ArrayList<>();
        LocalTime horaActual = apertura;

        // 2️⃣ Recorrer reservas y detectar huecos entre ellas
        for (Reserva reserva : reservas) {
            LocalTime inicioReserva = reserva.getHoraInicio();
            if (horaActual.isBefore(inicioReserva)) {
                // Agregar rango disponible entre horaActual y la siguiente reserva
                horariosDisponibles.add(formatearRango(horaActual, inicioReserva));
            }
            // Actualizar horaActual al final de la reserva
            horaActual = reserva.getHoraFin();
        }

        // 3️⃣ Si después de la última reserva aún hay tiempo libre hasta el cierre
        if (horaActual.isBefore(cierre)) {
            horariosDisponibles.add(formatearRango(horaActual, cierre));
        }

        return ajustarRangosCada30Minutos(horariosDisponibles);
    }

    

    private List<String> ajustarRangosCada30Minutos(List<String> rangos) {
        List<String> bloques30 = new ArrayList<>();

        for (String rango : rangos) {
            String[] partes = rango.split(" - ");
            LocalTime inicio = LocalTime.parse(partes[0]);
            LocalTime fin = LocalTime.parse(partes[1]);

            while (inicio.plusMinutes(30).isBefore(fin) || inicio.plusMinutes(30).equals(fin)) {
                bloques30.add(inicio + " - " + inicio.plusMinutes(30));
                inicio = inicio.plusMinutes(30);
            }
        }
        return bloques30;
    }

    
    private String formatearRango(LocalTime inicio, LocalTime fin) {
        return inicio + " - " + fin;
    }


    @Override
    @Transactional
    public ReservaDTO actualizarEstadoPagoReserva(Long idReserva) {
        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new EntityNotFoundException("Reserva no encontrada con ID: " + idReserva));

        Incluye incluye = incluyeRepository.findByReservaIdReserva(idReserva)
                .orElseThrow(() -> new EntityNotFoundException("Incluye no encontrado para reserva: " + idReserva));

        Double montoTotal = incluye.getMontoTotal();
        Double totalPagado = pagoRepository.sumMontoConfirmadoPorReserva(idReserva);
        if (totalPagado == null) totalPagado = 0.0;

        double saldoPendiente = montoTotal - totalPagado;
        boolean pagadaCompleta = Math.abs(saldoPendiente) <= 0.01;

            reserva.setTotalPagado(totalPagado);
            reserva.setSaldoPendiente(saldoPendiente);
            reserva.setPagadaCompleta(pagadaCompleta);

            // Guardar cambios en la reserva
            reserva = reservaRepository.save(reserva);

            // Si la reserva queda pagada en su totalidad, generar QRs para los invitados confirmados
            if (Boolean.TRUE.equals(reserva.getPagadaCompleta())) {
                try {
                    generarQrParaReserva(reserva);
                } catch (Exception e) {
                    log.warn("Error generando QR(s) para reserva {}: {}", reserva.getIdReserva(), e.getMessage());
                }
            }

            return convertToDTO(reserva);
        }

        // Genera QR(s) PNG y guarda registros en la tabla 'qr'.
        private void generarQrParaReserva(Reserva reserva) throws Exception {
            Long idReserva = reserva.getIdReserva();

            // Obtener invitados confirmados; si no hay ninguno, usaremos al cliente como invitado
            List<Participa> invitados = participaRepository.findInvitadosConfirmadosPorReserva(idReserva);

            if (invitados == null || invitados.isEmpty()) {
                // crear QR para el cliente como invitado si no hay invitados confirmados
                generarYGuardarQr(reserva, reserva.getCliente());
                return;
            }

            // Recoger IDs de invitados que ya tienen QR para esta reserva
            List<Qr> existentes = qrRepository.findByReservaIdReserva(idReserva);
            java.util.Set<Long> invitadosConQr = new java.util.HashSet<>();
            for (Qr q : existentes) {
                if (q.getInvitado() != null) invitadosConQr.add(q.getInvitado().getId());
            }

            for (Participa p : invitados) {
                Long idInv = p.getInvitado().getId();
                if (invitadosConQr.contains(idInv)) continue; // evitar duplicados
                generarYGuardarQr(reserva, p.getInvitado());
            }
        }

        private void generarYGuardarQr(Reserva reserva, com.espaciosdeportivos.model.Persona invitado) throws Exception {
            // Contenido del QR: JSON con datos mínimos para validación en acceso
            String contenido = String.format("{\"reservaId\":%d,\"invitadoId\":%d,\"clienteId\":%d,\"fechaReserva\":\"%s\",\"horaInicio\":\"%s\",\"horaFin\":\"%s\"}",
                    reserva.getIdReserva(),
                    invitado != null ? invitado.getId() : 0L,
                    reserva.getCliente() != null ? reserva.getCliente().getId() : 0L,
                    reserva.getFechaReserva() != null ? reserva.getFechaReserva().toString() : "",
                    reserva.getHoraInicio() != null ? reserva.getHoraInicio().toString() : "",
                    reserva.getHoraFin() != null ? reserva.getHoraFin().toString() : "");

            // Generar código único y ruta de archivo
            String codigo = "RES" + reserva.getIdReserva() + "-INV" + (invitado != null ? invitado.getId() : 0) + "-" + java.util.UUID.randomUUID().toString().substring(0, 8);
            String fileName = codigo + ".png";

            java.nio.file.Path dir = java.nio.file.Paths.get("uploads", "img", "qr");
            java.nio.file.Files.createDirectories(dir);
            java.nio.file.Path filePath = dir.resolve(fileName);

            // Usar ZXing para generar la imagen PNG
            com.google.zxing.BarcodeFormat format = com.google.zxing.BarcodeFormat.QR_CODE;
            int size = 350;
            com.google.zxing.qrcode.decoder.ErrorCorrectionLevel ecLevel = com.google.zxing.qrcode.decoder.ErrorCorrectionLevel.M;
            com.google.zxing.EncodeHintType hintType = com.google.zxing.EncodeHintType.ERROR_CORRECTION;
            java.util.Map<com.google.zxing.EncodeHintType, Object> hints = new java.util.HashMap<>();
            hints.put(hintType, ecLevel);

            com.google.zxing.common.BitMatrix bitMatrix = new com.google.zxing.MultiFormatWriter().encode(contenido, format, size, size, hints);
            java.awt.image.BufferedImage image = com.google.zxing.client.j2se.MatrixToImageWriter.toBufferedImage(bitMatrix);

            javax.imageio.ImageIO.write(image, "PNG", filePath.toFile());

            // Crear registro Qr
            Qr qr = Qr.builder()
                    .codigoQr(codigo)
                    .urlQr("/uploads/img/qr/" + fileName)
                    .fechaGeneracion(java.time.LocalDateTime.now())
                    .fechaExpiracion(reserva.getFechaReserva().atTime(reserva.getHoraFin()).plusHours(6))
                    .estado(true)
                    .descripcion("QR automático generado al confirmarse el pago")
                    .usuarioControl(reserva.getCliente())
                    .invitado(invitado)
                    .reserva(reserva)
                    .build();

            qrRepository.save(qr);
        }

    // ======================
    // BÚSQUEDAS
    // ======================

    @Override
    @Transactional(readOnly = true)
    public List<ReservaDTO> buscarPorCliente(Long idCliente) {
        return reservaRepository.findByClienteId(idCliente).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservaDTO> buscarPorEstado(String estado) {
        return reservaRepository.findByEstadoReserva(estado).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservaDTO> buscarPorRangoFechas(LocalDate inicio, LocalDate fin) {
        return reservaRepository.findByFechaReservaBetween(inicio, fin).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservaDTO> buscarReservasActivasDelCliente(Long clienteId) {
        return reservaRepository.findReservasActivasDelCliente(clienteId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservaDTO> obtenerReservasDelDia(LocalDate fecha) {
        return reservaRepository.findReservasConfirmadasDelDia(fecha).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }



    /*private void generarQrParaReserva(Reserva reserva) {
        // Lógica para generar QR (ejemplo simplificado)
        Qr qr = Qr.builder()
                .reserva(reserva)
                .codigoQr("QR_RESERVA_" + reserva.getIdReserva() + "_" + System.currentTimeMillis())
                .fechaGeneracion(LocalDate.now())
                .build();
        qrRepository.save(qr);
    }*/


    // ======================
    // MAPEO
    // ======================

    private ReservaDTO convertToDTO(Reserva reserva) {
        if (reserva == null) return null;

        Cliente cliente = reserva.getCliente();

        // Crear el DTO básico
        ReservaDTO dto = ReservaDTO.builder()
            .idReserva(reserva.getIdReserva())
            .fechaCreacion(reserva.getFechaCreacion() != null ? reserva.getFechaCreacion() : LocalDateTime.now())
            .fechaReserva(reserva.getFechaReserva())
            .horaInicio(reserva.getHoraInicio())
            .horaFin(reserva.getHoraFin())
            .estadoReserva(reserva.getEstadoReserva())
            //.montoTotal(reserva.getMontoTotal())
            .observaciones(reserva.getObservaciones())
            .clienteId(cliente != null ? cliente.getId() : null)
            .cliente(cliente != null ? convertClienteToDTO(cliente) : null)
            .duracionMinutos(reserva.getDuracionMinutos())
            .build();

        // >>> Cargar valores de pago: priorizar valores en la entidad, si faltan calcularlos
        try {
            Double totalPagado = reserva.getTotalPagado();
            if (totalPagado == null) {
                totalPagado = pagoRepository.sumMontoConfirmadoPorReserva(reserva.getIdReserva());
            }
            dto.setTotalPagado(totalPagado != null ? totalPagado : 0.0);

            Double saldo = reserva.getSaldoPendiente();
            if (saldo == null) {
                // intentar calcular desde incluye
                try {
                    var incluidos = incluyeRepository.findByReservaIdReserva(reserva.getIdReserva());
                    if (!incluidos.isEmpty()) {
                        saldo = incluidos.get().getMontoTotal() - (totalPagado != null ? totalPagado : 0.0);
                    }
                } catch (Exception ignored) {
                    saldo = null;
                }
            }
            dto.setSaldoPendiente(saldo);

            Boolean pagada = reserva.getPagadaCompleta();
            if (pagada == null) {
                pagada = (dto.getSaldoPendiente() != null) && Math.abs(dto.getSaldoPendiente()) <= 0.01;
            }
            dto.setPagadaCompleta(pagada);
        } catch (Exception e) {
            log.warn("No se pudo calcular campos de pago para reserva {}: {}", reserva.getIdReserva(), e.getMessage());
            dto.setTotalPagado(dto.getTotalPagado() == null ? 0.0 : dto.getTotalPagado());
            dto.setSaldoPendiente(dto.getSaldoPendiente());
            dto.setPagadaCompleta(dto.getPagadaCompleta());
        }

        try {
            Optional<Incluye> incluidos = incluyeRepository.findByReservaIdReserva(reserva.getIdReserva());
            if (!incluidos.isEmpty()) {
                //aqui era 0
                Incluye incluye = incluidos.get();
                dto.setCancha(convertCanchaToDTO(incluye.getCancha()));
                dto.setDisciplina(convertDisciplinaToDTO(incluye.getDisciplina()));
            }
        } catch (Exception e) {
            log.warn("Error cargando cancha/disciplina para reserva {}", reserva.getIdReserva(), e);
            dto.setCancha(null);
            dto.setDisciplina(null);
        }

        // >>> Cargar PAGOS <<<
        try {
            List<Pago> pagos = pagoRepository.findByReservaIdReserva(reserva.getIdReserva());
            dto.setPagos(pagos.stream()
                .map(this::convertPagoToDTO)
                .toList());
        } catch (Exception e) {
            log.warn("Error cargando pagos para reserva {}", reserva.getIdReserva(), e);
            dto.setPagos(List.of());
        }

        // >>> Cargar QRs <<<
        /*try {
            List<Qr> qrs = qrRepository.findByReservaIdReserva(reserva.getIdReserva());
            dto.setQrs(qrs.stream()
                .map(this::convertQrToDTO)
                .toList());
        } catch (Exception e) {
            log.warn("Error cargando QRs para reserva {}", reserva.getIdReserva(), e);
            dto.setQrs(List.of());
        }

        // >>> Cargar CANCELACIÓN (0 o 1) <<<
        try {
            Optional<Cancelacion> cancelacionOpt = cancelacionRepository.findByReservaIdReserva(reserva.getIdReserva());
            dto.setCancelacion(cancelacionOpt.map(this::convertCancelacionToDTO).orElse(null));
        } catch (Exception e) {
            log.warn("Error cargando cancelación para reserva {}", reserva.getIdReserva(), e);
            dto.setCancelacion(null);
        }*/

        return dto;
    }

    private Reserva convertToEntity(ReservaDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con ID: " + dto.getClienteId()));
        return Reserva.builder()
                .fechaReserva(dto.getFechaReserva())
                .horaInicio(dto.getHoraInicio())
                .horaFin(dto.getHoraFin())
                .estadoReserva(dto.getEstadoReserva())
                //.montoTotal(dto.getMontoTotal())
                .observaciones(dto.getObservaciones())
                //.codigoReserva(dto.getCodigoReserva())
                .cliente(cliente)
                .build();
    }

    // Mapeo de Cliente como objeto anidado (estilo CanchaServiceImpl)
    private ClienteDTO convertClienteToDTO(Cliente cliente) {
        if (cliente == null) return null;
        return ClienteDTO.builder()
                .id(cliente.getId())
                .nombre(cliente.getNombre())
                .aPaterno(cliente.getApellidoPaterno()) // corregido
                .aMaterno(cliente.getApellidoMaterno())
                .estado(cliente.getEstado())
                .urlImagen(cliente.getUrlImagen())
                .email(cliente.getEmail())
                .telefono(cliente.getTelefono())
                .categoria(cliente.getCategoria())
                .build();
    }


    private CanchaDTO convertCanchaToDTO(Cancha c) {
        return CanchaDTO.builder()
                .idCancha(c.getIdCancha())
                .nombre(c.getNombre())
                .costoHora(c.getCostoHora())
                .capacidad(c.getCapacidad())
                .estado(c.getEstado())
                .mantenimiento(c.getMantenimiento())
                .horaInicio(c.getHoraInicio())
                .horaFin(c.getHoraFin())
                .tipoSuperficie(c.getTipoSuperficie())
                .tamano(c.getTamano())
                .iluminacion(c.getIluminacion())
                .cubierta(c.getCubierta())
                .urlImagen(c.getUrlImagen())
                .idAreadeportiva(c.getAreaDeportiva() != null ? c.getAreaDeportiva().getIdAreaDeportiva() : null    )
                .build();
    }


    private DisciplinaDTO convertDisciplinaToDTO(Disciplina d) {
        return DisciplinaDTO.builder()
                .idDisciplina(d.getIdDisciplina())
                .nombre(d.getNombre())
                .descripcion(d.getDescripcion())
                .estado(d.getEstado())
                .build();
    }

    // Mapeo de Cliente como objeto anidado (estilo CanchaServiceImpl)
    private PagoDTO convertPagoToDTO(Pago pago) {
        Cliente cliente=pago.getCliente();
        return PagoDTO.builder()
            .idPago(pago.getIdPago())
            .monto(pago.getMonto())
            .fecha(pago.getFecha())
            .tipoPago(pago.getTipoPago())
            .metodoPago(pago.getMetodoPago())
            .estado(pago.getEstado())
            .codigoTransaccion(pago.getCodigoTransaccion())
            .descripcion(pago.getDescripcion())
            .idReserva(pago.getReserva().getIdReserva())
            .clienteId(pago.getCliente().getId())
            .cliente(cliente != null ? convertClienteToDTO(cliente) : null)  
            .build();
    }


    
    // ======================
    // GESTIÓN DE ESTADOS
    // ======================

    /*@Override
    @Transactional
    public ReservaDTO confirmarReserva(Long idReserva) {
        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new EntityNotFoundException("Reserva no encontrada con ID: " + idReserva));
        reservaValidator.validarConfirmacion(reserva.getEstadoReserva());
        reserva.setEstadoReserva(Reserva.EstadoReserva.CONFIRMADA.name());
        return convertToDTO(reservaRepository.save(reserva));
    }*/

    /*@Override
    @Transactional
    public ReservaDTO cancelarReserva(Long idReserva, String motivo) {
        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new EntityNotFoundException("Reserva no encontrada con ID: " + idReserva));
        reservaValidator.validarCancelacion(reserva.getEstadoReserva());
        reserva.setEstadoReserva(Reserva.EstadoReserva.CANCELADA.name());
        reserva.setObservaciones("CANCELADA: " + motivo + 
            (reserva.getObservaciones() != null ? ". " + reserva.getObservaciones() : ""));
        return convertToDTO(reservaRepository.save(reserva));
    }*/

    @Override
    @Transactional
    public ReservaDTO marcarComoEnCurso(Long idReserva) {
        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new EntityNotFoundException("Reserva no encontrada con ID: " + idReserva));
        if (!Reserva.EstadoReserva.CONFIRMADA.name().equals(reserva.getEstadoReserva())) {
            throw new IllegalArgumentException("Solo reservas CONFIRMADAS pueden estar EN CURSO");
        }
        reserva.setEstadoReserva(Reserva.EstadoReserva.EN_CURSO.name());
        return convertToDTO(reservaRepository.save(reserva));
    }

    @Override
    @Transactional
    public ReservaDTO marcarComoCompletada(Long idReserva) {
        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new EntityNotFoundException("Reserva no encontrada con ID: " + idReserva));
        if (!Reserva.EstadoReserva.EN_CURSO.name().equals(reserva.getEstadoReserva())) {
            throw new IllegalArgumentException("Solo reservas EN CURSO pueden COMPLETARSE");
        }
        reserva.setEstadoReserva(Reserva.EstadoReserva.COMPLETADA.name());
        return convertToDTO(reservaRepository.save(reserva));
    }

    @Override
    @Transactional
    public ReservaDTO marcarComoNoShow(Long idReserva) {
        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new EntityNotFoundException("Reserva no encontrada con ID: " + idReserva));
        if (!Reserva.EstadoReserva.CONFIRMADA.name().equals(reserva.getEstadoReserva())) {
            throw new IllegalArgumentException("Solo reservas CONFIRMADAS pueden ser NO-SHOW");
        }
        reserva.setEstadoReserva(Reserva.EstadoReserva.NO_SHOW.name());
        return convertToDTO(reservaRepository.save(reserva));
    }

    
    // ======================
    // UTILIDADES nooo veo neceseidad
    // ======================

    @Override
    @Transactional(readOnly = true)
    public boolean validarDisponibilidad(LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {
        return reservaRepository.findReservasSolapadas(fecha, horaInicio, horaFin).isEmpty();
    }

    @Override
    public void validarFechaReserva(LocalDate fechaReserva) {
        if (fechaReserva.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("No se puede reservar una fecha pasada");
        }
    }


    /*@Override
    @Transactional(readOnly = true)
    public Double calcularIngresosEnRango(LocalDate inicio, LocalDate fin) {
        Double ingresos = reservaRepository.calcularIngresosEnRango(inicio, fin);
        return ingresos != null ? ingresos : 0.0;
    }*/


}