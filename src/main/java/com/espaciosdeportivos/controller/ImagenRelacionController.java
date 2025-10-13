package com.espaciosdeportivos.controller;

import com.espaciosdeportivos.dto.ImagenRelacionDTO;
import com.espaciosdeportivos.service.ImagenRelacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/imagenes-relaciones")
@RequiredArgsConstructor
@Tag(name = "Gestión de Relaciones de Imágenes", description = "API para gestión de relaciones entre imágenes y entidades")
public class ImagenRelacionController {

    private final ImagenRelacionService imagenRelacionService;

    @Operation(summary = "Crear relación de imagen", description = "Crea una nueva relación entre una imagen y una entidad")
    @PostMapping
    public ResponseEntity<ImagenRelacionDTO> crearRelacion(
            @RequestBody ImagenRelacionDTO relacionDTO) {
        
        log.info(" nueva relación de imagen");
        
        try {
            ImagenRelacionDTO relacionCreada = imagenRelacionService.crearRelacion(relacionDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(relacionCreada);
        } catch (Exception e) {
            log.error("Error creando relación: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Obtener relación por ID", description = "Recupera los detalles de una relación específica por su ID")
    @GetMapping("/{idImagenRelacion}")
    public ResponseEntity<ImagenRelacionDTO> obtenerRelacionPorId(
            @Parameter(description = "ID de la relación de imagen") 
            @PathVariable Long idImagenRelacion) {
        
        log.info("Obteniendo relación por ID: {}", idImagenRelacion);
        
        try {
            ImagenRelacionDTO relacion = imagenRelacionService.obtenerRelacionPorId(idImagenRelacion);
            return ResponseEntity.ok(relacion);
        } catch (Exception e) {
            log.error("Error obteniendo relación: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Obtener relaciones por entidad", description = "Recupera todas las relaciones de una entidad específica")
    @GetMapping("/entidad/{entidadTipo}/{entidadId}")
    public ResponseEntity<List<ImagenRelacionDTO>> obtenerRelacionesPorEntidad(
            @Parameter(description = "Tipo de entidad") 
            @PathVariable String entidadTipo,
            @Parameter(description = "ID de la entidad") 
            @PathVariable Long entidadId) {
        
        log.info("Obteniendo relaciones para entidad {}:{}", entidadTipo, entidadId);
        
        try {
            List<ImagenRelacionDTO> relaciones = imagenRelacionService.obtenerRelacionesPorEntidad(entidadTipo, entidadId);
            return ResponseEntity.ok(relaciones);
        } catch (Exception e) {
            log.error("Error obteniendo relaciones: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Obtener relaciones activas por entidad", description = "Recupera solo las relaciones activas de una entidad")
    @GetMapping("/entidad/{entidadTipo}/{entidadId}/activas")
    public ResponseEntity<List<ImagenRelacionDTO>> obtenerRelacionesActivasPorEntidad(
            @Parameter(description = "Tipo de entidad") 
            @PathVariable String entidadTipo,
            @Parameter(description = "ID de la entidad") 
            @PathVariable Long entidadId) {
        
        log.info("Obteniendo relaciones activas para entidad {}:{}", entidadTipo, entidadId);
        
        try {
            List<ImagenRelacionDTO> relaciones = imagenRelacionService.obtenerRelacionesActivasPorEntidad(entidadTipo, entidadId);
            return ResponseEntity.ok(relaciones);
        } catch (Exception e) {
            log.error("Error obteniendo relaciones activas: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Actualizar relación", description = "Actualiza los detalles de una relación existente")
    @PutMapping("/{idImagenRelacion}")
    public ResponseEntity<ImagenRelacionDTO> actualizarRelacion(
            @Parameter(description = "ID de la relación de imagen") 
            @PathVariable Long idImagenRelacion,
            @RequestBody ImagenRelacionDTO relacionDTO) {
        
        log.info("Actualizando relación ID: {}", idImagenRelacion);
        
        try {
            ImagenRelacionDTO relacionActualizada = imagenRelacionService.actualizarRelacion(idImagenRelacion, relacionDTO);
            return ResponseEntity.ok(relacionActualizada);
        } catch (Exception e) {
            log.error("Error actualizando relación: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Actualizar orden de relación", description = "Cambia el orden de una relación específica")
    @PutMapping("/{idImagenRelacion}/orden")
    public ResponseEntity<ImagenRelacionDTO> actualizarOrdenRelacion(
            @Parameter(description = "ID de la relación de imagen") 
            @PathVariable Long idImagenRelacion,
            @Parameter(description = "Nuevo orden") 
            @RequestParam Integer nuevoOrden) {
        
        log.info("Actualizando orden de relación {} a {}", idImagenRelacion, nuevoOrden);
        
        try {
            ImagenRelacionDTO relacionActualizada = imagenRelacionService.actualizarOrdenRelacion(idImagenRelacion, nuevoOrden);
            return ResponseEntity.ok(relacionActualizada);
        } catch (Exception e) {
            log.error("Error actualizando orden: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Reordenar relaciones", description = "Reordena todas las relaciones de una entidad según una lista especificada")
    @PutMapping("/reordenar/{entidadTipo}/{entidadId}")
    public ResponseEntity<Void> reordenarRelaciones(
            @Parameter(description = "Tipo de entidad") 
            @PathVariable String entidadTipo,
            @Parameter(description = "ID de la entidad") 
            @PathVariable Long entidadId,
            @Parameter(description = "Lista de IDs de relaciones en el nuevo orden") 
            @RequestBody List<Long> idsEnOrden) {
        
        log.info("Reordenando relaciones para entidad {}:{}", entidadTipo, entidadId);
        
        try {
            imagenRelacionService.reordenarRelaciones(entidadTipo, entidadId, idsEnOrden);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error reordenando relaciones: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Intercambiar posiciones", description = "Intercambia las posiciones de dos relaciones")
    @PutMapping("/intercambiar")
    public ResponseEntity<Void> intercambiarPosiciones(
            @Parameter(description = "ID de la primera relación") 
            @RequestParam Long idImagenRelacion1,
            @Parameter(description = "ID de la segunda relación") 
            @RequestParam Long idImagenRelacion2) {
        
        log.info("Intercambiando posiciones de relaciones {} y {}", idImagenRelacion1, idImagenRelacion2);
        
        try {
            imagenRelacionService.intercambiarPosiciones(idImagenRelacion1, idImagenRelacion2);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error intercambiando posiciones: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Eliminar relación (lógico)", description = "Desactiva una relación pero mantiene el registro")
    @DeleteMapping("/logico/{idImagenRelacion}")
    public ResponseEntity<Void> eliminarRelacionLogicamente(
            @Parameter(description = "ID de la relación de imagen") 
            @PathVariable Long idImagenRelacion) {
        
        log.info("Eliminando lógicamente relación: {}", idImagenRelacion);
        
        try {
            imagenRelacionService.eliminarRelacionLogicamente(idImagenRelacion);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error eliminando relación: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Eliminar relación (físico)", description = "Elimina completamente una relación")
    @DeleteMapping("/fisico/{idImagenRelacion}")
    public ResponseEntity<Void> eliminarRelacionFisicamente(
            @Parameter(description = "ID de la relación de imagen") 
            @PathVariable Long idImagenRelacion) {
        
        log.info("🗑️ Eliminando físicamente relación: {}", idImagenRelacion);
        
        try {
            imagenRelacionService.eliminarRelacionFisicamente(idImagenRelacion);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error eliminando relación: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Migrar relación", description = "Mueve una relación de una entidad a otra")
    @PutMapping("/migrar/{idImagenRelacion}")
    public ResponseEntity<ImagenRelacionDTO> migrarRelacion(
            @Parameter(description = "ID de la relación de imagen") 
            @PathVariable Long idImagenRelacion,
            @Parameter(description = "Nuevo tipo de entidad") 
            @RequestParam String nuevaEntidadTipo,
            @Parameter(description = "Nuevo ID de entidad") 
            @RequestParam Long nuevaEntidadId) {
        
        log.info("Migrando relación {} a {}:{}", idImagenRelacion, nuevaEntidadTipo, nuevaEntidadId);
        
        try {
            ImagenRelacionDTO relacionMigrada = imagenRelacionService.migrarRelacion(idImagenRelacion, nuevaEntidadTipo, nuevaEntidadId);
            return ResponseEntity.ok(relacionMigrada);
        } catch (Exception e) {
            log.error("Error migrando relación: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Transferir relaciones", description = "Transfiere todas las relaciones de una entidad a otra")
    @PutMapping("/transferir")
    public ResponseEntity<Void> transferirTodasLasRelaciones(
            @Parameter(description = "Tipo de entidad origen") 
            @RequestParam String entidadTipoOrigen,
            @Parameter(description = "ID de entidad origen") 
            @RequestParam Long entidadIdOrigen,
            @Parameter(description = "Tipo de entidad destino") 
            @RequestParam String entidadTipoDestino,
            @Parameter(description = "ID de entidad destino") 
            @RequestParam Long entidadIdDestino) {
        
        log.info("Transferiendo relaciones de {}:{} a {}:{}", 
                entidadTipoOrigen, entidadIdOrigen, entidadTipoDestino, entidadIdDestino);
        
        try {
            imagenRelacionService.transferirTodasLasRelaciones(entidadTipoOrigen, entidadIdOrigen, entidadTipoDestino, entidadIdDestino);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error transfiriendo relaciones: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Clonar relaciones", description = "Clona todas las relaciones de una entidad a otra")
    @PostMapping("/clonar")
    public ResponseEntity<Void> clonarRelaciones(
            @Parameter(description = "Tipo de entidad origen") 
            @RequestParam String entidadTipoOrigen,
            @Parameter(description = "ID de entidad origen") 
            @RequestParam Long entidadIdOrigen,
            @Parameter(description = "Tipo de entidad destino") 
            @RequestParam String entidadTipoDestino,
            @Parameter(description = "ID de entidad destino") 
            @RequestParam Long entidadIdDestino) {
        
        log.info("Clonando relaciones de {}:{} a {}:{}", 
                entidadTipoOrigen, entidadIdOrigen, entidadTipoDestino, entidadIdDestino);
        
        try {
            imagenRelacionService.clonarRelaciones(entidadTipoOrigen, entidadIdOrigen, entidadTipoDestino, entidadIdDestino);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error clonando relaciones: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Obtener estadísticas por tipo de entidad", description = "Obtiene estadísticas de uso de imágenes por tipo de entidad")
    @GetMapping("/estadisticas/tipo-entidad")
    public ResponseEntity<List<Object[]>> obtenerEstadisticasPorTipoEntidad() {
        log.info("Obteniendo estadísticas por tipo de entidad");
        
        try {
            List<Object[]> estadisticas = imagenRelacionService.obtenerEstadisticasPorTipoEntidad();
            return ResponseEntity.ok(estadisticas);
        } catch (Exception e) {
            log.error("Error obteniendo estadísticas: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Encontrar relaciones duplicadas", description = "Encuentra relaciones duplicadas en el sistema")
    @GetMapping("/duplicados")
    public ResponseEntity<List<ImagenRelacionDTO>> encontrarRelacionesDuplicadas() {
        log.info("🔍 Buscando relaciones duplicadas");
        
        try {
            List<ImagenRelacionDTO> duplicados = imagenRelacionService.encontrarRelacionesDuplicadas();
            return ResponseEntity.ok(duplicados);
        } catch (Exception e) {
            log.error("Error buscando duplicados: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Validar integridad de relaciones", description = "Valida la integridad de todas las relaciones y reporta problemas")
    @GetMapping("/validar-integridad")
    public ResponseEntity<List<String>> validarIntegridadRelaciones() {
        log.info("🔍 Validando integridad de relaciones");
        
        try {
            List<String> problemas = imagenRelacionService.validarIntegridadRelaciones();
            return ResponseEntity.ok(problemas);
        } catch (Exception e) {
            log.error("Error validando integridad: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Limpiar relaciones inactivas", description = "Elimina relaciones que están marcadas como inactivas")
    @PostMapping("/limpiar/inactivas")
    public ResponseEntity<Void> limpiarRelacionesInactivas() {
        log.info("Limpiando relaciones inactivas");
        
        try {
            imagenRelacionService.limpiarRelacionesInactivas();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error limpiando relaciones: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Reindexar órdenes", description = "Reindexa los órdenes de las relaciones de una entidad")
    @PutMapping("/reindexar/{entidadTipo}/{entidadId}")
    public ResponseEntity<Void> reindexarOrdenes(
            @Parameter(description = "Tipo de entidad") 
            @PathVariable String entidadTipo,
            @Parameter(description = "ID de la entidad") 
            @PathVariable Long entidadId) {
        
        log.info("Reindexando órdenes para entidad {}:{}", entidadTipo, entidadId);
        
        try {
            imagenRelacionService.reindexarOrdenes(entidadTipo, entidadId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error reindexando órdenes: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Verificar si imagen está en uso", description = "Verifica si una imagen específica está siendo usada en alguna relación activa")
    @GetMapping("/imagen/{idImagen}/en-uso")
    public ResponseEntity<Boolean> estaImagenEnUso(
            @Parameter(description = "ID de la imagen") 
            @PathVariable Long idImagen) {
        
        log.info("🔍 Verificando si imagen {} está en uso", idImagen);
        
        try {
            boolean enUso = imagenRelacionService.estaImagenEnUso(idImagen);
            return ResponseEntity.ok(enUso);
        } catch (Exception e) {
            log.error("Error verificando uso: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Contar entidades que usan imagen", description = "Cuenta cuántas entidades diferentes están usando una imagen específica")
    @GetMapping("/imagen/{idImagen}/entidades-count")
    public ResponseEntity<Integer> contarEntidadesQueUsanImagen(
            @Parameter(description = "ID de la imagen") 
            @PathVariable Long idImagen) {
        
        log.info("Contando entidades que usan imagen: {}", idImagen);
        
        try {
            int count = imagenRelacionService.contarEntidadesQueUsanImagen(idImagen);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            log.error("Error contando entidades: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}