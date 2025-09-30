package com.espaciosdeportivos.controller;

import com.espaciosdeportivos.dto.AdministradorDTO;
import com.espaciosdeportivos.service.AdministradorService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/administradores")
@RequiredArgsConstructor
public class AdministradorController {

    private final AdministradorService administradorService;

    @PostMapping
    public AdministradorDTO crear(@Valid @RequestBody AdministradorDTO dto) {
        return administradorService.crearAdministrador(dto);
    }

    @GetMapping("/{id}")
    public AdministradorDTO obtenerPorId(@PathVariable Long id) {
        return administradorService.obtenerPorId(id);
    }

    @GetMapping
    public List<AdministradorDTO> listarTodos() {
        return administradorService.listarTodos();
    }

    @PutMapping("/{id}")
    public AdministradorDTO actualizar(@PathVariable Long id, @RequestBody AdministradorDTO dto) {
        return administradorService.actualizarAdministrador(id, dto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        administradorService.eliminarAdministrador(id);
    }

    @GetMapping("/buscar/{nombre}")
    public List<AdministradorDTO> buscarPorNombre(@PathVariable String nombre) {
        return administradorService.buscarPorNombre(nombre);
    }
}
