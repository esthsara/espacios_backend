package com.espaciosdeportivos.controller;


import com.espaciosdeportivos.dto.ZonaDTO;
import com.espaciosdeportivos.model.Zona;
import com.espaciosdeportivos.service.IZonaService;

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
@RequestMapping("/api/zona")
@Validated
public class ZonaController {

    private final IZonaService zonaService;
    private static final Logger logger = LoggerFactory.getLogger(ZonaController.class);

    @Autowired
    public ZonaController(IZonaService zonaService) {
        this.zonaService = zonaService;
    }
    @GetMapping("/activos")
    public ResponseEntity<List<ZonaDTO>> listarZonasActivas() {
        logger.info("[ZONA] Inicio obtenerTodasLasZonas");
        List<ZonaDTO> zonas = zonaService.obtenerTodasLasZonas();
        logger.info("[ZONA] Fin obtenerTodasLasZonas");
        return ResponseEntity.ok(zonas);
    }

    @GetMapping
    public ResponseEntity<List<ZonaDTO>> ListarTodos() {
        logger.info("[ZONA] Inicio obtenerTodasLasZonas");
        List<ZonaDTO> zonas = zonaService.ListarTodos();
        logger.info("[ZONA] Fin obtenerTodasLasZonas");
        return ResponseEntity.ok(zonas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ZonaDTO> obtenerZonaPorId(@PathVariable Long id) {
        logger.info("[ZONA] Inicio obtenerZonaPorId: {}", id);
        ZonaDTO zona = zonaService.obtenerZonaPorId(id);
        logger.info("[ZONA] Fin obtenerZonaPorId");
        return ResponseEntity.ok(zona);
    }

    @PostMapping
    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ZonaDTO> crearZona(@Valid @RequestBody ZonaDTO zonaDTO) {
        ZonaDTO creada = zonaService.crearZona(zonaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<ZonaDTO> actualizarZona(@PathVariable Long id, @Valid @RequestBody ZonaDTO zonaDTO) {
        ZonaDTO actualizada = zonaService.actualizarZona(id, zonaDTO);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void eliminar(@PathVariable Long id) {
        zonaService.eliminarZonaFisicamente(id);
    }

    @PatchMapping("/{id}/estado")
    @Transactional
    public ResponseEntity<ZonaDTO> cambiarEstado(@PathVariable Long id, @RequestParam Boolean nuevoEstado) {
        ZonaDTO actualizada = zonaService.eliminarZona(id, nuevoEstado);
        return ResponseEntity.ok(actualizada);
    }

    // Baja lógica (estado=false)
    /*@PutMapping("/{id}/eliminar")
    @Transactional
    public ResponseEntity<ZonaDTO> eliminarZona(@PathVariable Long id) {
        ZonaDTO eliminada = zonaService.eliminarZona(id);
        return ResponseEntity.ok(eliminada);
    }*/
    @GetMapping("/buscar/{nombre}")
    public List<ZonaDTO> buscarPorNombre(@PathVariable String nombre) {
        return zonaService.buscarPorNombre(nombre);
    }

    @GetMapping("/{id}/lock")
    public ResponseEntity<Zona> obtenerZonaConBloqueo(@PathVariable Long id) {
        Zona zona = zonaService.obtenerZonaConBloqueo(id);
        return ResponseEntity.ok(zona);
    }

    
}
