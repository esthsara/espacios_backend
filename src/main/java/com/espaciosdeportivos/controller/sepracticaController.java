package com.espaciosdeportivos.controller;

import com.espaciosdeportivos.dto.sepracticaDTO;
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
    public ResponseEntity<sepracticaDTO> asociarDisciplinaACancha(@Valid @RequestBody sepracticaDTO dto) {
        sepracticaDTO result = sepracticaService.asociarDisciplinaACancha(dto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{idCancha}/{idDisciplina}")
    public ResponseEntity<sepracticaDTO> obtenerDisciplinaDeCancha(
            @PathVariable Long idCancha,
            @PathVariable Long idDisciplina) {
        sepracticaDTO association = sepracticaService.obtenerDisciplinaDeCancha(idCancha, idDisciplina);
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
    public ResponseEntity<List<sepracticaDTO>> obtenerDisciplinasPorCancha(@PathVariable Long idCancha) {
        List<sepracticaDTO> disciplinas = sepracticaService.obtenerDisciplinasPorCancha(idCancha);
        return ResponseEntity.ok(disciplinas);
    }

    @GetMapping("/por-cancha/{idCancha}")
    public ResponseEntity<List<sepracticaDTO>> listarPorIdCancha(@PathVariable Long idCancha) {
        List<sepracticaDTO> relaciones = sepracticaService.listarPorIdCancha(idCancha);
        return ResponseEntity.ok(relaciones);
    }

    @GetMapping("/por-disciplina/{idDisciplina}")
    public ResponseEntity<List<sepracticaDTO>> listarPorIdDisciplina(@PathVariable Long idDisciplina) {
        List<sepracticaDTO> relaciones = sepracticaService.listarPorIdDisciplina(idDisciplina);
        return ResponseEntity.ok(relaciones);
    }
}