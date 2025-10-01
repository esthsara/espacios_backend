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

    @GetMapping
    public ResponseEntity<List<MacrodistritoDTO>> obtenerTodos() {
        logger.info("[MACRO] Inicio obtenerTodos");
        List<MacrodistritoDTO> macrodistrito = macrodistritoService.obtenerTodosLosMacrodistritos();
        logger.info("[MACRO] Fin obtenerTodos");
        return ResponseEntity.ok(macrodistrito);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MacrodistritoDTO> obtenerPorId(@PathVariable Long id) {
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
    public ResponseEntity<MacrodistritoDTO> actualizar(@PathVariable Long id, @Valid @RequestBody MacrodistritoDTO macrodistritoDTO) {
        MacrodistritoDTO actualizado = macrodistritoService.actualizarMacrodistrito(id, macrodistritoDTO);
        return ResponseEntity.ok(actualizado);
    }

    // Baja lógica
    @PutMapping("/{id}/eliminar")
    @Transactional
    public ResponseEntity<MacrodistritoDTO> eliminar(@PathVariable Long id) {
        MacrodistritoDTO eliminado = macrodistritoService.eliminarMacrodistrito(id);
        return ResponseEntity.ok(eliminado);
    }

    @GetMapping("/{id}/lock")
    public ResponseEntity<Macrodistrito> obtenerMacrodistritoConBloqueo(@PathVariable Long id) {
        Macrodistrito macrodistrito = macrodistritoService.obtenerMacrodistritoConBloqueo(id);
        return ResponseEntity.ok(macrodistrito);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<String> eliminarMacrodistritoFisicamente(@PathVariable Long id) {
        macrodistritoService.eliminarMacrodistritoFisicamente(id);
        return ResponseEntity.ok("Mmacrodistrito eliminada físicamente");
    }
}
