package com.espaciosdeportivos.controller;


import com.espaciosdeportivos.dto.MacrodistritoDTO;
import com.espaciosdeportivos.model.Macrodistrito;
import com.espaciosdeportivos.service.IMacrodistritoService;

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
@RequestMapping("/api/macrodistrito")
@Validated
public class MacrodistritoController {

    private final IMacrodistritoService macrodistritoService;
    private static final Logger logger = LoggerFactory.getLogger(MacrodistritoController.class);

    @Autowired
    public MacrodistritoController(IMacrodistritoService macrodistritoService) {
        this.macrodistritoService = macrodistritoService;
    }

    @GetMapping("/activos")
    public ResponseEntity<List<MacrodistritoDTO>> listarMacrodistritosActivos() {
        logger.info("[MACRO] Inicio obtenerTodosLosMacrodistritos");
        List<MacrodistritoDTO> macrodistrito = macrodistritoService.obtenerTodosLosMacrodistritos();
        logger.info("[MACRO] Fin obtenerTodosLosMacrodistritos");
        return ResponseEntity.ok(macrodistrito);
    }

    @GetMapping
    public ResponseEntity<List<MacrodistritoDTO>> ListarTodos() {
        logger.info("[MACRO] Inicio obtenerTodos");
        List<MacrodistritoDTO> macrodistrito = macrodistritoService.ListarTodos();
        logger.info("[MACRO] Fin obtenerTodos");
        return ResponseEntity.ok(macrodistrito);
    }

    

    @GetMapping("/{id}")
    public ResponseEntity<MacrodistritoDTO> obtenerMacrodistritoPorId(@PathVariable Long id) {
        logger.info("[MACRO] Inicio obtenerPorId: {}", id);
        MacrodistritoDTO macrodistrito = macrodistritoService.obtenerMacrodistritoPorId(id);
        logger.info("[MACRO] Fin obtenerPorId");
        return ResponseEntity.ok(macrodistrito);
    }

    @PostMapping
    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<MacrodistritoDTO> crearMacrodistrito(@Valid @RequestBody MacrodistritoDTO macrodistritoDTO) {
        MacrodistritoDTO creado = macrodistritoService.crearMacrodistrito(macrodistritoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<MacrodistritoDTO> actualizarMacrodistrito(@PathVariable Long id, @Valid @RequestBody MacrodistritoDTO macrodistritoDTO) {
        MacrodistritoDTO actualizado = macrodistritoService.actualizarMacrodistrito(id, macrodistritoDTO);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void eliminar(@PathVariable Long id) {
        macrodistritoService.eliminarMacrodistritoFisicamente(id);
    }

    @PatchMapping("/{id}/estado")
    public MacrodistritoDTO cambiarEstado(@PathVariable Long id, @RequestParam Boolean nuevoEstado) {
        return macrodistritoService.eliminarMacrodistrito(id, nuevoEstado);
    }

    // Baja lógica
    /*@PutMapping("/{id}/eliminar")
    @Transactional
    public ResponseEntity<MacrodistritoDTO> eliminar(@PathVariable Long id) {
        MacrodistritoDTO eliminado = macrodistritoService.eliminarMacrodistrito(id);
        return ResponseEntity.ok(eliminado);
    }*/

    @GetMapping("/buscar/{nombre}")
    public List<MacrodistritoDTO> buscarPorNombre(@PathVariable String nombre) {
        List<MacrodistritoDTO> resultados = macrodistritoService.buscarPorNombre(nombre);
        return resultados;
    }

    @GetMapping("/{id}/lock")
    public ResponseEntity<Macrodistrito> obtenerMacrodistritoConBloqueo(@PathVariable Long id) {
        Macrodistrito macrodistrito = macrodistritoService.obtenerMacrodistritoConBloqueo(id);
        return ResponseEntity.ok(macrodistrito);
    }

    
}
