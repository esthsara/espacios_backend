package com.espaciosdeportivos.controller;

import com.espaciosdeportivos.dto.QrDTO;
import com.espaciosdeportivos.model.Qr;
import com.espaciosdeportivos.service.IQrService;

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
@RequestMapping("/api/qr")
@Validated
public class QrController {

    private final IQrService qrService;
    private static final Logger logger = LoggerFactory.getLogger(QrController.class);

    @Autowired
    public QrController(IQrService qrService) {
        this.qrService = qrService;
    }

    @GetMapping
    public ResponseEntity<List<QrDTO>> obtenerTodosLosQrs() {
        logger.info("[QR] Inicio obtenerTodosLosQrs");
        List<QrDTO> qrs = qrService.obtenerTodosLosQrs();
        logger.info("[QR] Fin obtenerTodosLosQrs");
        return ResponseEntity.ok(qrs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QrDTO> obtenerQrPorId(@PathVariable Long id) {
        logger.info("[QR] Inicio obtenerQrPorId: {}", id);
        QrDTO qr = qrService.obtenerQrPorId(id);
        logger.info("[QR] Fin obtenerQrPorId");
        return ResponseEntity.ok(qr);
    }

    @PostMapping
    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<QrDTO> crearQr(@Valid @RequestBody QrDTO qrDTO) {
        QrDTO creado = qrService.crearQr(qrDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<QrDTO> actualizarQr(@PathVariable Long id, @RequestBody QrDTO qrDTO) {
        QrDTO actualizado = qrService.actualizarQr(id, qrDTO);
        return ResponseEntity.ok(actualizado);
    }

    @PutMapping("/{id}/eliminar")
    @Transactional
    public ResponseEntity<QrDTO> eliminarQr(@PathVariable Long id) {
        QrDTO eliminado = qrService.eliminarQr(id);
        return ResponseEntity.ok(eliminado);
    }

    @GetMapping("/{id}/lock")
    public ResponseEntity<Qr> obtenerQrConBloqueo(@PathVariable Long id) {
        Qr qr = qrService.obtenerQrConBloqueo(id);
        return ResponseEntity.ok(qr);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<String> eliminarQrFisicamente(@PathVariable Long id) {
        qrService.eliminarQrFisicamente(id);
        return ResponseEntity.ok("QR eliminado físicamente");
    }

    //K PA FRONT
    @GetMapping("/reserva/{id}")
    public ResponseEntity<List<QrDTO>> obtenerQrsPorReserva(@PathVariable Long id) {
        logger.info("[QR] Inicio obtenerQrsPorReserva: {}", id);
        List<QrDTO> qrs = qrService.obtenerQrsPorReserva(id);
        logger.info("[QR] Fin obtenerQrsPorReserva");
        return ResponseEntity.ok(qrs);
    }

}