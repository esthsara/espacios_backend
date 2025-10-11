package com.espaciosdeportivos.service.impl;

import com.espaciosdeportivos.dto.ImagenDTO;
import com.espaciosdeportivos.model.Imagen;
import com.espaciosdeportivos.model.ImagenRelacion;
import com.espaciosdeportivos.repository.ImagenRepository;
import com.espaciosdeportivos.repository.ImagenRelacionRepository;
import com.espaciosdeportivos.service.FileStorageService;
import com.espaciosdeportivos.service.ImagenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
    //OK
@Slf4j
@Service
@RequiredArgsConstructor
public class ImagenServiceImpl implements ImagenService {

    private final ImagenRepository imagenRepository;
    private final ImagenRelacionRepository imagenRelacionRepository;
    private final FileStorageService fileStorageService;

    private static final String[] TIPOS_PERMITIDOS = {"jpg", "jpeg", "png", "gif", "webp", "bmp", "svg"};
    private static final long TAMANIO_MAXIMO = 10 * 1024 * 1024; // 10MB

    @Override
    @Transactional
    public List<ImagenDTO> guardarImagenesParaEntidad(List<MultipartFile> archivos, String entidadTipo, Long entidadId) {
        log.info("Guardando {} imágenes para entidad {}:{}", archivos.size(), entidadTipo, entidadId);
        
        List<ImagenDTO> imagenesGuardadas = new ArrayList<>();
        
        for (MultipartFile archivo : archivos) {
            try {
                // Validaciones
                if (!validarTipoArchivo(archivo)) {
                    log.warn("Tipo de archivo no permitido: {}", archivo.getOriginalFilename());
                    continue;
                }
                
                if (!validarTamanioArchivo(archivo)) {
                    log.warn("Tamaño de archivo excedido: {}", archivo.getOriginalFilename());
                    continue;
                }

                // Obtener subcarpeta según tipo de entidad
                String subcarpeta = fileStorageService.obtenerSubcarpetaPorTipoEntidad(entidadTipo);
                
                // Guardar archivo físicamente
                String rutaAlmacenamiento = fileStorageService.guardarArchivo(archivo, subcarpeta);
                
                // Crear entidad Imagen
                Imagen imagen = new Imagen();
                imagen.setNombreArchivo(archivo.getOriginalFilename());
                imagen.setRutaAlmacenamiento(rutaAlmacenamiento);
                imagen.setTipoMime(archivo.getContentType());
                imagen.setTamanioBytes(archivo.getSize());
                imagen.setEstado(true);
                imagen.setFechaCreacion(LocalDateTime.now());
                
                Imagen imagenGuardada = imagenRepository.save(imagen);
                log.info("Imagen guardada con ID: {}", imagenGuardada.getIdImagen());
                
                // Crear relación
                //OJO
                Integer siguienteOrden = imagenRelacionRepository.obtenerSiguienteOrden(entidadTipo, entidadId);
                
                ImagenRelacion relacion = new ImagenRelacion();
                relacion.setImagen(imagenGuardada);
                relacion.setEntidadTipo(entidadTipo);
                relacion.setEntidadId(entidadId);
                relacion.setOrden(siguienteOrden);
                relacion.setEstado(true);
                relacion.setFechaCreacion(LocalDateTime.now());
                
                ImagenRelacion relacionGuardada = imagenRelacionRepository.save(relacion);
                log.info("Relación creada con ID: {} y orden: {}", relacionGuardada.getIdImagenRelacion(), siguienteOrden);
                
                // Convertir a DTO y agregar a la lista
                imagenesGuardadas.add(convertirAImagenDTO(imagenGuardada, relacionGuardada));
                
            } catch (Exception e) {
                log.error("Error guardando imagen {}: {}", archivo.getOriginalFilename(), e.getMessage(), e);
            }
        }
        
        log.info("Imágenes guardadas exitosamente: {}/{}", imagenesGuardadas.size(), archivos.size());
        return imagenesGuardadas;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ImagenDTO> obtenerImagenesPorEntidad(String entidadTipo, Long entidadId) {
        log.info("Obteniendo imágenes para entidad {}:{}", entidadTipo, entidadId);
        
        List<ImagenRelacion> relaciones = imagenRelacionRepository.findWithImagenByEntidad(entidadTipo, entidadId);
        
        return relaciones.stream()
                .map(relacion -> convertirAImagenDTO(relacion.getImagen(), relacion))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void eliminarImagenLogicamente(Long idImagenRelacion) {
        log.info("Eliminando lógicamente imagen relación ID: {}", idImagenRelacion);
        
        Optional<ImagenRelacion> relacionOpt = imagenRelacionRepository.findById(idImagenRelacion);
        if (relacionOpt.isPresent()) {
            ImagenRelacion relacion = relacionOpt.get();
            relacion.setEstado(false);
            imagenRelacionRepository.save(relacion);
            
            // Reordenar las imágenes restantes
            imagenRelacionRepository.reordenarDespuesDeEliminar(
                relacion.getEntidadTipo(), 
                relacion.getEntidadId(), 
                relacion.getOrden()
            );
            
            log.info("Imagen relación {} desactivada y reordenada", idImagenRelacion);
        } else {
            throw new RuntimeException("Relación de imagen no encontrada: " + idImagenRelacion);
        }
    }

    @Override
    @Transactional
    public void eliminarImagenFisicamente(Long idImagenRelacion) {
        log.info("Eliminando físicamente imagen relación ID: {}", idImagenRelacion);
        
        Optional<ImagenRelacion> relacionOpt = imagenRelacionRepository.findById(idImagenRelacion);
        if (relacionOpt.isPresent()) {
            ImagenRelacion relacion = relacionOpt.get();
            Imagen imagen = relacion.getImagen();
            
            // Verificar si la imagen está siendo usada en otras relaciones
            boolean enUso = imagenRelacionRepository.estaImagenEnUso(imagen.getIdImagen());
            
            if (!enUso) {
                // Eliminar archivo físico
                fileStorageService.eliminarArchivo(imagen.getRutaAlmacenamiento());
                
                // Eliminar relación
                imagenRelacionRepository.delete(relacion);
                
                // Eliminar imagen
                imagenRepository.delete(imagen);
                
                log.info("Imagen y relación eliminadas completamente");
            } else {
                // Solo eliminar la relación específica
                imagenRelacionRepository.delete(relacion);
                log.info("Solo relación eliminada (imagen en uso en otras entidades)");
            }
        } else {
            throw new RuntimeException("Relación de imagen no encontrada: " + idImagenRelacion);
        }
    }

    @Override
    @Transactional
    public void eliminarTodasImagenesDeEntidad(String entidadTipo, Long entidadId) {
        log.info("Eliminando todas las imágenes de entidad {}:{}", entidadTipo, entidadId);
        
        List<ImagenRelacion> relaciones = imagenRelacionRepository.findByEntidadTipoAndEntidadId(entidadTipo, entidadId);
        
        for (ImagenRelacion relacion : relaciones) {
            eliminarImagenFisicamente(relacion.getIdImagenRelacion());
        }
        
        log.info("Todas las imágenes de la entidad eliminadas");
    }

    @Override
    public String obtenerUrlAcceso(String rutaArchivo) {
        // En una implementación real, esto construiría la URL completa
        return "/api/imagenes/archivo/" + rutaArchivo;
    }

    @Override
    @Transactional(readOnly = true)
    public ImagenDTO obtenerImagenPorId(Long idImagen) {
        log.info("Obteniendo imagen por ID: {}", idImagen);
        
        Imagen imagen = imagenRepository.findById(idImagen)
                .orElseThrow(() -> new RuntimeException("Imagen no encontrada: " + idImagen));
        
        return convertirAImagenDTO(imagen, null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ImagenDTO> obtenerTodasLasImagenesActivas() {
        log.info("Obteniendo todas las imágenes activas");
        
        return imagenRepository.findByEstadoTrue().stream()
                .map(imagen -> convertirAImagenDTO(imagen, null))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ImagenDTO actualizarImagen(Long idImagen, ImagenDTO imagenDTO) {
        log.info("Actualizando imagen ID: {}", idImagen);
        
        Imagen imagen = imagenRepository.findById(idImagen)
                .orElseThrow(() -> new RuntimeException("Imagen no encontrada: " + idImagen));
        
        // Actualizar campos permitidos
        if (imagenDTO.getNombreArchivo() != null) {
            imagen.setNombreArchivo(imagenDTO.getNombreArchivo());
        }
        //OJO
        /*if (imagenDTO.getDescripcion() != null) {
            imagen.setDescripcion(imagenDTO.getDescripcion());
        }
        */
        imagen.setFechaActualizacion(LocalDateTime.now());
        
        Imagen imagenActualizada = imagenRepository.save(imagen);
        log.info("Imagen actualizada exitosamente");
        
        return convertirAImagenDTO(imagenActualizada, null);
    }

    @Override
    @Transactional
    public void desactivarImagen(Long idImagen) {
        log.info("Desactivando imagen ID: {}", idImagen);
        
        imagenRepository.actualizarEstado(idImagen, false);
        log.info("Imagen desactivada");
    }

    @Override
    @Transactional
    public void activarImagen(Long idImagen) {
        log.info("Activando imagen ID: {}", idImagen);
        
        imagenRepository.actualizarEstado(idImagen, true);
        log.info("Imagen activada");
    }

    @Override
    @Transactional(readOnly = true)
    public List<ImagenDTO> buscarImagenesPorNombre(String nombre) {
        log.info("Buscando imágenes por nombre: {}", nombre);
        
        return imagenRepository.findByNombreArchivoContainingIgnoreCase(nombre).stream()
                .map(imagen -> convertirAImagenDTO(imagen, null))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ImagenDTO> buscarImagenesPorTipoMime(String tipoMime) {
        log.info("Buscando imágenes por tipo MIME: {}", tipoMime);
        
        return imagenRepository.findByTipoMimeContainingIgnoreCase(tipoMime).stream()
                .map(imagen -> convertirAImagenDTO(imagen, null))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ImagenDTO> buscarImagenesPorTamanioMayorA(Long tamanioMinimo) {
        log.info("Buscando imágenes con tamaño mayor a: {} bytes", tamanioMinimo);
        
        return imagenRepository.findByTamanioBytesGreaterThan(tamanioMinimo).stream()
                .map(imagen -> convertirAImagenDTO(imagen, null))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ImagenDTO> buscarImagenesPorExtension(String extension) {
        log.info("Buscando imágenes por extensión: {}", extension);
        
        String tipoMime = obtenerTipoMimePorExtension(extension);
        return buscarImagenesPorTipoMime(tipoMime);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeRelacionActiva(String entidadTipo, Long entidadId, Long idImagen) {
        return imagenRelacionRepository.existsByImagenIdImagenAndEntidadTipoAndEntidadIdAndEstadoTrue(
            idImagen, entidadTipo, entidadId);
    }

    @Override
    @Transactional(readOnly = true)
    public int contarImagenesPorEntidad(String entidadTipo, Long entidadId) {
        return (int) imagenRelacionRepository.countByEntidadTipoAndEntidadIdAndEstadoTrue(entidadTipo, entidadId);
    }

    @Override
    @Transactional
    public void reordenarImagenes(String entidadTipo, Long entidadId, List<Long> idsImagenesRelacionEnOrden) {
        log.info("Reordenando imágenes para entidad {}:{}", entidadTipo, entidadId);
        
        for (int i = 0; i < idsImagenesRelacionEnOrden.size(); i++) {
            Long idRelacion = idsImagenesRelacionEnOrden.get(i);
            imagenRelacionRepository.actualizarOrden(idRelacion, i + 1);
        }
        
        log.info("Imágenes reordenadas exitosamente");
    }

    @Override
    @Transactional
    public boolean cambiarOrdenImagen(Long idImagenRelacion, Integer nuevoOrden) {
        log.info("Cambiando orden de imagen relación {} a posición {}", idImagenRelacion, nuevoOrden);
        
        try {
            imagenRelacionRepository.actualizarOrden(idImagenRelacion, nuevoOrden);
            log.info("Orden actualizado exitosamente");
            return true;
        } catch (Exception e) {
            log.error("Error actualizando orden: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean validarTipoArchivo(MultipartFile archivo) {
        return fileStorageService.validarTipoArchivo(archivo, TIPOS_PERMITIDOS);
    }

    @Override
    public boolean validarTamanioArchivo(MultipartFile archivo) {
        return fileStorageService.validarTamanioArchivo(archivo, TAMANIO_MAXIMO);
    }

    @Override
    public String obtenerExtensionArchivo(MultipartFile archivo) {
        return fileStorageService.obtenerExtension(archivo.getOriginalFilename());
    }

    @Override
    public String formatearTamanioBytes(Long tamanioBytes) {
        if (tamanioBytes == null) return "0 B";
        
        String[] unidades = {"B", "KB", "MB", "GB"};
        int unidadIndex = 0;
        double tamanio = tamanioBytes;
        
        while (tamanio >= 1024 && unidadIndex < unidades.length - 1) {
            tamanio /= 1024;
            unidadIndex++;
        }
        
        return String.format("%.2f %s", tamanio, unidades[unidadIndex]);
    }

    @Override
    @Transactional
    public void migrarImagenEntreEntidades(Long idImagenRelacion, String nuevaEntidadTipo, Long nuevaEntidadId) {
        log.info("Migrando imagen relación {} a entidad {}:{}", idImagenRelacion, nuevaEntidadTipo, nuevaEntidadId);
        
        ImagenRelacion relacion = imagenRelacionRepository.findById(idImagenRelacion)
                .orElseThrow(() -> new RuntimeException("Relación no encontrada: " + idImagenRelacion));
        
        // Obtener nuevo orden
        Integer nuevoOrden = imagenRelacionRepository.obtenerSiguienteOrden(nuevaEntidadTipo, nuevaEntidadId);
        
        // Actualizar relación
        relacion.setEntidadTipo(nuevaEntidadTipo);
        relacion.setEntidadId(nuevaEntidadId);
        relacion.setOrden(nuevoOrden);
        
        imagenRelacionRepository.save(relacion);
        log.info("Imagen migrada exitosamente");
    }

    @Override
    @Transactional(readOnly = true)
    public List<ImagenDTO> encontrarImagenesDuplicadas() {
        log.info("Buscando imágenes duplicadas");
        
        List<ImagenRelacion> relacionesDuplicadas = imagenRelacionRepository.findRelacionesDuplicadas();
        
        return relacionesDuplicadas.stream()
                .map(relacion -> convertirAImagenDTO(relacion.getImagen(), relacion))
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void limpiarImagenesNoUtilizadas() {
        log.info("Limpiando imágenes no utilizadas");
        
        List<Imagen> imagenesInactivas = imagenRepository.findByEstadoTrue();
        int eliminadas = 0;
        
        for (Imagen imagen : imagenesInactivas) {
            // Verificar si tiene relaciones activas
            boolean tieneRelacionesActivas = imagenRelacionRepository.existsByImagenIdImagenAndEstadoTrue(imagen.getIdImagen());
            
            if (!tieneRelacionesActivas) {
                try {
                    // Eliminar archivo físico
                    fileStorageService.eliminarArchivo(imagen.getRutaAlmacenamiento());
                    
                    // Eliminar imagen de la base de datos
                    imagenRepository.delete(imagen);
                    eliminadas++;
                    
                    log.info("Imagen no utilizada eliminada: {}", imagen.getIdImagen());
                } catch (Exception e) {
                    log.error("Error eliminando imagen {}: {}", imagen.getIdImagen(), e.getMessage());
                }
            }
        }
        
        log.info("Limpieza completada: {} imágenes eliminadas", eliminadas);
    }

    @Override
    @Transactional(readOnly = true)
    public Long obtenerEstadisticasUsoImagenes() {
        return imagenRelacionRepository.countRelacionesActivas();
    }

    @Override
    public boolean hacerBackupImagen(Long idImagen, String rutaBackup) {
        // Implementación simplificada para backup
        log.info("Haciendo backup de imagen {} en {}", idImagen, rutaBackup);
        // En una implementación real, aquí se copiaría el archivo a la ruta de backup
        return true;
    }

    @Override
    public boolean restaurarImagenDesdeBackup(Long idImagen, String rutaBackup) {
        // Implementación simplificada para restauración
        log.info("Restaurando imagen {} desde {}", idImagen, rutaBackup);
        // En una implementación real, aquí se restauraría el archivo desde el backup
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ImagenDTO> obtenerImagenesConProblemas() {
        log.info("Buscando imágenes con problemas");
        
        List<ImagenDTO> imagenesConProblemas = new ArrayList<>();
        //OJO
        // Imágenes sin archivo físico
        /*List<Imagen> todasLasImagenes = imagenRepository.findAll();
        for (Imagen imagen : todasLasImagenes) {
            if (!fileStorageService.existeArchivo(imagen.getRutaAlmacenamiento())) {
                ImagenDTO dto = convertirAImagenDTO(imagen, null);
                dto.setProblema("ARCHIVO_FISICO_NO_ENCONTRADO");
                imagenesConProblemas.add(dto);
            }
        }*/
        
        // Relaciones con imágenes inactivas
        /*OJO SI SE ESCALA EN IMG*/
        List<ImagenRelacion> relacionesProblema = imagenRelacionRepository.findRelacionesConImagenInactiva();
        for (ImagenRelacion relacion : relacionesProblema) {
            ImagenDTO dto = convertirAImagenDTO(relacion.getImagen(), relacion);
            dto.setProblema("IMAGEN_INACTIVA_EN_RELACION_ACTIVA");
            imagenesConProblemas.add(dto);
        }
        
        return imagenesConProblemas;
    }

    // ========== MÉTODOS PRIVADOS ==========

    private ImagenDTO convertirAImagenDTO(Imagen imagen, ImagenRelacion relacion) {
        ImagenDTO dto = new ImagenDTO();
        dto.setIdImagen(imagen.getIdImagen());
        dto.setNombreArchivo(imagen.getNombreArchivo());
        dto.setRutaAlmacenamiento(imagen.getRutaAlmacenamiento());
        dto.setTipoMime(imagen.getTipoMime());
        dto.setTamanioBytes(imagen.getTamanioBytes());
        dto.setEstado(imagen.getEstado());
        dto.setFechaCreacion(imagen.getFechaCreacion());
        dto.setFechaActualizacion(imagen.getFechaActualizacion());
        dto.setUrlAcceso(obtenerUrlAcceso(imagen.getRutaAlmacenamiento()));
        
        if (relacion != null) {
            dto.setIdImagenRelacion(relacion.getIdImagenRelacion());
            dto.setOrden(relacion.getOrden());
            dto.setEstado(relacion.getEstado());
        }
        
        return dto;
    }

    private String obtenerTipoMimePorExtension(String extension) {
        Map<String, String> extensiones = new HashMap<>();
        extensiones.put("jpg", "image/jpeg");
        extensiones.put("jpeg", "image/jpeg");
        extensiones.put("png", "image/png");
        extensiones.put("gif", "image/gif");
        extensiones.put("webp", "image/webp");
        extensiones.put("bmp", "image/bmp");
        extensiones.put("svg", "image/svg+xml");
        
        return extensiones.getOrDefault(extension.toLowerCase(), "application/octet-stream");
    }
}