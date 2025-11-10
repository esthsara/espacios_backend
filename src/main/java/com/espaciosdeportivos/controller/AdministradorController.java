package com.espaciosdeportivos.controller;

import com.espaciosdeportivos.dto.AdministradorDTO;
import com.espaciosdeportivos.dto.UsuarioControlDTO;

import com.espaciosdeportivos.service.AdministradorService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import com.espaciosdeportivos.dto.ClienteDTO;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/administradores")
@RequiredArgsConstructor
public class AdministradorController {

    private final AdministradorService administradorService;
    //solo los activos
    @GetMapping("/activos")
    public List<AdministradorDTO> listarAdminitradoresActivos() {
        return administradorService.obtenerTodoslosAdministradores();
    }

    //Listar todos, incluyendo desactivados 
    @GetMapping
    public List<AdministradorDTO> listarTodos() {
        return administradorService.listarTodos();
    }
    @GetMapping("/{id}")
    public AdministradorDTO obtenerPorId(@PathVariable Long id) {
        return administradorService.obtenerAdministradorPorId(id);
    }

    @GetMapping("/buscar/{nombre}")
    public List<AdministradorDTO> buscarPorNombre(@PathVariable String nombre) {
        return administradorService.buscarPorNombre(nombre);
    }

    @PostMapping
    public AdministradorDTO crear(@Valid @RequestBody AdministradorDTO dto) {
        return administradorService.crearAdministrador(dto);
    }

    @PutMapping("/{id}")
    public AdministradorDTO actualizar(@PathVariable Long id, @RequestBody AdministradorDTO dto) {
        return administradorService.actualizarAdministrador(id, dto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        administradorService.eliminarAdministrador(id);
    }

    @PatchMapping("/{id}/estado")
    public AdministradorDTO cambiarEstado(@PathVariable Long id, @RequestParam Boolean estado) {
        return administradorService.cambiarEstado(id, estado);
    }

    @GetMapping("/buscar/fechas")
    public List<AdministradorDTO> buscarPorRangoFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin
    ) {
        return administradorService.buscarPorRangoFecha(inicio, fin);
    }

    @GetMapping("/buscar")
    public List<AdministradorDTO> buscar(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String aPaterno,
            @RequestParam(required = false) String aMaterno
    ) {
        return administradorService.buscarPorNombreApellidos(nombre, aPaterno, aMaterno);
    }
    /**
     * Obtiene todos los usuarios de control que están asignados a las canchas
     * pertenecientes al área deportiva de un administrador específico.
     * @param id ID del administrador
     * @return Lista de UsuarioControlDTO con información personal y operativa
     */
    @GetMapping("/{id}/usuarios-control")
    public List<UsuarioControlDTO> obtenerUsuariosControlDelAdministrador(@PathVariable Long id) {
        return administradorService.obtenerUsuariosControlPorAdministrador(id);
    }

    //para obtener los clientes que han reservado canchas del administrador
    @GetMapping("/{id}/clientes")
    public List<ClienteDTO> obtenerClientesDelAdministrador(@PathVariable Long id) {
        return administradorService.obtenerClientesPorAdministrador(id);
    }

    //crear un usuario de control desde el administrador
    @PostMapping("/{id}/usuarios-control")
    public UsuarioControlDTO crearUsuarioControlDesdeAdministrador(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioControlDTO dto
    ) {
        return administradorService.crearUsuarioControlParaAdministrador(id, dto);
    }



}
