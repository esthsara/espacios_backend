package com.espaciosdeportivos.controller;

import com.espaciosdeportivos.dto.DisponeDTO;
//import com.espaciosdeportivos.model.dispone;
import com.espaciosdeportivos.service.IdisponeService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/cancha-equipamiento")
@Validated
public class disponeController {

    private final IdisponeService disponeService;
    private static final Logger logger = LoggerFactory.getLogger(disponeController.class);

    @Autowired
    public disponeController(IdisponeService disponeService) {
        this.disponeService = disponeService;
    }

    // Asociar/Actualizar equipamiento a una cancha
    @PostMapping
    public ResponseEntity<DisponeDTO> asociarEquipamientoACancha(
            @Valid @RequestBody DisponeDTO dto) {
        logger.info("[DISPONE] Solicitud para asociar/actualizar equipamiento ID {} a cancha ID {} con cantidad {}.",
                dto.getIdEquipamiento(), dto.getIdCancha(), dto.getCantidad());
        DisponeDTO result = disponeService.asociarEquipamientoACancha(dto);
        logger.info("[DISPONE] Asociación creada/actualizada exitosamente.");
        return ResponseEntity.ok(result);
    }

    // Obtener la asociación específica (cancha + equipamiento)
    @GetMapping("/{idCancha}/{idEquipamiento}")
    public ResponseEntity<DisponeDTO> obtenerEquipamientoDeCancha(
            @PathVariable Long idCancha,
            @PathVariable Long idEquipamiento) {
        logger.info("[DISPONE] Solicitud para obtener asociación de cancha ID {} y equipamiento ID {}.",
                idCancha, idEquipamiento);
        DisponeDTO association = disponeService.obtenerEquipamientoDeCancha(idCancha, idEquipamiento);
        logger.info("[DISPONE] Asociación encontrada exitosamente.");
        return ResponseEntity.ok(association);
    }

    // Desasociar equipamiento de una cancha
    @DeleteMapping("/{idCancha}/{idEquipamiento}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> desasociarEquipamientoDeCancha(
            @PathVariable Long idCancha,
            @PathVariable Long idEquipamiento) {
        logger.info("[DISPONE] Solicitud para desasociar equipamiento ID {} de cancha ID {}.",
                idEquipamiento, idCancha);
        disponeService.desasociarEquipamientoDeCancha(idCancha, idEquipamiento);
        logger.info("[DISPONE] Asociación eliminada exitosamente.");
        return ResponseEntity.noContent().build();
    }

    // Listar todos los equipamientos de una cancha (DTO simple)
    @GetMapping("/{idCancha}/equipamientos")
    public ResponseEntity<List<DisponeDTO>> obtenerEquipamientosPorCancha(@PathVariable Long idCancha) {
        List<DisponeDTO> lista = disponeService.obtenerEquipamientosPorCancha(idCancha);
        return ResponseEntity.ok(lista);
    }

    // Listar con detalle (similar a espacio-equipamiento)
    @GetMapping("/por-cancha/{idCancha}")
    public ResponseEntity<List<Map<String, Object>>> listarPorIdCancha(@PathVariable Long idCancha) {
        List<DisponeDTO> listaDTO = disponeService.listarPorIdCancha(idCancha);

        List<Map<String, Object>> response = new ArrayList<>();
        for (DisponeDTO d : listaDTO) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("idCancha", d.getIdCancha());
            item.put("idEquipamiento", d.getIdEquipamiento());
            item.put("cantidad", d.getCantidad());
            response.add(item);
        }
        return ResponseEntity.ok(response);
    }

}
