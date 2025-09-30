package com.espaciosdeportivos.controller;

import com.espaciosdeportivos.dto.ClienteDTO;
import com.espaciosdeportivos.service.ClienteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    public ClienteDTO crear(@Valid @RequestBody ClienteDTO dto) {
        return clienteService.crearCliente(dto);
    }

    @GetMapping("/{id}")
    public ClienteDTO obtenerPorId(@PathVariable Long id) {
        return clienteService.obtenerPorId(id);
    }

    @GetMapping
    public List<ClienteDTO> listarTodos() {
        return clienteService.listarTodos();
    }

    @PutMapping("/{id}")
    public ClienteDTO actualizar(@PathVariable Long id, @RequestBody ClienteDTO dto) {
        return clienteService.actualizarCliente(id, dto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        clienteService.eliminarCliente(id);
    }

    @GetMapping("/buscar/{nombre}")
    public List<ClienteDTO> buscarPorNombre(@PathVariable String nombre) {
        return clienteService.buscarPorNombre(nombre);
    }
}
