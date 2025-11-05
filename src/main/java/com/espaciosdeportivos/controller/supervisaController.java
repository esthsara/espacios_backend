package com.espaciosdeportivos.controller;

import com.espaciosdeportivos.dto.CanchaDTO;
import com.espaciosdeportivos.dto.UsuarioControlDTO;
import com.espaciosdeportivos.service.ISupervisaService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/supervisa")
@Validated
@RequiredArgsConstructor
public class SupervisaController {

    private final ISupervisaService supervisaService;

    // Asignar una cancha a un usuario de control
    @PostMapping
    public ResponseEntity<Void> asignarCanchaASupervisor(
            @RequestParam @NotNull Long idUsuarioControl,
            @RequestParam @NotNull Long idCancha) {
        supervisaService.asignarCanchaASupervisor(idUsuarioControl, idCancha);
        return ResponseEntity.ok().build();
    }

    // Quitar una cancha de un usuario de control
    @DeleteMapping
    public ResponseEntity<Void> quitarCanchaDeSupervisor(
            @RequestParam @NotNull Long idUsuarioControl,
            @RequestParam @NotNull Long idCancha) {
        supervisaService.quitarCanchaDeSupervisor(idUsuarioControl, idCancha);
        return ResponseEntity.noContent().build();
    }

    // Ver todas las canchas que supervisa un usuario
    @GetMapping("/usuario/{idUsuarioControl}/canchas")
    public ResponseEntity<List<CanchaDTO>> obtenerCanchasSupervisadasPorUsuario(@PathVariable Long idUsuarioControl) {
        List<CanchaDTO> canchas = supervisaService.obtenerCanchasSupervisadasPorUsuario(idUsuarioControl);
        return ResponseEntity.ok(canchas);
    }

    // Ver todos los usuarios que supervisan una cancha
    @GetMapping("/cancha/{idCancha}/usuarios")
    public ResponseEntity<List<UsuarioControlDTO>> obtenerSupervisoresDeCancha(@PathVariable Long idCancha) {
        List<UsuarioControlDTO> usuarios = supervisaService.obtenerSupervisoresDeCancha(idCancha);
        return ResponseEntity.ok(usuarios);
    }
}
