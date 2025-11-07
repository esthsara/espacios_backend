package com.espaciosdeportivos.controller;

import com.espaciosdeportivos.dto.ReservaDTO;
import com.espaciosdeportivos.service.IReservaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reservas")
@CrossOrigin(origins = "*")
public class ReservaController {

    @Autowired
    private IReservaService reservaService;

    // CRUD
    @GetMapping
    public ResponseEntity<List<ReservaDTO>> listarTodas() {
        List<ReservaDTO> reservas = reservaService.listarTodas();
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaDTO> obtenerPorId(@PathVariable Long id) {
        ReservaDTO reserva = reservaService.obtenerPorId(id);
        return ResponseEntity.ok(reserva);
    }

    /*@PostMapping("/crearReserva")
    public ResponseEntity<ReservaDTO> crearReserva(@RequestBody ReservaDTO dto) {
        ReservaDTO nuevaReserva = reservaService.crearReserva(dto);
        return ResponseEntity.ok(nuevaReserva);
    }*/

    @PostMapping
    public ResponseEntity<ReservaDTO> crear(@Valid @RequestBody ReservaDTO dto) {
        ReservaDTO reservaCreada = reservaService.crear(dto);
        return ResponseEntity.ok(reservaCreada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservaDTO> actualizar(@PathVariable Long id, @Valid @RequestBody ReservaDTO dto) {
        ReservaDTO reservaActualizada = reservaService.actualizar(id, dto);
        return ResponseEntity.ok(reservaActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        reservaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // BÚSQUEDAS
    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<ReservaDTO>> buscarPorCliente(@PathVariable Long idCliente) {
        List<ReservaDTO> reservas = reservaService.buscarPorCliente(idCliente);
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<ReservaDTO>> buscarPorEstado(@PathVariable String estado) {
        List<ReservaDTO> reservas = reservaService.buscarPorEstado(estado);
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/rango-fechas")
    public ResponseEntity<List<ReservaDTO>> buscarPorRangoFechas(
            @RequestParam("inicio") String inicio,
            @RequestParam("fin") String fin) {
        List<ReservaDTO> reservas = reservaService.buscarPorRangoFechas(
            LocalDate.parse(inicio), LocalDate.parse(fin));
        return ResponseEntity.ok(reservas);
    }


    @GetMapping("/horario-disponible")
    public ResponseEntity<List<String>> getHorasDisponibles(
            @RequestParam Long canchaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {

        List<String> horasDisponibles = reservaService.obtenerHorasDisponibles(canchaId, fecha);

        // Ya son strings del tipo "08:00 - 08:30"
        return ResponseEntity.ok(horasDisponibles);
    }

    
    

    /*@GetMapping("/codigo/{codigo}")
    public ResponseEntity<ReservaDTO> obtenerPorCodigo(@PathVariable String codigo) {
        ReservaDTO reserva = reservaService.obtenerPorCodigoReserva(codigo);
        return ResponseEntity.ok(reserva);
    }*/

    // OPERACIONES DE NEGOCIO
    /*@PostMapping("/{id}/confirmar")
    public ResponseEntity<ReservaDTO> confirmarReserva(@PathVariable Long id) {
        ReservaDTO reservaConfirmada = reservaService.confirmarReserva(id);
        return ResponseEntity.ok(reservaConfirmada);
    }

    @PostMapping("/{id}/cancelar")
    public ResponseEntity<ReservaDTO> cancelarReserva(
            @PathVariable Long id, 
            @RequestBody Map<String, String> request) {
        String motivo = request.get("motivo");
        ReservaDTO reservaCancelada = reservaService.cancelarReserva(id, motivo);
        return ResponseEntity.ok(reservaCancelada);
    }*/

    @PostMapping("/{id}/en-curso")
    public ResponseEntity<ReservaDTO> marcarEnCurso(@PathVariable Long id) {
        ReservaDTO reservaEnCurso = reservaService.marcarComoEnCurso(id);
        return ResponseEntity.ok(reservaEnCurso);
    }

    @PostMapping("/{id}/completar")
    public ResponseEntity<ReservaDTO> completarReserva(@PathVariable Long id) {
        ReservaDTO reservaCompletada = reservaService.marcarComoCompletada(id);
        return ResponseEntity.ok(reservaCompletada);
    }

    @PostMapping("/{id}/no-show")
    public ResponseEntity<ReservaDTO> marcarNoShow(@PathVariable Long id) {
        ReservaDTO reservaNoShow = reservaService.marcarComoNoShow(id);
        return ResponseEntity.ok(reservaNoShow);
    }

    // VALIDACIONES Y REPORTES
    @GetMapping("/disponibilidad")
    public ResponseEntity<Map<String, Boolean>> validarDisponibilidad(
            @RequestParam String fecha,
            @RequestParam String horaInicio,
            @RequestParam String horaFin) {
        boolean disponible = reservaService.validarDisponibilidad(
            LocalDate.parse(fecha), 
            LocalTime.parse(horaInicio), 
            LocalTime.parse(horaFin)
        );
        return ResponseEntity.ok(Map.of("disponible", disponible));
    }

    @GetMapping("/cliente/{idCliente}/activas")
    public ResponseEntity<List<ReservaDTO>> buscarReservasActivas(@PathVariable Long idCliente) {
        List<ReservaDTO> reservas = reservaService.buscarReservasActivasDelCliente(idCliente);
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/dia/{fecha}")
    public ResponseEntity<List<ReservaDTO>> obtenerReservasDelDia(@PathVariable String fecha) {
        List<ReservaDTO> reservas = reservaService.obtenerReservasDelDia(LocalDate.parse(fecha));
        return ResponseEntity.ok(reservas);
    }

    /*@GetMapping("/proximas")
    public ResponseEntity<List<ReservaDTO>> obtenerReservasProximas() {
        List<ReservaDTO> reservas = reservaService.obtenerReservasProximas();
        return ResponseEntity.ok(reservas);
    }*/

    /*@GetMapping("/reporte/ingresos")
    public ResponseEntity<Map<String, Object>> calcularIngresos(
            @RequestParam String inicio,
            @RequestParam String fin) {
        Double ingresos = reservaService.calcularIngresosEnRango(
            LocalDate.parse(inicio), LocalDate.parse(fin));
        return ResponseEntity.ok(Map.of(
            "inicio", inicio,
            "fin", fin,
            "ingresos", ingresos,
            "moneda", "BOB"
        ));
    }*/

    // HEALTH CHECK
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        return ResponseEntity.ok(Map.of("status", "OK", "service", "Reservas"));
    }
}