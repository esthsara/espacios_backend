package com.espaciosdeportivos.controller;

import com.espaciosdeportivos.dto.ParticipaDTO;
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
public class ParticipaController {

    private final IparticipaService participaService;

    @PostMapping
    public ResponseEntity<ParticipaDTO> crear(@Valid @RequestBody ParticipaDTO participaDTO) {
        ParticipaDTO resultado = participaService.crear(participaDTO);
        return ResponseEntity.ok(resultado);
    }

    @PutMapping("/reserva/{idReserva}/invitado/{idInvitado}")
    public ResponseEntity<ParticipaDTO> actualizar(
            @PathVariable Long idReserva, 
            @PathVariable Long idInvitado,
            @Valid @RequestBody ParticipaDTO participaDTO) {
        ParticipaDTO resultado = participaService.actualizar(idReserva, idInvitado, participaDTO);
        return ResponseEntity.ok(resultado);
    }

    @PostMapping("/reserva/{idReserva}/invitado/{idInvitado}/confirmar")
    public ResponseEntity<ParticipaDTO> confirmarInvitacion(
            @PathVariable Long idReserva, 
            @PathVariable Long idInvitado) {
        ParticipaDTO resultado = participaService.confirmarInvitacion(idReserva, idInvitado);
        return ResponseEntity.ok(resultado);
    }

    @PostMapping("/reserva/{idReserva}/invitado/{idInvitado}/asistencia")
    public ResponseEntity<ParticipaDTO> registrarAsistencia(
            @PathVariable Long idReserva, 
            @PathVariable Long idInvitado,
            @RequestBody Map<String, Boolean> request) {
        Boolean asistio = request.get("asistio");
        ParticipaDTO resultado = participaService.registrarAsistencia(idReserva, idInvitado, asistio);
        return ResponseEntity.ok(resultado);
    }

    @PostMapping("/reserva/{idReserva}/invitado/{idInvitado}/notificar")
    public ResponseEntity<ParticipaDTO> marcarComoNotificado(
            @PathVariable Long idReserva, 
            @PathVariable Long idInvitado) {
        ParticipaDTO resultado = participaService.marcarComoNotificado(idReserva, idInvitado);
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
    public ResponseEntity<List<ParticipaDTO>> obtenerPorReserva(@PathVariable Long idReserva) {
        List<ParticipaDTO> resultados = participaService.findByReservaIdReserva(idReserva);
        return ResponseEntity.ok(resultados);
    }

    @GetMapping("/reserva/{idReserva}/confirmados")
    public ResponseEntity<List<ParticipaDTO>> obtenerConfirmadosPorReserva(@PathVariable Long idReserva) {
        List<ParticipaDTO> resultados = participaService.findByReservaIdReservaAndConfirmado(idReserva, true);
        return ResponseEntity.ok(resultados);
    }

    @GetMapping("/invitado/{idInvitado}")
    public ResponseEntity<List<ParticipaDTO>> obtenerPorInvitado(@PathVariable Long idInvitado) {
        List<ParticipaDTO> resultados = participaService.findByInvitadoId(idInvitado);
        return ResponseEntity.ok(resultados);
    }

    @GetMapping("/reserva/{idReserva}/invitado/{idInvitado}")
    public ResponseEntity<ParticipaDTO> obtenerPorIds(
            @PathVariable Long idReserva, 
            @PathVariable Long idInvitado) {
        ParticipaDTO resultado = participaService.findByIds(idReserva, idInvitado);
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