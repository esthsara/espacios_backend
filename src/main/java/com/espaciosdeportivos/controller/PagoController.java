package com.espaciosdeportivos.controller;

import com.espaciosdeportivos.dto.PagoDTO;
import com.espaciosdeportivos.service.IPagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pagos")
@CrossOrigin(origins = "*")
public class PagoController {

    @Autowired
    private IPagoService pagoService;

    // CRUD ENDPOINTS
    @GetMapping
    public ResponseEntity<List<PagoDTO>> listarTodos() {
        List<PagoDTO> pagos = pagoService.listarTodos();
        return ResponseEntity.ok(pagos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagoDTO> obtenerPorId(@PathVariable Long id) {
        PagoDTO pago = pagoService.obtenerPorId(id);
        return ResponseEntity.ok(pago);
    }

    @PostMapping
    public ResponseEntity<PagoDTO> crear(@Valid @RequestBody PagoDTO dto) {
        PagoDTO pagoCreado = pagoService.crear(dto);
        return ResponseEntity.ok(pagoCreado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PagoDTO> actualizar(@PathVariable Long id, @Valid @RequestBody PagoDTO dto) {
        PagoDTO pagoActualizado = pagoService.actualizar(id, dto);
        return ResponseEntity.ok(pagoActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        pagoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // ENDPOINTS DE BÚSQUEDA
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<PagoDTO>> buscarPorEstado(@PathVariable String estado) {
        List<PagoDTO> pagos = pagoService.buscarPorEstado(estado);
        return ResponseEntity.ok(pagos);
    }

    @GetMapping("/metodo/{metodoPago}")
    public ResponseEntity<List<PagoDTO>> buscarPorMetodo(@PathVariable String metodoPago) {
        List<PagoDTO> pagos = pagoService.buscarPorMetodo(metodoPago);
        return ResponseEntity.ok(pagos);
    }

    @GetMapping("/tipo/{tipoPago}")
    public ResponseEntity<List<PagoDTO>> buscarPorTipo(@PathVariable String tipoPago) {
        List<PagoDTO> pagos = pagoService.buscarPorTipo(tipoPago);
        return ResponseEntity.ok(pagos);
    }

    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<List<PagoDTO>> buscarPorFecha(@PathVariable String fecha) {
        List<PagoDTO> pagos = pagoService.buscarPorFecha(LocalDate.parse(fecha));
        return ResponseEntity.ok(pagos);
    }

    @GetMapping("/rango-fechas")
    public ResponseEntity<List<PagoDTO>> buscarPorRangoFechas(
            @RequestParam("inicio") String inicio,
            @RequestParam("fin") String fin) {
        List<PagoDTO> pagos = pagoService.buscarPorRangoFechas(
            LocalDate.parse(inicio), LocalDate.parse(fin));
        return ResponseEntity.ok(pagos);
    }

    @GetMapping("/transaccion/{codigo}")
    public ResponseEntity<PagoDTO> obtenerPorCodigoTransaccion(@PathVariable String codigo) {
        PagoDTO pago = pagoService.obtenerPorCodigoTransaccion(codigo);
        return ResponseEntity.ok(pago);
    }

    @GetMapping("/reserva/{idReserva}")
    public ResponseEntity<List<PagoDTO>> buscarPorReserva(@PathVariable Long idReserva) {
        List<PagoDTO> pagos = pagoService.buscarPorReserva(idReserva);
        return ResponseEntity.ok(pagos);
    }

    @GetMapping("/reserva/{idReserva}/estado/{estado}")
    public ResponseEntity<List<PagoDTO>> buscarPorReservaYEstado(
            @PathVariable Long idReserva, 
            @PathVariable String estado) {
        List<PagoDTO> pagos = pagoService.buscarPorReservaYEstado(idReserva, estado);
        return ResponseEntity.ok(pagos);
    }

    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<PagoDTO>> buscarPorCliente(@PathVariable Long idCliente) {
        List<PagoDTO> pagos = pagoService.buscarPorCliente(idCliente);
        return ResponseEntity.ok(pagos);
    }

    @GetMapping("/cliente/{idCliente}/estado/{estado}")
    public ResponseEntity<List<PagoDTO>> buscarPorClienteYEstado(
            @PathVariable Long idCliente,
            @PathVariable String estado) {
        List<PagoDTO> pagos = pagoService.buscarPorClienteYEstado(idCliente, estado);
        return ResponseEntity.ok(pagos);
    }

    // ENDPOINTS DE OPERACIONES DE NEGOCIO
    @PostMapping("/{id}/confirmar")
    public ResponseEntity<PagoDTO> confirmarPago(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        String codigoTransaccion = request.get("codigoTransaccion");
        PagoDTO pagoConfirmado = pagoService.confirmarPago(id, codigoTransaccion);
        return ResponseEntity.ok(pagoConfirmado);
    }

    @PostMapping("/{id}/anular")
    public ResponseEntity<PagoDTO> anularPago(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        String razon = request.get("razon");
        PagoDTO pagoAnulado = pagoService.anularPago(id, razon);
        return ResponseEntity.ok(pagoAnulado);
    }

    @PostMapping("/{id}/rechazar")
    public ResponseEntity<PagoDTO> rechazarPago(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        String razon = request.get("razon");
        PagoDTO pagoRechazado = pagoService.rechazarPago(id, razon);
        return ResponseEntity.ok(pagoRechazado);
    }

    // ENDPOINTS DE REPORTES
    @GetMapping("/reporte/total-por-fecha")
    public ResponseEntity<Map<String, Object>> obtenerTotalPorFecha(@RequestParam String fecha) {
        Double total = pagoService.obtenerTotalPagosConfirmadosPorFecha(LocalDate.parse(fecha));
        return ResponseEntity.ok(Map.of(
            "fecha", fecha, 
            "total", total,
            "moneda", "BOB"
        ));
    }

    @GetMapping("/reporte/total-por-rango")
    public ResponseEntity<Map<String, Object>> obtenerTotalPorRango(
            @RequestParam String inicio,
            @RequestParam String fin) {
        Double total = pagoService.obtenerTotalPagosConfirmadosPorRango(
            LocalDate.parse(inicio), LocalDate.parse(fin));
        return ResponseEntity.ok(Map.of(
            "inicio", inicio,
            "fin", fin,
            "total", total,
            "moneda", "BOB"
        ));
    }

    @GetMapping("/reporte/pagos-confirmados")
    public ResponseEntity<List<PagoDTO>> obtenerPagosConfirmadosEnRango(
            @RequestParam String inicio,
            @RequestParam String fin) {
        List<PagoDTO> pagos = pagoService.obtenerPagosConfirmadosEnRango(
            LocalDate.parse(inicio), LocalDate.parse(fin));
        return ResponseEntity.ok(pagos);
    }

    @GetMapping("/reserva/{idReserva}/saldo-pendiente")
    public ResponseEntity<Map<String, Object>> obtenerSaldoPendiente(@PathVariable Long idReserva) {
        Double saldoPendiente = pagoService.obtenerSaldoPendienteReserva(idReserva);
        return ResponseEntity.ok(Map.of(
            "idReserva", idReserva,
            "saldoPendiente", saldoPendiente,
            "moneda", "BOB"
        ));
    }

    // ENDPOINTS DE VALIDACIÓN
    @GetMapping("/validar-transaccion/{codigo}")
    public ResponseEntity<Map<String, Boolean>> validarCodigoTransaccion(@PathVariable String codigo) {
        boolean disponible = pagoService.validarCodigoTransaccionUnico(codigo);
        return ResponseEntity.ok(Map.of("disponible", disponible));
    }

    @GetMapping("/validar-monto-reserva")
    public ResponseEntity<Map<String, Boolean>> validarMontoYReserva(
            @RequestParam Double monto,
            @RequestParam Long idReserva) {
        boolean existe = pagoService.existePagoConMismoMontoYReserva(monto, idReserva);
        return ResponseEntity.ok(Map.of("existe", existe));
    }

    // HEALTH CHECK
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        return ResponseEntity.ok(Map.of("status", "OK", "service", "Pagos"));
    }
}