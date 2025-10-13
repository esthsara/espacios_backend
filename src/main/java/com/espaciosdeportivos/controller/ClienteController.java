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

    // Solo los activos
    @GetMapping("/activos")
    public List<ClienteDTO> listarClientesActivos() {
        return clienteService.obtenerTodoslosClientes();
    }

    // Listar todos, incluyendo desactivados
    @GetMapping
    public List<ClienteDTO> listarTodos() {
        return clienteService.listarTodos();
    }

    @GetMapping("/{id}")
    public ClienteDTO obtenerPorId(@PathVariable Long id) {
        return clienteService.obtenerClientePorId(id);
    }

    @GetMapping("/buscar/{nombre}")
    public List<ClienteDTO> buscarPorNombre(@PathVariable String nombre) {
        return clienteService.buscarPorNombre(nombre);
    }

    @PostMapping
    public ClienteDTO crear(@Valid @RequestBody ClienteDTO dto) {
        return clienteService.crearCliente(dto);
    }

    @PutMapping("/{id}")
    public ClienteDTO actualizar(@PathVariable Long id, @Valid @RequestBody ClienteDTO dto) {
        return clienteService.actualizarCliente(id, dto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        clienteService.eliminarCliente(id);
    }

    @PatchMapping("/{id}/estado")
    public ClienteDTO cambiarEstado(@PathVariable Long id, @RequestParam Boolean estado) {
        return clienteService.cambiarEstado(id, estado);
    }
}
