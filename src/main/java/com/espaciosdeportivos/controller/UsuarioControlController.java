package com.espaciosdeportivos.controller;

import com.espaciosdeportivos.dto.UsuarioControlDTO;
import com.espaciosdeportivos.service.UsuarioControlService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuario-control")
@RequiredArgsConstructor
public class UsuarioControlController {

    private final UsuarioControlService service;

    @PostMapping
    public ResponseEntity<UsuarioControlDTO> crear(@Valid @RequestBody UsuarioControlDTO dto){
        return ResponseEntity.ok(service.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioControlDTO> actualizar(@PathVariable Long id, @RequestBody UsuarioControlDTO dto){
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioControlDTO> obtener(@PathVariable Long id){
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<UsuarioControlDTO>> listar(){
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/buscar/{nombre}")
    public ResponseEntity<List<UsuarioControlDTO>> buscarPorNombre(@PathVariable String nombre){
        return ResponseEntity.ok(service.buscarPorNombre(nombre));
    }
}
