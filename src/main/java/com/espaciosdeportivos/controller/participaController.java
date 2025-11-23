package com.espaciosdeportivos.controller;

import com.espaciosdeportivos.dto.participaDTO;
import com.espaciosdeportivos.service.IparticipaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/participaciones")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class participaController {

    private final IparticipaService participaService;

    @PostMapping
    public ResponseEntity<participaDTO> crear(@Valid @RequestBody participaDTO participaDTO) {
        participaDTO resultado = participaService.crear(participaDTO);
        return ResponseEntity.ok(resultado);
    }

    @PutMapping("/reserva/{idReserva}/invitado/{idInvitado}")
    public ResponseEntity<participaDTO> actualizar(
            @PathVariable Long idReserva, 
            @PathVariable Long idInvitado,
            @Valid @RequestBody participaDTO participaDTO) {
        participaDTO resultado = participaService.actualizar(idReserva, idInvitado, participaDTO);
        return ResponseEntity.ok(resultado);
    }

    @PostMapping("/reserva/{idReserva}/invitado/{idInvitado}/confirmar")
    public ResponseEntity<participaDTO> confirmarInvitacion(
            @PathVariable Long idReserva, 
            @PathVariable Long idInvitado) {
        participaDTO resultado = participaService.confirmarInvitacion(idReserva, idInvitado);
        return ResponseEntity.ok(resultado);
    }

    @PostMapping("/reserva/{idReserva}/invitado/{idInvitado}/asistencia")
    public ResponseEntity<participaDTO> registrarAsistencia(
            @PathVariable Long idReserva, 
            @PathVariable Long idInvitado,
            @RequestBody Map<String, Boolean> request) {
        Boolean asistio = request.get("asistio");
        participaDTO resultado = participaService.registrarAsistencia(idReserva, idInvitado, asistio);
        return ResponseEntity.ok(resultado);
    }

    @PostMapping("/reserva/{idReserva}/invitado/{idInvitado}/notificar")
    public ResponseEntity<participaDTO> marcarComoNotificado(
            @PathVariable Long idReserva, 
            @PathVariable Long idInvitado) {
        participaDTO resultado = participaService.marcarComoNotificado(idReserva, idInvitado);
        return ResponseEntity.ok(resultado);
    }

    @DeleteMapping("/reserva/{idReserva}/invitado/{idInvitado}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long idReserva, 
            @PathVariable Long idInvitado) {
        participaService.eliminar(idReserva, idInvitado);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reserva/{idReserva}")
    public ResponseEntity<List<participaDTO>> obtenerPorReserva(@PathVariable Long idReserva) {
        List<participaDTO> resultados = participaService.findByReservaIdReserva(idReserva);
        return ResponseEntity.ok(resultados);
    }

    @GetMapping("/reserva/{idReserva}/confirmados")
    public ResponseEntity<List<participaDTO>> obtenerConfirmadosPorReserva(@PathVariable Long idReserva) {
        List<participaDTO> resultados = participaService.findByReservaIdReservaAndConfirmado(idReserva, true);
        return ResponseEntity.ok(resultados);
    }

    @GetMapping("/invitado/{idInvitado}")
    public ResponseEntity<List<participaDTO>> obtenerPorInvitado(@PathVariable Long idInvitado) {
        List<participaDTO> resultados = participaService.findByInvitadoId(idInvitado);
        return ResponseEntity.ok(resultados);
    }

    @GetMapping("/reserva/{idReserva}/invitado/{idInvitado}")
    public ResponseEntity<participaDTO> obtenerPorIds(
            @PathVariable Long idReserva, 
            @PathVariable Long idInvitado) {
        participaDTO resultado = participaService.findByIds(idReserva, idInvitado);
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/reserva/{idReserva}/invitado/{idInvitado}/existe")
    public ResponseEntity<Map<String, Boolean>> existeInvitacion(
            @PathVariable Long idReserva, 
            @PathVariable Long idInvitado) {
        boolean existe = participaService.existsByInvitadoIdAndReservaIdReserva(idInvitado, idReserva);
        return ResponseEntity.ok(Map.of("existe", existe));
    }

    @GetMapping("/reserva/{idReserva}/contador")
    public ResponseEntity<Map<String, Long>> contarInvitados(@PathVariable Long idReserva) {
        Long total = participaService.countByReservaIdReserva(idReserva);
        return ResponseEntity.ok(Map.of("totalInvitados", total));
    }

    @GetMapping("/reserva/{idReserva}/asistentes")
    public ResponseEntity<Map<String, Long>> contarAsistentes(@PathVariable Long idReserva) {
        Long asistentes = participaService.countByReservaIdReservaAndAsistio(idReserva, true);
        return ResponseEntity.ok(Map.of("totalAsistentes", asistentes));
    }
}