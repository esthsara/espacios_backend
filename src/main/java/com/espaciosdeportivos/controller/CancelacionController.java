package com.espaciosdeportivos.controller;

import com.espaciosdeportivos.dto.CancelacionDTO;
import com.espaciosdeportivos.model.Cancelacion;
import com.espaciosdeportivos.service.ICancelacionService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/cancelacion")
@Validated
public class CancelacionController {

    private final ICancelacionService cancelacionService;
    private static final Logger logger = LoggerFactory.getLogger(CancelacionController.class);

    @Autowired
    public CancelacionController(ICancelacionService cancelacionService) {
        this.cancelacionService = cancelacionService;
    }

    @GetMapping
    public ResponseEntity<List<CancelacionDTO>> obtenerTodasLasCancelaciones() {
        logger.info("[CANCELACION] Inicio obtenerTodasLasCancelaciones");
        List<CancelacionDTO> cancelaciones = cancelacionService.obtenerTodasLasCancelaciones();
        logger.info("[CANCELACION] Fin obtenerTodasLasCancelaciones");
        return ResponseEntity.ok(cancelaciones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CancelacionDTO> obtenerCancelacionPorId(@PathVariable Long id) {
        logger.info("[CANCELACION] Inicio obtenerCancelacionPorId: {}", id);
        CancelacionDTO cancelacion = cancelacionService.obtenerCancelacionPorId(id);
        logger.info("[CANCELACION] Fin obtenerCancelacionPorId");
        return ResponseEntity.ok(cancelacion);
    }

    @PostMapping
    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CancelacionDTO> crearCancelacion(@Valid @RequestBody CancelacionDTO cancelacionDTO) {
        CancelacionDTO creada = cancelacionService.crearCancelacion(cancelacionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<CancelacionDTO> actualizarCancelacion(@PathVariable Long id, @RequestBody CancelacionDTO cancelacionDTO) {
        CancelacionDTO actualizada = cancelacionService.actualizarCancelacion(id, cancelacionDTO);
        return ResponseEntity.ok(actualizada);
    }

    @PutMapping("/{id}/eliminar")
    @Transactional
    public ResponseEntity<CancelacionDTO> eliminarCancelacion(@PathVariable Long id) {
        CancelacionDTO eliminada = cancelacionService.eliminarCancelacion(id);
        return ResponseEntity.ok(eliminada);
    }

    @GetMapping("/{id}/lock")
    public ResponseEntity<Cancelacion> obtenerCancelacionConBloqueo(@PathVariable Long id) {
        Cancelacion cancelacion = cancelacionService.obtenerCancelacionConBloqueo(id);
        return ResponseEntity.ok(cancelacion);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<String> eliminarCancelacionFisicamente(@PathVariable Long id) {
        cancelacionService.eliminarCancelacionFisicamente(id);
        return ResponseEntity.ok("Cancelación eliminada físicamente");
    }
}