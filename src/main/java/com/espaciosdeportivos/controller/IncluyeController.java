package com.espaciosdeportivos.controller;


import com.espaciosdeportivos.dto.IncluyeDTO;
import com.espaciosdeportivos.service.IIncluyeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incluye")
@Validated
@RequiredArgsConstructor
public class IncluyeController {

    private final IIncluyeService incluyeService;
    private static final Logger logger = LoggerFactory.getLogger(IncluyeController.class);

    // ---------------------------
    // Crear asociación ternaria
    // ---------------------------
    @PostMapping
    public ResponseEntity<IncluyeDTO> crearAsociacion(@Valid @RequestBody IncluyeDTO dto) {
        logger.info("Creando asociación: Reserva={}, Cancha={}, Disciplina={}",
                dto.getIdReserva(), dto.getIdCancha(), dto.getIdDisciplina());

        IncluyeDTO resultado = incluyeService.asociarCanchaDisciplinaAReserva(dto);
        return ResponseEntity.ok(resultado);
    }

    // ---------------------------
    // Obtener una asociación específica
    // ---------------------------
    @GetMapping("/{idReserva}/{idCancha}/{idDisciplina}")
    public ResponseEntity<IncluyeDTO> obtenerAsociacion(
            @PathVariable Long idReserva,
            @PathVariable Long idCancha,
            @PathVariable Long idDisciplina) {

        logger.debug("Buscando asociación: Reserva={}, Cancha={}, Disciplina={}",
                idReserva, idCancha, idDisciplina);

        IncluyeDTO dto = incluyeService.obtenerPorReservaCanchaDisciplina(idReserva, idCancha, idDisciplina);
        return ResponseEntity.ok(dto);
    }

    // ---------------------------
    // Eliminar una asociación
    // ---------------------------
    @DeleteMapping("/{idReserva}/{idCancha}/{idDisciplina}")
    public ResponseEntity<Void> eliminarAsociacion(
            @PathVariable Long idReserva,
            @PathVariable Long idCancha,
            @PathVariable Long idDisciplina) {

        logger.info("Eliminando asociación: Reserva={}, Cancha={}, Disciplina={}",
                idReserva, idCancha, idDisciplina);

        incluyeService.desasociarCanchaDisciplinaDeReserva(idReserva, idCancha, idDisciplina);
        return ResponseEntity.noContent().build();
    }

    // ---------------------------
    // Listar todas las asociaciones de una RESERVA
    // ---------------------------
    @GetMapping("/por-reserva/{idReserva}")
    public ResponseEntity<List<IncluyeDTO>> listarPorReserva(@PathVariable Long idReserva) {
        logger.debug("Listando asociaciones para reserva ID: {}", idReserva);
        List<IncluyeDTO> lista = incluyeService.obtenerPorReserva(idReserva);
        return ResponseEntity.ok(lista);
    }

    // ---------------------------
    // Listar todas las asociaciones de una CANCHA
    // ---------------------------
    @GetMapping("/por-cancha/{idCancha}")
    public ResponseEntity<List<IncluyeDTO>> listarPorCancha(@PathVariable Long idCancha) {
        logger.debug("Listando asociaciones para cancha ID: {}", idCancha);
        List<IncluyeDTO> lista = incluyeService.obtenerPorCancha(idCancha);
        return ResponseEntity.ok(lista);
    }

    // ---------------------------
    // Listar todas las asociaciones de una DISCIPLINA
    // ---------------------------
    @GetMapping("/por-disciplina/{idDisciplina}")
    public ResponseEntity<List<IncluyeDTO>> listarPorDisciplina(@PathVariable Long idDisciplina) {
        logger.debug("Listando asociaciones para disciplina ID: {}", idDisciplina);
        List<IncluyeDTO> lista = incluyeService.obtenerPorDisciplina(idDisciplina);
        return ResponseEntity.ok(lista);
    }

    //
    // ---------------------------
    // Obtener monto total de una asociación específica
    // ---------------------------

    @GetMapping("/monto-total")
    public ResponseEntity<Double> obtenerMontoTotal(
            @RequestParam Long idReserva,
            @RequestParam Long idCancha,
            @RequestParam Long idDisciplina) {

        Double monto = incluyeService.obtenerMontoTotal(idReserva, idCancha, idDisciplina);
        return ResponseEntity.ok(monto);
    }
    
}

