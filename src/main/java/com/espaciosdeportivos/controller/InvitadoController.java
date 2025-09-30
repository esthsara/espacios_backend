package com.espaciosdeportivos.controller;

import com.espaciosdeportivos.dto.InvitadoDTO;
import com.espaciosdeportivos.service.InvitadoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invitados")
@RequiredArgsConstructor
public class InvitadoController {

    private final InvitadoService invitadoService;

    @PostMapping
    public InvitadoDTO crear(@Valid @RequestBody InvitadoDTO dto) {
        return invitadoService.crearInvitado(dto);
    }

    @GetMapping("/{id}")
    public InvitadoDTO obtenerPorId(@PathVariable Long id) {
        return invitadoService.obtenerPorId(id);
    }

    @GetMapping
    public List<InvitadoDTO> listarTodos() {
        return invitadoService.listarTodos();
    }

    @PutMapping("/{id}")
    public InvitadoDTO actualizar(@PathVariable Long id, @RequestBody InvitadoDTO dto) {
        return invitadoService.actualizarInvitado(id, dto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        invitadoService.eliminarInvitado(id);
    }

    @GetMapping("/buscar/{nombre}")
    public List<InvitadoDTO> buscarPorNombre(@PathVariable String nombre) {
        return invitadoService.buscarPorNombre(nombre);
    }
}
