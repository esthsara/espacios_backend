package com.espaciosdeportivos.controller;

import com.espaciosdeportivos.dto.CanchaDTO;
import com.espaciosdeportivos.dto.EquipamientoDTO;
import com.espaciosdeportivos.model.Equipamiento;
import com.espaciosdeportivos.service.IEquipamientoService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipamientos")
@Validated
public class EquipamientoController {

    private final IEquipamientoService equipamientoService;
    private static final Logger logger = LoggerFactory.getLogger(EquipamientoController.class);

    @Autowired
    public EquipamientoController(IEquipamientoService equipamientoService) {
        this.equipamientoService = equipamientoService;
    }

    // -------- CRUD básico --------

    @GetMapping
    public ResponseEntity<List<EquipamientoDTO>> obtenerTodos() {
        logger.info("[EQUIP] Inicio obtenerTodos");
        List<EquipamientoDTO> lista = equipamientoService.ListarTodos();
        logger.info("[EQUIP] Fin obtenerTodos ({} registros)", lista.size());
        return ResponseEntity.ok(lista);
    }
    
    @GetMapping("/activos")
    public ResponseEntity<List<EquipamientoDTO>> obtenerTodosActivos() {
        logger.info("[EQUIP] Inicio obtenerTodos los activos");
        List<EquipamientoDTO> lista = equipamientoService.obtenerTodosLosEquipamientos();
        logger.info("[EQUIP] Fin obtenerTodos ({} registros)", lista.size());
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipamientoDTO> obtenerPorId(@PathVariable Long id) {
        logger.info("[EQUIP] Inicio obtenerPorId: {}", id);
        EquipamientoDTO dto = equipamientoService.obtenerEquipamientoPorId(id);
        logger.info("[EQUIP] Fin obtenerPorId");
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<EquipamientoDTO> crear(@Valid @RequestBody EquipamientoDTO dto) {
        logger.info("[EQUIP] Inicio crear");
        EquipamientoDTO creado = equipamientoService.crearEquipamiento(dto);
        logger.info("[EQUIP] Fin crear: id={}", creado.getIdEquipamiento());
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<EquipamientoDTO> actualizar(@PathVariable Long id, @Valid @RequestBody EquipamientoDTO dto) {
        logger.info("[EQUIP] Inicio actualizar: {}", id);
        EquipamientoDTO actualizado = equipamientoService.actualizarEquipamiento(id, dto);
        logger.info("[EQUIP] Fin actualizar: {}", id);
        return ResponseEntity.ok(actualizado);
    }

    // Baja lógica (estadobool = false)
    @PutMapping("/{id}/eliminar")
    @Transactional
    public ResponseEntity<EquipamientoDTO> eliminar(@PathVariable Long id) {
        logger.info("[EQUIP] Inicio eliminar (baja lógica): {}", id);
        EquipamientoDTO eliminado = equipamientoService.eliminarEquipamiento(id);
        logger.info("[EQUIP] Fin eliminar (baja lógica): {}", id);
        return ResponseEntity.ok(eliminado);
    }

    //eliminacion con el patch 
    @PatchMapping("/{id}/estado")
    @Transactional
    public ResponseEntity<EquipamientoDTO> cambiarEstadoCancha(@PathVariable Long id, @RequestParam Boolean nuevoEstado) {
        logger.info("[CANCHA] Inicio cambiar estado equipamiento: {}", id);
        EquipamientoDTO actualizada = equipamientoService.eliminar(id, nuevoEstado);
        logger.info("[CANCHA] Inicio cambiar estado equipamiento: {}", id);
        return ResponseEntity.ok(actualizada);
    }



    @GetMapping("/{id}/lock")
    public ResponseEntity<Equipamiento> obtenerEquipamientoConBloqueo(@PathVariable Long id) {
        Equipamiento equipamiento = equipamientoService.obtenerEquipamientoConBloqueo(id);
        return ResponseEntity.ok(equipamiento);
    }
 
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<String> eliminarEquipamientoFisicamente(@PathVariable Long id) {
        equipamientoService.eliminarEquipamientoFisicamente(id);
        return ResponseEntity.ok("Equipamiento eliminada físicamente");
    }
}
