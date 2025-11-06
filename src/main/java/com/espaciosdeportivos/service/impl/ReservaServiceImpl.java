package com.espaciosdeportivos.service.impl;

import com.espaciosdeportivos.dto.AreaDeportivaDTO;
import com.espaciosdeportivos.dto.CancelacionDTO;
import com.espaciosdeportivos.dto.CanchaDTO;
import com.espaciosdeportivos.dto.ClienteDTO;
import com.espaciosdeportivos.dto.DisciplinaDTO;
import com.espaciosdeportivos.dto.EquipamientoDTO;
import com.espaciosdeportivos.dto.ImagenDTO;
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
    private final ReservaValidator reservaValidator;
    private final CancelacionRepository cancelacionRepository;
    private final IncluyeRepository incluyeRepository;


    /*@Override
    @Transactional
    public ReservaDTO crearReserva(ReservaDTO dto) {
        // Validar existencia y estado de entidades relacionadas
        Cliente cliente = clienteRepository.findById(dto.getCliente().getId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado"));
        if (!Boolean.TRUE.equals(cliente.getEstado())) {
            throw new IllegalArgumentException("El cliente no está activo");
        }

        Cancha cancha = canchaRepository.findById(dto.getCancha().getIdCancha())
                .orElseThrow(() -> new EntityNotFoundException("Cancha no encontrada"));
        if (!Boolean.TRUE.equals(cancha.getEstado())) {
            throw new IllegalArgumentException("El cancha no está activo");
        }

        Disciplina disciplina = disciplinaRepository.findById(dto.getDisciplina().getIdDisciplina())
                .orElseThrow(() -> new EntityNotFoundException("Disciplina no encontrada"));
        if (!Boolean.TRUE.equals(disciplina.getEstado())) {
            throw new IllegalArgumentException("El cancha no está activo");
        }

        AreaDeportiva area = areaDeportivaRepository.findById(dto.getAreaDeportiva().getIdAreadeportiva())
                .orElseThrow(() -> new EntityNotFoundException("Área deportiva no encontrada"));
        if (!Boolean.TRUE.equals(area.getEstado())) {
            throw new IllegalArgumentException("El cancha no está activo");
        }

        // Validar relación Cancha -> Área deportiva

        if (!cancha.getAreaDeportiva().getIdAreaDeportiva().equals(area.getIdAreaDeportiva())) {
            throw new IllegalArgumentException("La cancha no pertenece al área seleccionada");
        }
        // Validar horario dentro del rango permitido

        if (dto.getHoraInicio().isBefore(area.getHoraInicioArea()) ||
            dto.getHoraFin().isAfter(area.getHoraFinArea())) {
            throw new IllegalArgumentException("El horario está fuera del rango de atención del área");
        }

        if (!dto.getHoraFin().isAfter(dto.getHoraInicio())) {
            throw new IllegalArgumentException("La hora fin debe ser posterior a la hora inicio");
        }


        // Validar solapamiento de reservas existentes

        // Obtener reservas existentes para la misma cancha y fecha
        List<Reserva> reservasExistentes = reservaRepository.findByCanchaAndFechaReserva(
                dto.getCancha().getIdCancha(),
                dto.getFechaReserva()
        );

        System.out.println("Reservas existentes encontradas: " + reservasExistentes.size());

        // Verificar solapamiento de horarios
        boolean seSolapa = reservasExistentes.stream()
            .filter(r -> dto.getIdReserva() == null || !r.getIdReserva().equals(dto.getIdReserva())) // Ignora la misma reserva si estás editando
            .anyMatch(r ->
                !dto.getHoraFin().isBefore(r.getHoraInicio()) && // Nueva hora fin no es antes de la existente
                !dto.getHoraInicio().isAfter(r.getHoraFin())     // Nueva hora inicio no es después de la existente
            );

        if (seSolapa) {
            throw new IllegalArgumentException("El horario seleccionado ya está reservado para esta cancha.");
        }


        long duracionMinutos = java.time.Duration.between(dto.getHoraInicio(), dto.getHoraFin()).toMinutes();
        Reserva reserva = new Reserva();
        reserva.setFechaCreacion(LocalDateTime.now());
        reserva.setFechaReserva(dto.getFechaReserva());
        reserva.setHoraInicio(dto.getHoraInicio());
        reserva.setHoraFin(dto.getHoraFin());
        reserva.setEstadoReserva("PENDIENTE");
        reserva.setMontoTotal(dto.getMontoTotal());
        reserva.setObservaciones(dto.getObservaciones());
        reserva.setDuracionMinutos((int) duracionMinutos);
        reserva.setCliente(cliente);

        Reserva reservaGuardada = reservaRepository.save(reserva);

        Incluye incluye = new Incluye();
        incluye.setReserva(reservaGuardada);
        incluye.setCancha(cancha);
        incluye.setDisciplina(disciplina);
        incluyeRepository.save(incluye);


        return convertToDTO(reservaGuardada);
    }*/

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



    private List<LocalTime> generarHorasEnRango(LocalTime inicio, LocalTime fin) {
        List<LocalTime> horas = new java.util.ArrayList<>();
        LocalTime actual = inicio;
        while (actual.isBefore(fin)) {
            horas.add(actual);
            actual = actual.plusHours(1);
            //actual = actual.plusMinutes(30);
        }
        return horas;
    }
    

    
    @Override
    @Transactional(readOnly = true)
    public List<ReservaDTO> listarTodas() {
        return reservaRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

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
        existente.setMontoTotal(dto.getMontoTotal());
        existente.setObservaciones(dto.getObservaciones());
        existente.setCliente(clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con ID: " + dto.getClienteId())));

        return convertToDTO(reservaRepository.save(existente));
    }

    @Override
    @Transactional(readOnly = true)
    public ReservaDTO obtenerPorId(Long id) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reserva no encontrada con ID: " + id));
        return convertToDTO(reserva);
    }

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
            .montoTotal(reserva.getMontoTotal())
            .observaciones(reserva.getObservaciones())
            .clienteId(cliente != null ? cliente.getId() : null)
            .cliente(cliente != null ? convertClienteToDTO(cliente) : null)
            .duracionMinutos(reserva.getDuracionMinutos())
            .build();

        try {
            List<Incluye> incluidos = incluyeRepository.findByReservaIdReserva(reserva.getIdReserva());
            if (!incluidos.isEmpty()) {
                Incluye incluye = incluidos.get(0);
                dto.setCancha(convertCanchaToDTO(incluye.getCancha()));
                dto.setDisciplina(convertDisciplinaToDTO(incluye.getDisciplina()));
            }
        } catch (Exception e) {
            log.warn("Error cargando cancha/disciplina para reserva {}", reserva.getIdReserva(), e);
            dto.setCancha(null);
            dto.setDisciplina(null);
        }

        // >>> Cargar PAGOS <<<
        /*try {
            List<Pago> pagos = pagoRepository.findByReservaIdReserva(reserva.getIdReserva());
            dto.setPagos(pagos.stream()
                .map(this::convertPagoToDTO)
                .toList());
        } catch (Exception e) {
            log.warn("Error cargando pagos para reserva {}", reserva.getIdReserva(), e);
            dto.setPagos(List.of());
        }

        // >>> Cargar QRs <<<
        try {
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
                .montoTotal(dto.getMontoTotal())
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
    // UTILIDADES
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


    @Override
    @Transactional(readOnly = true)
    public Double calcularIngresosEnRango(LocalDate inicio, LocalDate fin) {
        Double ingresos = reservaRepository.calcularIngresosEnRango(inicio, fin);
        return ingresos != null ? ingresos : 0.0;
    }

}