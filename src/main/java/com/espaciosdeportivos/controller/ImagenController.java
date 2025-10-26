package com.espaciosdeportivos.controller;

import com.espaciosdeportivos.dto.ImagenDTO;
import com.espaciosdeportivos.service.ImagenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
//import org.springframework.core.io.Resource;
//import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/imagenes")
@RequiredArgsConstructor
@Tag(name = "Gestión de Imágenes", description = "API para gestión completa de imágenes y sus relaciones")
public class ImagenController {

    private final ImagenService imagenService;

    @Operation(summary = "Subir imágenes para una entidad", description = "Sube una o múltiples imágenes y las asocia a una entidad específica")
    @PostMapping(value = "/subir/{entidadTipo}/{entidadId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<ImagenDTO>> subirImagenes(
            @Parameter(description = "Tipo de entidad (ej: instalacion, usuario, evento)") 
            @PathVariable String entidadTipo,
            @Parameter(description = "ID de la entidad") 
            @PathVariable Long entidadId,
            @Parameter(description = "Archivos de imagen a subir") 
            @RequestParam("archivos") List<MultipartFile> archivos) {
        
        log.info("Subiendo {} imágenes para entidad {}:{}", archivos.size(), entidadTipo, entidadId);
        
        try {
            List<ImagenDTO> imagenesGuardadas = imagenService.guardarImagenesParaEntidad(archivos, entidadTipo, entidadId);
            return ResponseEntity.ok(imagenesGuardadas);
        } catch (Exception e) {
            log.error("Error subiendo imágenes: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Obtener imágenes de una entidad", description = "Recupera todas las imágenes activas asociadas a una entidad específica")
    @GetMapping("/entidad/{entidadTipo}/{entidadId}")
    public ResponseEntity<List<ImagenDTO>> obtenerImagenesPorEntidad(
            @Parameter(description = "Tipo de entidad") 
            @PathVariable String entidadTipo,
            @Parameter(description = "ID de la entidad") 
            @PathVariable Long entidadId) {
        
        log.info("Obteniendo imágenes para entidad {}:{}", entidadTipo, entidadId);
        
        try {
            List<ImagenDTO> imagenes = imagenService.obtenerImagenesPorEntidad(entidadTipo, entidadId);
            return ResponseEntity.ok(imagenes);
        } catch (Exception e) {
            log.error("Error obteniendo imágenes: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

   /* @Operation(summary = "Descargar archivo de imagen", description = "Descarga el archivo físico de una imagen por su ruta de almacenamiento")
    @GetMapping("/archivo/{rutaArchivo:.+}")
    public ResponseEntity<Resource> descargarArchivo(
            @Parameter(description = "Ruta completa del archivo") 
            @PathVariable String rutaArchivo) {
        
        log.info("Descargando archivo: {}", rutaArchivo);
        
        try {
            //CREAR METODO EN IMAGENSERVICE
            Resource resource = imagenService.cargarArchivo(rutaArchivo);
            
            String contentType = "application/octet-stream";
            String filename = rutaArchivo.substring(rutaArchivo.lastIndexOf("/") + 1);
            
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .body(resource);
        } catch (Exception e) {
            log.error("Error descargando archivo: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }*/

    @Operation(summary = "Eliminar imagen (lógico)", description = "Desactiva una imagen pero mantiene el archivo físico y el registro")
    @DeleteMapping("/logico/{idImagenRelacion}")
    public ResponseEntity<Void> eliminarImagenLogicamente(
            @Parameter(description = "ID de la relación de imagen") 
            @PathVariable Long idImagenRelacion) {
        
        log.info("🗑️ Eliminando lógicamente imagen relación: {}", idImagenRelacion);
        
        try {
            imagenService.eliminarImagenLogicamente(idImagenRelacion);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error eliminando imagen: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Eliminar imagen (físico)", description = "Elimina completamente la imagen, tanto el archivo físico como los registros")
    @DeleteMapping("/fisico/{idImagenRelacion}")
    public ResponseEntity<Void> eliminarImagenFisicamente(
            @Parameter(description = "ID de la relación de imagen") 
            @PathVariable Long idImagenRelacion) {
        
        log.info("Eliminando físicamente imagen relación: {}", idImagenRelacion);
        
        try {
            imagenService.eliminarImagenFisicamente(idImagenRelacion);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error eliminando imagen: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Eliminar todas las imágenes de una entidad", description = "Elimina todas las imágenes asociadas a una entidad específica")
    @DeleteMapping("/entidad/{entidadTipo}/{entidadId}")
    public ResponseEntity<Void> eliminarTodasImagenesDeEntidad(
            @Parameter(description = "Tipo de entidad") 
            @PathVariable String entidadTipo,
            @Parameter(description = "ID de la entidad") 
            @PathVariable Long entidadId) {
        
        log.info("Eliminando todas las imágenes de entidad {}:{}", entidadTipo, entidadId);
        
        try {
            imagenService.eliminarTodasImagenesDeEntidad(entidadTipo, entidadId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error eliminando imágenes: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Obtener imagen por ID", description = "Recupera los detalles de una imagen específica por su ID")
    @GetMapping("/{idImagen}")
    public ResponseEntity<ImagenDTO> obtenerImagenPorId(
            @Parameter(description = "ID de la imagen") 
            @PathVariable Long idImagen) {
        
        log.info("Obteniendo imagen por ID: {}", idImagen);
        
        try {
            ImagenDTO imagen = imagenService.obtenerImagenPorId(idImagen);
            return ResponseEntity.ok(imagen);
        } catch (Exception e) {
            log.error("Error obteniendo imagen: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Obtener todas las imágenes activas", description = "Recupera todas las imágenes activas del sistema")
    @GetMapping("/activas")
    public ResponseEntity<List<ImagenDTO>> obtenerTodasLasImagenesActivas() {
        log.info("Obteniendo todas las imágenes activas");
        
        try {
            List<ImagenDTO> imagenes = imagenService.obtenerTodasLasImagenesActivas();
            return ResponseEntity.ok(imagenes);
        } catch (Exception e) {
            log.error("Error obteniendo imágenes: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Actualizar información de imagen", description = "Actualiza la información descriptiva de una imagen")
    @PutMapping("/{idImagen}")
    public ResponseEntity<ImagenDTO> actualizarImagen(
            @Parameter(description = "ID de la imagen") 
            @PathVariable Long idImagen,
            @RequestBody ImagenDTO imagenDTO) {
        
        log.info("Actualizando imagen ID: {}", idImagen);
        
        try {
            ImagenDTO imagenActualizada = imagenService.actualizarImagen(idImagen, imagenDTO);
            return ResponseEntity.ok(imagenActualizada);
        } catch (Exception e) {
            log.error("Error actualizando imagen: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Buscar imágenes por nombre", description = "Busca imágenes cuyo nombre contenga el texto especificado")
    @GetMapping("/buscar/nombre")
    public ResponseEntity<List<ImagenDTO>> buscarImagenesPorNombre(
            @Parameter(description = "Texto a buscar en el nombre") 
            @RequestParam String nombre) {
        
        log.info("Buscando imágenes por nombre: {}", nombre);
        
        try {
            List<ImagenDTO> imagenes = imagenService.buscarImagenesPorNombre(nombre);
            return ResponseEntity.ok(imagenes);
        } catch (Exception e) {
            log.error("Error buscando imágenes: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Buscar imágenes por tipo MIME", description = "Busca imágenes por su tipo MIME")
    @GetMapping("/buscar/tipo-mime")
    public ResponseEntity<List<ImagenDTO>> buscarImagenesPorTipoMime(
            @Parameter(description = "Tipo MIME a buscar") 
            @RequestParam String tipoMime) {
        
        log.info("Buscando imágenes por tipo MIME: {}", tipoMime);
        
        try {
            List<ImagenDTO> imagenes = imagenService.buscarImagenesPorTipoMime(tipoMime);
            return ResponseEntity.ok(imagenes);
        } catch (Exception e) {
            log.error("Error buscando imágenes: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Reordenar imágenes", description = "Cambia el orden de las imágenes de una entidad")
    @PutMapping("/reordenar/{entidadTipo}/{entidadId}")
    public ResponseEntity<Void> reordenarImagenes(
            @Parameter(description = "Tipo de entidad") 
            @PathVariable String entidadTipo,
            @Parameter(description = "ID de la entidad") 
            @PathVariable Long entidadId,
            @Parameter(description = "Lista de IDs de relaciones en el nuevo orden") 
            @RequestBody List<Long> idsImagenesRelacionEnOrden) {
        
        log.info("Reordenando imágenes para entidad {}:{}", entidadTipo, entidadId);
        
        try {
            imagenService.reordenarImagenes(entidadTipo, entidadId, idsImagenesRelacionEnOrden);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error reordenando imágenes: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Migrar imagen entre entidades", description = "Mueve una imagen de una entidad a otra")
    @PutMapping("/migrar/{idImagenRelacion}")
    public ResponseEntity<Void> migrarImagenEntreEntidades(
            @Parameter(description = "ID de la relación de imagen") 
            @PathVariable Long idImagenRelacion,
            @Parameter(description = "Nuevo tipo de entidad") 
            @RequestParam String nuevaEntidadTipo,
            @Parameter(description = "Nuevo ID de entidad") 
            @RequestParam Long nuevaEntidadId) {
        
        log.info("Migrando imagen relación {} a {}:{}", idImagenRelacion, nuevaEntidadTipo, nuevaEntidadId);
        
        try {
            imagenService.migrarImagenEntreEntidades(idImagenRelacion, nuevaEntidadTipo, nuevaEntidadId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error migrando imagen: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Limpiar imágenes no utilizadas", description = "Elimina imágenes que no están siendo utilizadas por ninguna entidad")
    @PostMapping("/limpiar/no-utilizadas")
    public ResponseEntity<Void> limpiarImagenesNoUtilizadas() {
        log.info("Limpiando imágenes no utilizadas");
        
        try {
            imagenService.limpiarImagenesNoUtilizadas();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error limpiando imágenes: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Obtener estadísticas de uso", description = "Obtiene estadísticas sobre el uso de imágenes en el sistema")
    @GetMapping("/estadisticas")
    public ResponseEntity<Long> obtenerEstadisticasUsoImagenes() {
        log.info("Obteniendo estadísticas de uso de imágenes");
        
        try {
            Long estadisticas = imagenService.obtenerEstadisticasUsoImagenes();
            return ResponseEntity.ok(estadisticas);
        } catch (Exception e) {
            log.error("Error obteniendo estadísticas: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Obtener imágenes con problemas", description = "Encuentra imágenes que tienen problemas (archivos faltantes, etc.)")
    @GetMapping("/problemas")
    public ResponseEntity<List<ImagenDTO>> obtenerImagenesConProblemas() {
        log.info("Buscando imágenes con problemas");
        
        try {
            List<ImagenDTO> imagenesConProblemas = imagenService.obtenerImagenesConProblemas();
            return ResponseEntity.ok(imagenesConProblemas);
        } catch (Exception e) {
            log.error("Error obteniendo imágenes con problemas: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Validar archivo", description = "Valida si un archivo es válido antes de subirlo")
    @PostMapping("/validar-archivo")
    public ResponseEntity<Boolean> validarArchivo(
            @Parameter(description = "Archivo a validar") 
            @RequestParam("archivo") MultipartFile archivo) {
        
        log.info("Validando archivo: {}", archivo.getOriginalFilename());
        
        try {
            boolean esValido = imagenService.validarTipoArchivo(archivo) && 
                             imagenService.validarTamanioArchivo(archivo);
            return ResponseEntity.ok(esValido);
        } catch (Exception e) {
            log.error("Error validando archivo: {}", e.getMessage());
            return ResponseEntity.ok(false);
        }
    }
}