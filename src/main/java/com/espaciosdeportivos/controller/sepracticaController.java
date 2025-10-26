package com.espaciosdeportivos.controller;

import com.espaciosdeportivos.dto.SepracticaDTO;
import com.espaciosdeportivos.service.IsepracticaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cancha-disciplina")
@Validated
@RequiredArgsConstructor
public class sepracticaController {

    private final IsepracticaService sepracticaService;

    @PostMapping
    public ResponseEntity<SepracticaDTO> asociarDisciplinaACancha(@Valid @RequestBody SepracticaDTO dto) {
        SepracticaDTO result = sepracticaService.asociarDisciplinaACancha(dto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{idCancha}/{idDisciplina}")
    public ResponseEntity<SepracticaDTO> obtenerDisciplinaDeCancha(
            @PathVariable Long idCancha,
            @PathVariable Long idDisciplina) {
        SepracticaDTO association = sepracticaService.obtenerDisciplinaDeCancha(idCancha, idDisciplina);
        return ResponseEntity.ok(association);
    }

    @DeleteMapping("/{idCancha}/{idDisciplina}")
    public ResponseEntity<Void> desasociarDisciplinaDeCancha(
            @PathVariable Long idCancha,
            @PathVariable Long idDisciplina) {
        sepracticaService.desasociarDisciplinaDeCancha(idCancha, idDisciplina);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{idCancha}/disciplinas")
    public ResponseEntity<List<SepracticaDTO>> obtenerDisciplinasPorCancha(@PathVariable Long idCancha) {
        List<SepracticaDTO> disciplinas = sepracticaService.obtenerDisciplinasPorCancha(idCancha);
        return ResponseEntity.ok(disciplinas);
    }

    @GetMapping("/por-cancha/{idCancha}")
    public ResponseEntity<List<SepracticaDTO>> listarPorIdCancha(@PathVariable Long idCancha) {
        List<SepracticaDTO> relaciones = sepracticaService.listarPorIdCancha(idCancha);
        return ResponseEntity.ok(relaciones);
    }

    @GetMapping("/por-disciplina/{idDisciplina}")
    public ResponseEntity<List<SepracticaDTO>> listarPorIdDisciplina(@PathVariable Long idDisciplina) {
        List<SepracticaDTO> relaciones = sepracticaService.listarPorIdDisciplina(idDisciplina);
        return ResponseEntity.ok(relaciones);
    }
}