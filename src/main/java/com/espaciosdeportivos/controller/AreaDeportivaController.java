package com.espaciosdeportivos.controller;

import com.espaciosdeportivos.dto.AreaDeportivaDTO;
import com.espaciosdeportivos.model.AreaDeportiva;
import com.espaciosdeportivos.service.IAreaDeportivaService;

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
@RequestMapping("/api/areasdeportivas")
@Validated
public class AreaDeportivaController {

    private final IAreaDeportivaService areaDeportivaservice;
    private static final Logger logger = LoggerFactory.getLogger(AreaDeportivaController.class);

    @Autowired
    public AreaDeportivaController(IAreaDeportivaService service) {
        this.areaDeportivaservice = service;
    }
    
    @GetMapping("/activos")
    public ResponseEntity<List<AreaDeportivaDTO>> obtenerTodasLasAreasDeportivas() {
        logger.info("[AREA] Inicio obtenerTodas");
        List<AreaDeportivaDTO> lista = areaDeportivaservice.obtenerTodasLasAreasDeportivas();
        logger.info("[AREA] Fin obtenerTodas");
        return ResponseEntity.ok(lista);
    }
    

    @GetMapping
    public ResponseEntity<List<AreaDeportivaDTO>> listarTodos() {
        logger.info("[AREA] Inicio obtenerTodas");
        List<AreaDeportivaDTO> lista = areaDeportivaservice.listarTodos();
        logger.info("[AREA] Fin obtenerTodas");
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AreaDeportivaDTO> obtenerAreadeportivaPorId(@PathVariable Long id) {
        logger.info("[AREA] Inicio obtenerPorId: {}", id);
        AreaDeportivaDTO dto = areaDeportivaservice.obtenerAreaDeportivaPorId(id);
        logger.info("[AREA] Fin obtenerPorId");
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AreaDeportivaDTO> crearAreadeportiva(@Valid @RequestBody AreaDeportivaDTO dto) {
        AreaDeportivaDTO creado = areaDeportivaservice.crearAreaDeportiva(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<AreaDeportivaDTO> actualizarAreadeportiva(@PathVariable Long id, @Valid @RequestBody AreaDeportivaDTO dto) {
        AreaDeportivaDTO actualizado = areaDeportivaservice.actualizarAreaDeportiva(id, dto);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void eliminar(@PathVariable Long id) {
        areaDeportivaservice.eliminarAreaDeportivaFisicamente(id);
    }
    

    @PatchMapping("/{id}/estado")
    @Transactional
    public ResponseEntity<AreaDeportivaDTO> cambiarEstado(@PathVariable Long id, @RequestParam Boolean nuevoEstado) {
        AreaDeportivaDTO actualizada = areaDeportivaservice.eliminarAreaDeportiva(id, nuevoEstado);
        return ResponseEntity.ok(actualizada);
    }

    @GetMapping("/buscar/{nombre}")
    public ResponseEntity<List<AreaDeportivaDTO>> buscarPorNombre(@PathVariable String nombre) {
        List<AreaDeportivaDTO> resultados = areaDeportivaservice.buscarPorNombre(nombre);
        return ResponseEntity.ok(resultados);
    }

    @GetMapping("/{id}/lock")
    public ResponseEntity<AreaDeportiva> obtenerAreaDeportivaConBloqueo(@PathVariable Long id) {
        AreaDeportiva areaDeportiva = areaDeportivaservice.obtenerAreaDeportivaConBloqueo(id);
        return ResponseEntity.ok(areaDeportiva);
    }
 
}
