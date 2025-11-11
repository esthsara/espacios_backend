package com.espaciosdeportivos.controller;

import com.espaciosdeportivos.dto.QrDTO;
import com.espaciosdeportivos.model.Qr;
import com.espaciosdeportivos.service.IQrService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

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
    //BUSQUEDAS

    @GetMapping("/reserva/{id}")
    public ResponseEntity<List<QrDTO>> obtenerQrsPorReserva(@PathVariable Long id) {
        logger.info("[QR] Inicio obtenerQrsPorReserva: {}", id);
        List<QrDTO> qrs = qrService.obtenerQrsPorReserva(id);
        logger.info("[QR] Fin obtenerQrsPorReserva");
        return ResponseEntity.ok(qrs);
    }

    //GEERAR QR
    @PostMapping("/reserva/{idReserva}/generar")
    public ResponseEntity<QrDTO> generarQrParaReserva(
            @PathVariable Long idReserva,
            @RequestParam Long idPersona) {
        QrDTO qr = qrService.generarQrParaReserva(idReserva, idPersona);
        if (qr == null) {
            return ResponseEntity.status(500).body(null); // Error interno al generar QR
        }
        return ResponseEntity.ok(qr);
    }
    
    //VALIDAR QR
    @PostMapping("/validar")
    public ResponseEntity<Boolean> validarQr(@RequestParam String codigo) {
        try {
            // Aquí puedes implementar la lógica de validación de QR
            // Por ahora, devolvemos true si existe y está activo
            List<QrDTO> qrs = qrService.obtenerTodosLosQrs();
            boolean existe = qrs.stream()
                    .anyMatch(qr -> qr.getCodigoQr().equals(codigo) && Boolean.TRUE.equals(qr.getEstado()));
            return ResponseEntity.ok(existe);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(false);
        }
    }

    @GetMapping("/contenido/{codigo}")
    public ResponseEntity<String> verContenidoQr(@PathVariable String codigo) {
        try {
            List<QrDTO> qrs = qrService.obtenerTodosLosQrs();
            QrDTO qr = qrs.stream()
                    .filter(q -> q.getCodigoQr().equals(codigo))
                    .findFirst()
                    .orElse(null);

            if (qr == null) {
                return ResponseEntity.notFound().build();
            }

            // Aquí puedes devolver el contenido del QR si lo guardaste como JSON
            // Por ahora, devolvemos una representación simple
            String contenido = String.format(
                "QR: %s\nReserva: %d\nPersona: %d\nGenerado: %s\nExpira: %s",
                qr.getCodigoQr(),
                qr.getIdReserva(),
                qr.getIdPersona(),
                qr.getFechaGeneracion(),
                qr.getFechaExpiracion()
            );
            return ResponseEntity.ok(contenido);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al obtener contenido del QR");
        }
    }

    @GetMapping("/imagen/{codigo}")
    public ResponseEntity<org.springframework.core.io.Resource> verImagenQr(@PathVariable String codigo) {
        try {
            // Aquí puedes implementar la lógica para servir la imagen del QR
            // Por ahora, devolvemos un error
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

        // 🔍 Buscar QR por código
    @GetMapping("/codigo/{codigoQr}")
    public ResponseEntity<QrDTO> obtenerPorCodigo(@PathVariable String codigoQr) {
        return ResponseEntity.ok(qrService.obtenerQrPorCodigo(codigoQr));
    }

    // 🔍 Buscar QRs por reserva
    @GetMapping("/reserva/{idReserva}")
    public ResponseEntity<List<QrDTO>> obtenerPorReserva(@PathVariable Long idReserva) {
        return ResponseEntity.ok(qrService.obtenerQrsPorReserva(idReserva));
    }

    // 🔍 Buscar QRs por persona
    @GetMapping("/persona/{idPersona}")
    public ResponseEntity<List<QrDTO>> obtenerPorPersona(@PathVariable Long idPersona) {
        return ResponseEntity.ok(qrService.obtenerQrsPorPersona(idPersona));
    }
    //aqui jalamos literalmente la imagen :)
    @GetMapping("/qrs/{filename}")
    public ResponseEntity<Resource> getQrImage(@PathVariable String filename) throws IOException {
        Path path = Paths.get("uploads", "img", "qr").resolve(filename);
        Resource resource = new UrlResource(path.toUri());

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(resource);
    }



}