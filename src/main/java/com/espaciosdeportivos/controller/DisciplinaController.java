package com.espaciosdeportivos.controller;

import com.espaciosdeportivos.dto.DisciplinaDTO;
import com.espaciosdeportivos.dto.EquipamientoDTO;
import com.espaciosdeportivos.service.IDisciplinaService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/disciplina")
@RequiredArgsConstructor
public class DisciplinaController {
    
    private final IDisciplinaService disciplinaService;
    //json 
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DisciplinaDTO> crear(@RequestBody DisciplinaDTO disciplinaDTO) {
        DisciplinaDTO nueva = disciplinaService.crearDisciplina(disciplinaDTO);
        return ResponseEntity.ok(nueva);
    }
    //como un formulario html
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DisciplinaDTO> createDisciplina(
            @Valid @ModelAttribute DisciplinaDTO disciplinaDTO) {
        log.info("POST /api/disciplinas - Creando disciplina: {}", disciplinaDTO.getNombre());
        
        try {
            DisciplinaDTO response = disciplinaService.crearDisciplina(disciplinaDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("Error creando disciplina: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    
    @GetMapping("/{idDisciplina}")
    public ResponseEntity<DisciplinaDTO> obtenerDisciplina(@PathVariable Long idDisciplina) {
        log.info("GET /api/disciplinas/{}", idDisciplina);
        
        try {
            DisciplinaDTO response = disciplinaService.obtenerDisciplinaPorId(idDisciplina);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error obteniendo disciplina: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping
    public ResponseEntity<List<DisciplinaDTO>> ListarTodos() {
        log.info("GET /api/disciplina");
        
        try {
            List<DisciplinaDTO> response = disciplinaService.listarTodas();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error obteniendo disciplinas: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
    @GetMapping("/activos")
    public ResponseEntity<List<DisciplinaDTO>> obtenerTodasLasDisciplinas() {
        log.info("GET /api/disciplina");
        
        try {
            List<DisciplinaDTO> response = disciplinaService.obtenerTodasLasDisciplinas();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error obteniendo disciplinas activas: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DisciplinaDTO> updateDiciplina(
            @PathVariable Long id,
            @RequestBody DisciplinaDTO disciplinaDTO) {
        DisciplinaDTO actualizada = disciplinaService.actualizarDisciplina(id, disciplinaDTO);
        return ResponseEntity.ok(actualizada);
    }

    
    @PutMapping(value = "/{idDisciplina}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DisciplinaDTO> actualizar(
            @PathVariable Long idDisciplina,
            @Valid @ModelAttribute DisciplinaDTO disciplinaDTO) {
        log.info("PUT /api/disciplinas/{}", idDisciplina);
        
        try {
            DisciplinaDTO response = disciplinaService.actualizarDisciplina(idDisciplina, disciplinaDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error actualizando disciplina: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{idDisciplina}/eliminar-logico")
    public ResponseEntity<Void> deleteDiciplina(@PathVariable Long idDisciplina) {
        log.info("PUT /api/disciplinas/{}/eliminar-logico", idDisciplina);
        
        try {
            disciplinaService.eliminarDisciplinaLogicamente(idDisciplina);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error eliminando lógicamente disciplina: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    //eliminacion con el patch 
    @PatchMapping("/{id}/estado")
    @Transactional
    public ResponseEntity<DisciplinaDTO> eliminar(@PathVariable Long id, @RequestParam Boolean nuevoEstado) {
        log.info("[DISCIPLINA] Inicio cambiar estado equipamiento: {}", id);
        DisciplinaDTO actualizada = disciplinaService.eliminar(id, nuevoEstado);
        log.info("[DISCIPLINA] Inicio cambiar estado equipamiento: {}", id);
        return ResponseEntity.ok(actualizada);
    }
    
    @DeleteMapping("/{idDisciplina}")
    public ResponseEntity<Void> eliminarFisico(@PathVariable Long idDisciplina) {
        log.info("DELETE /api/disciplinas/{}", idDisciplina);
        
        try {
            disciplinaService.eliminarDisciplinaFisicamente(idDisciplina);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error eliminando físicamente disciplina: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }


    //---------------NUEVOS -----------------
    // BÚSQUEDA Y FILTROS
    @GetMapping("/buscar/nombre")
    public ResponseEntity<List<DisciplinaDTO>> buscarPorNombre(@RequestParam String nombre) {
        log.info("GET /api/disciplinas/buscar/nombre?nombre={}", nombre);
        List<DisciplinaDTO> response = disciplinaService.buscarPorNombre(nombre);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/buscar/descripcion")
    public ResponseEntity<List<DisciplinaDTO>> buscarPorDescripcion(@RequestParam String descripcion) {
        log.info("GET /api/disciplinas/buscar/descripcion?descripcion={}", descripcion);
        List<DisciplinaDTO> response = disciplinaService.buscarPorDescripcion(descripcion);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/inactivas")
    public ResponseEntity<List<DisciplinaDTO>> obtenerInactivas() {
        log.info("GET /api/disciplinas/inactivas");
        List<DisciplinaDTO> response = disciplinaService.obtenerDisciplinasInactivas();
        return ResponseEntity.ok(response);
    }

    // 🆕 GESTIÓN DE ESTADO
    @PutMapping("/{idDisciplina}/activar")
    public ResponseEntity<DisciplinaDTO> activarDisciplina(@PathVariable Long idDisciplina) {
        log.info("PUT /api/disciplinas/{}/activar", idDisciplina);
        DisciplinaDTO response = disciplinaService.activarDisciplina(idDisciplina);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/desactivar-masivo")
    public ResponseEntity<Void> desactivarMasivo(@RequestBody List<Long> idsDisciplinas) {
        log.info("PUT /api/disciplinas/desactivar-masivo - {} disciplinas", idsDisciplinas.size());
        disciplinaService.desactivarMasivo(idsDisciplinas);
        return ResponseEntity.noContent().build();
    }

    // GESTIÓN DE IMÁGENES ESPECÍFICA
    @PostMapping(value = "/{idDisciplina}/imagenes", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DisciplinaDTO> agregarImagenes(
            @PathVariable Long idDisciplina,
            @RequestParam List<MultipartFile> archivosImagenes) {
        log.info("POST /api/disciplinas/{}/imagenes - {} archivos", idDisciplina, archivosImagenes.size());
        DisciplinaDTO response = disciplinaService.agregarImagenes(idDisciplina, archivosImagenes);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{idDisciplina}/imagenes/{idImagenRelacion}")
    public ResponseEntity<DisciplinaDTO> eliminarImagen(
            @PathVariable Long idDisciplina,
            @PathVariable Long idImagenRelacion) {
        log.info("DELETE /api/disciplinas/{}/imagenes/{}", idDisciplina, idImagenRelacion);
        DisciplinaDTO response = disciplinaService.eliminarImagen(idDisciplina, idImagenRelacion);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{idDisciplina}/imagenes/reordenar")
    public ResponseEntity<DisciplinaDTO> reordenarImagenes(
            @PathVariable Long idDisciplina,
            @RequestBody List<Long> idsImagenesOrden) {
        log.info("PUT /api/disciplinas/{}/imagenes/reordenar - {} imágenes", idDisciplina, idsImagenesOrden.size());
        DisciplinaDTO response = disciplinaService.reordenarImagenes(idDisciplina, idsImagenesOrden);
        return ResponseEntity.ok(response);
    }

    // VALIDACIONES
    @GetMapping("/verificar-nombre/{nombre}")
    public ResponseEntity<Boolean> verificarNombreDisponible(@PathVariable String nombre) {
        log.info("GET /api/disciplinas/verificar-nombre/{}", nombre);
        boolean disponible = disciplinaService.verificarNombreDisponible(nombre);
        return ResponseEntity.ok(disponible);
    }

    /*@GetMapping("/{idDisciplina}/puede-eliminarse")
    public ResponseEntity<Boolean> puedeEliminarse(@PathVariable Long idDisciplina) {
        log.info("GET /api/disciplinas/{}/puede-eliminarse", idDisciplina);
        boolean puedeEliminarse = disciplinaService.puedeEliminarse(idDisciplina);
        return ResponseEntity.ok(puedeEliminarse);
    }*/

    // ESTADÍSTICAS
    @GetMapping("/estadisticas/total-activas")
    public ResponseEntity<Long> contarActivas() {
        log.info("GET /api/disciplinas/estadisticas/total-activas");
        long total = disciplinaService.contarDisciplinasActivas();
        return ResponseEntity.ok(total);
    }

    @GetMapping("/recientes")
    public ResponseEntity<List<DisciplinaDTO>> obtenerRecientes(
            @RequestParam(defaultValue = "5") int limite) {
        log.info("GET /api/disciplinas/recientes?limite={}", limite);
        List<DisciplinaDTO> response = disciplinaService.obtenerRecientes(limite);
        return ResponseEntity.ok(response);
    }
}