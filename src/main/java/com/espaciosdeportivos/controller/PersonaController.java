package com.espaciosdeportivos.controller;

import com.espaciosdeportivos.dto.PersonaDTO;
import com.espaciosdeportivos.service.PersonaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personas")
@RequiredArgsConstructor
public class PersonaController {

    private final PersonaService personaService;

    @PostMapping
    public ResponseEntity<PersonaDTO> crear(@Valid @RequestBody PersonaDTO dto){
        return ResponseEntity.ok(personaService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonaDTO> actualizar(@PathVariable Long id, @RequestBody PersonaDTO dto){
        return ResponseEntity.ok(personaService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        personaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonaDTO> obtener(@PathVariable Long id){
        return ResponseEntity.ok(personaService.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<PersonaDTO>> listar(){
        return ResponseEntity.ok(personaService.listar());
    }

    @GetMapping("/buscar/{nombre}")
    public ResponseEntity<List<PersonaDTO>> buscarPorNombre(@PathVariable String nombre){
        return ResponseEntity.ok(personaService.buscarPorNombre(nombre));
    }
}
