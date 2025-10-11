package com.espaciosdeportivos.controller;

import com.espaciosdeportivos.dto.UsuarioControlDTO;
import com.espaciosdeportivos.service.UsuarioControlService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuario_control")
@RequiredArgsConstructor
public class UsuarioControlController {

    private final UsuarioControlService usuarioControlService;

    // Solo los activos
    @GetMapping("/activos")
    public List<UsuarioControlDTO> listarUsuariosActivos() {
        return usuarioControlService.obtenerTodosLosUsuariosControl();
    }

    // Listar todos, incluyendo desactivados
    @GetMapping
    public List<UsuarioControlDTO> listarTodos() {
        return usuarioControlService.listarTodos();
    }

    @GetMapping("/{id}")
    public UsuarioControlDTO obtenerPorId(@PathVariable Long id) {
        return usuarioControlService.obtenerUsuarioControlPorId(id);
    }

    @GetMapping("/buscar/{nombre}")
    public List<UsuarioControlDTO> buscarPorNombre(@PathVariable String nombre) {
        return usuarioControlService.buscarPorNombre(nombre);
    }

    @PostMapping
    public UsuarioControlDTO crear(@Valid @RequestBody UsuarioControlDTO dto) {
        return usuarioControlService.crearUsuarioControl(dto);
    }

    @PutMapping("/{id}")
    public UsuarioControlDTO actualizar(@PathVariable Long id, @Valid @RequestBody UsuarioControlDTO dto) {
        return usuarioControlService.actualizarUsuarioControl(id, dto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        usuarioControlService.eliminarUsuarioControl(id);
    }

    @PatchMapping("/{id}/estado")
    public UsuarioControlDTO cambiarEstado(@PathVariable Long id, @RequestParam Boolean estado) {
        return usuarioControlService.cambiarEstado(id, estado);
    }
}
