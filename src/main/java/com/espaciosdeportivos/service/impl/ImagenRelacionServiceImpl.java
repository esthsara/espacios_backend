package com.espaciosdeportivos.service.impl;

import com.espaciosdeportivos.dto.ImagenRelacionDTO;
import com.espaciosdeportivos.model.Imagen;
import com.espaciosdeportivos.model.ImagenRelacion;
import com.espaciosdeportivos.repository.ImagenRepository;
import com.espaciosdeportivos.repository.ImagenRelacionRepository;
import com.espaciosdeportivos.service.ImagenRelacionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
//ok
@Slf4j
@Service
@RequiredArgsConstructor
public class ImagenRelacionServiceImpl implements ImagenRelacionService {

    private final ImagenRelacionRepository imagenRelacionRepository;
    private final ImagenRepository imagenRepository;

    @Override
    @Transactional
    public ImagenRelacionDTO crearRelacion(ImagenRelacionDTO relacionDTO) {
        log.info("Creando nueva relación de imagen");
        
        // Validar que la imagen existe y está activa
        Imagen imagen = imagenRepository.findById(relacionDTO.getIdImagen())
                .orElseThrow(() -> new RuntimeException("Imagen no encontrada: " + relacionDTO.getIdImagen()));
        
        if (!imagen.getEstado()) {
            throw new RuntimeException("La imagen no está activa: " + relacionDTO.getIdImagen());
        }
        
        // Verificar duplicados
        if (verificarRelacionDuplicada(relacionDTO.getEntidadTipo(), relacionDTO.getEntidadId(), relacionDTO.getIdImagen())) {
            throw new RuntimeException("Ya existe una relación activa para esta imagen y entidad");
        }
        
        // Obtener siguiente orden disponible
        Integer siguienteOrden = obtenerSiguienteOrdenDisponible(relacionDTO.getEntidadTipo(), relacionDTO.getEntidadId());
        
        // Crear entidad
        ImagenRelacion relacion = new ImagenRelacion();
        relacion.setImagen(imagen);
        relacion.setEntidadTipo(relacionDTO.getEntidadTipo());
        relacion.setEntidadId(relacionDTO.getEntidadId());
        relacion.setOrden(siguienteOrden);
        relacion.setEstado(true);
        relacion.setFechaCreacion(LocalDateTime.now());
        
        ImagenRelacion relacionGuardada = imagenRelacionRepository.save(relacion);
        log.info("Relación creada con ID: {}", relacionGuardada.getIdImagenRelacion());
        
        return convertirARelacionDTO(relacionGuardada);
    }

    @Override
    @Transactional(readOnly = true)
    public ImagenRelacionDTO obtenerRelacionPorId(Long idImagenRelacion) {
        log.info("Obteniendo relación por ID: {}", idImagenRelacion);
        
        ImagenRelacion relacion = imagenRelacionRepository.findById(idImagenRelacion)
                .orElseThrow(() -> new RuntimeException("Relación no encontrada: " + idImagenRelacion));
        
        return convertirARelacionDTO(relacion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ImagenRelacionDTO> obtenerRelacionesPorEntidad(String entidadTipo, Long entidadId) {
        log.info("Obteniendo relaciones para entidad {}:{}", entidadTipo, entidadId);
        
        List<ImagenRelacion> relaciones = imagenRelacionRepository.findByEntidadTipoAndEntidadId(entidadTipo, entidadId);
        
        return relaciones.stream()
                .map(this::convertirARelacionDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ImagenRelacionDTO> obtenerRelacionesActivasPorEntidad(String entidadTipo, Long entidadId) {
        log.info("Obteniendo relaciones activas para entidad {}:{}", entidadTipo, entidadId);
        
        List<ImagenRelacion> relaciones = imagenRelacionRepository.findByEntidadTipoAndEntidadIdAndEstadoTrue(entidadTipo, entidadId);
        
        return relaciones.stream()
                .map(this::convertirARelacionDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ImagenRelacionDTO> obtenerTodasLasRelacionesActivas() {
        log.info("Obteniendo todas las relaciones activas");
        
        return imagenRelacionRepository.findByEstado(true).stream()
                .map(this::convertirARelacionDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ImagenRelacionDTO actualizarRelacion(Long idImagenRelacion, ImagenRelacionDTO relacionDTO) {
        log.info("Actualizando relación ID: {}", idImagenRelacion);
        
        ImagenRelacion relacion = imagenRelacionRepository.findById(idImagenRelacion)
                .orElseThrow(() -> new RuntimeException("Relación no encontrada: " + idImagenRelacion));
        
        // Actualizar campos permitidos
        if (relacionDTO.getOrden() != null && !relacionDTO.getOrden().equals(relacion.getOrden())) {
            relacion.setOrden(relacionDTO.getOrden());
        }
        
        if (relacionDTO.getEstado() != null) {
            relacion.setEstado(relacionDTO.getEstado());
        }
        
        ImagenRelacion relacionActualizada = imagenRelacionRepository.save(relacion);
        log.info("Relación actualizada exitosamente");
        
        return convertirARelacionDTO(relacionActualizada);
    }

    @Override
    @Transactional
    public void eliminarRelacionLogicamente(Long idImagenRelacion) {
        log.info("Eliminando lógicamente relación ID: {}", idImagenRelacion);
        
        Optional<ImagenRelacion> relacionOpt = imagenRelacionRepository.findById(idImagenRelacion);
        if (relacionOpt.isPresent()) {
            ImagenRelacion relacion = relacionOpt.get();
            relacion.setEstado(false);
            imagenRelacionRepository.save(relacion);
            
            // Reordenar relaciones restantes
            reindexarOrdenes(relacion.getEntidadTipo(), relacion.getEntidadId());
            
            log.info("Relación desactivada y reordenada");
        } else {
            throw new RuntimeException("Relación no encontrada: " + idImagenRelacion);
        }
    }

    @Override
    @Transactional
    public void eliminarRelacionFisicamente(Long idImagenRelacion) {
        log.info("Eliminando físicamente relación ID: {}", idImagenRelacion);
        
        if (imagenRelacionRepository.existsById(idImagenRelacion)) {
            imagenRelacionRepository.deleteById(idImagenRelacion);
            log.info("Relación eliminada completamente");
        } else {
            throw new RuntimeException("Relación no encontrada: " + idImagenRelacion);
        }
    }

    @Override
    @Transactional
    public ImagenRelacionDTO actualizarOrdenRelacion(Long idImagenRelacion, Integer nuevoOrden) {
        log.info("Actualizando orden de relación {} a {}", idImagenRelacion, nuevoOrden);
        
        ImagenRelacion relacion = imagenRelacionRepository.findById(idImagenRelacion)
                .orElseThrow(() -> new RuntimeException("Relación no encontrada: " + idImagenRelacion));
        
        // Validar que el nuevo orden sea válido
        int maxOrden = obtenerSiguienteOrdenDisponible(relacion.getEntidadTipo(), relacion.getEntidadId()) - 1;
        if (nuevoOrden < 1 || nuevoOrden > maxOrden + 1) {
            throw new RuntimeException("Orden inválido: " + nuevoOrden);
        }
        
        Integer ordenAnterior = relacion.getOrden();
        relacion.setOrden(nuevoOrden);
        
        ImagenRelacion relacionActualizada = imagenRelacionRepository.save(relacion);
        
        // Ajustar órdenes de otras relaciones si es necesario
        if (!nuevoOrden.equals(ordenAnterior)) {
            reindexarOrdenes(relacion.getEntidadTipo(), relacion.getEntidadId());
        }
        
        log.info("Orden actualizado de {} a {}", ordenAnterior, nuevoOrden);
        return convertirARelacionDTO(relacionActualizada);
    }

    @Override
    @Transactional
    public void reordenarRelaciones(String entidadTipo, Long entidadId, List<Long> idsEnOrden) {
        log.info("Reordenando relaciones para entidad {}:{}", entidadTipo, entidadId);
        
        // Validar que todos los IDs pertenecen a la misma entidad
        for (Long idRelacion : idsEnOrden) {
            ImagenRelacion relacion = imagenRelacionRepository.findById(idRelacion)
                    .orElseThrow(() -> new RuntimeException("Relación no encontrada: " + idRelacion));
            
            if (!relacion.getEntidadTipo().equals(entidadTipo) || !relacion.getEntidadId().equals(entidadId)) {
                throw new RuntimeException("La relación " + idRelacion + " no pertenece a la entidad especificada");
            }
        }
        
        // Actualizar órdenes
        for (int i = 0; i < idsEnOrden.size(); i++) {
            imagenRelacionRepository.actualizarOrden(idsEnOrden.get(i), i + 1);
        }
        
        log.info("Relaciones reordenadas exitosamente");
    }

    @Override
    @Transactional(readOnly = true)
    public Integer obtenerSiguienteOrdenDisponible(String entidadTipo, Long entidadId) {
        Integer maxOrden = imagenRelacionRepository.obtenerSiguienteOrden(entidadTipo, entidadId);
        return maxOrden + 1;
    }

    @Override
    @Transactional
    public ImagenRelacionDTO moverRelacionAPosicion(Long idImagenRelacion, Integer nuevaPosicion) {
        return actualizarOrdenRelacion(idImagenRelacion, nuevaPosicion);
    }

    @Override
    @Transactional
    public void intercambiarPosiciones(Long idImagenRelacion1, Long idImagenRelacion2) {
        log.info("Intercambiando posiciones de relaciones {} y {}", idImagenRelacion1, idImagenRelacion2);
        
        ImagenRelacion relacion1 = imagenRelacionRepository.findById(idImagenRelacion1)
                .orElseThrow(() -> new RuntimeException("Relación no encontrada: " + idImagenRelacion1));
        
        ImagenRelacion relacion2 = imagenRelacionRepository.findById(idImagenRelacion2)
                .orElseThrow(() -> new RuntimeException("Relación no encontrada: " + idImagenRelacion2));
        
        // Verificar que pertenecen a la misma entidad
        if (!relacion1.getEntidadTipo().equals(relacion2.getEntidadTipo()) || 
            !relacion1.getEntidadId().equals(relacion2.getEntidadId())) {
            throw new RuntimeException("Las relaciones no pertenecen a la misma entidad");
        }
        
        // Intercambiar órdenes
        Integer ordenTemp = relacion1.getOrden();
        relacion1.setOrden(relacion2.getOrden());
        relacion2.setOrden(ordenTemp);
        
        imagenRelacionRepository.save(relacion1);
        imagenRelacionRepository.save(relacion2);
        
        log.info("Posiciones intercambiadas exitosamente");
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeRelacionActiva(String entidadTipo, Long entidadId, Long idImagen) {
        return imagenRelacionRepository.existsByImagenIdImagenAndEntidadTipoAndEntidadIdAndEstadoTrue(
            idImagen, entidadTipo, entidadId);
    }

    @Override
    @Transactional(readOnly = true)
    public int contarRelacionesPorEntidad(String entidadTipo, Long entidadId) {
        return (int) imagenRelacionRepository.countByEntidadTipoAndEntidadIdAndEstadoTrue(entidadTipo, entidadId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean verificarRelacionDuplicada(String entidadTipo, Long entidadId, Long idImagen) {
        return imagenRelacionRepository.existsByImagenIdImagenAndEntidadTipoAndEntidadIdAndEstadoTrue(
            idImagen, entidadTipo, entidadId);
    }

    @Override
    public boolean validarRelacion(ImagenRelacionDTO relacionDTO) {
        if (relacionDTO == null) return false;
        if (relacionDTO.getIdImagen() == null) return false;
        if (relacionDTO.getEntidadTipo() == null || relacionDTO.getEntidadTipo().trim().isEmpty()) return false;
        if (relacionDTO.getEntidadId() == null || relacionDTO.getEntidadId() <= 0) return false;
        
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean estaImagenEnUso(Long idImagen) {
        return imagenRelacionRepository.estaImagenEnUso(idImagen);
    }

    @Override
    @Transactional(readOnly = true)
    public int contarEntidadesQueUsanImagen(Long idImagen) {
        List<Object[]> resultados = imagenRelacionRepository.findEntidadesQueUsanImagen(idImagen);
        return resultados.size();
    }

    @Override
    @Transactional
    public ImagenRelacionDTO migrarRelacion(Long idImagenRelacion, String nuevaEntidadTipo, Long nuevaEntidadId) {
        log.info("Migrando relación {} a entidad {}:{}", idImagenRelacion, nuevaEntidadTipo, nuevaEntidadId);
        
        ImagenRelacion relacion = imagenRelacionRepository.findById(idImagenRelacion)
                .orElseThrow(() -> new RuntimeException("Relación no encontrada: " + idImagenRelacion));
        
        // Verificar que no existe ya una relación igual en la nueva entidad
        if (verificarRelacionDuplicada(nuevaEntidadTipo, nuevaEntidadId, relacion.getImagen().getIdImagen())) {
            throw new RuntimeException("Ya existe una relación para esta imagen en la entidad destino");
        }
        
        // Obtener nuevo orden
        Integer nuevoOrden = obtenerSiguienteOrdenDisponible(nuevaEntidadTipo, nuevaEntidadId);
        
        // Actualizar relación
        relacion.setEntidadTipo(nuevaEntidadTipo);
        relacion.setEntidadId(nuevaEntidadId);
        relacion.setOrden(nuevoOrden);
        
        ImagenRelacion relacionActualizada = imagenRelacionRepository.save(relacion);
        log.info("Relación migrada exitosamente");
        
        return convertirARelacionDTO(relacionActualizada);
    }

    @Override
    @Transactional
    public void transferirTodasLasRelaciones(String entidadTipoOrigen, Long entidadIdOrigen, 
                                           String entidadTipoDestino, Long entidadIdDestino) {
        log.info("Transferiendo todas las relaciones de {}:{} a {}:{}", 
                entidadTipoOrigen, entidadIdOrigen, entidadTipoDestino, entidadIdDestino);
        
        List<ImagenRelacion> relacionesOrigen = imagenRelacionRepository
                .findByEntidadTipoAndEntidadIdAndEstadoTrue(entidadTipoOrigen, entidadIdOrigen);
        
        for (ImagenRelacion relacion : relacionesOrigen) {
            migrarRelacion(relacion.getIdImagenRelacion(), entidadTipoDestino, entidadIdDestino);
        }
        
        log.info("Transferencia completada: {} relaciones transferidas", relacionesOrigen.size());
    }

    @Override
    @Transactional
    public void clonarRelaciones(String entidadTipoOrigen, Long entidadIdOrigen, 
                               String entidadTipoDestino, Long entidadIdDestino) {
        log.info("Clonando relaciones de {}:{} a {}:{}", 
                entidadTipoOrigen, entidadIdOrigen, entidadTipoDestino, entidadIdDestino);
        
        List<ImagenRelacion> relacionesOrigen = imagenRelacionRepository
                .findByEntidadTipoAndEntidadIdAndEstadoTrue(entidadTipoOrigen, entidadIdOrigen);
        
        for (ImagenRelacion relacionOrigen : relacionesOrigen) {
            ImagenRelacionDTO nuevaRelacionDTO = new ImagenRelacionDTO();
            nuevaRelacionDTO.setIdImagen(relacionOrigen.getImagen().getIdImagen());
            nuevaRelacionDTO.setEntidadTipo(entidadTipoDestino);
            nuevaRelacionDTO.setEntidadId(entidadIdDestino);
            
            crearRelacion(nuevaRelacionDTO);
        }
        
        log.info("Clonación completada: {} relaciones clonadas", relacionesOrigen.size());
    }

    @Override
    @Transactional
    public void fusionarRelaciones(List<String> tiposEntidadesOrigen, List<Long> idsEntidadesOrigen,
                                 String entidadTipoDestino, Long entidadIdDestino) {
        log.info("Fusionando relaciones en entidad destino {}:{}", entidadTipoDestino, entidadIdDestino);
        
        if (tiposEntidadesOrigen.size() != idsEntidadesOrigen.size()) {
            throw new RuntimeException("Las listas de tipos e IDs de entidades origen deben tener el mismo tamaño");
        }
        
        Set<Long> imagenesYaProcesadas = new HashSet<>();
        
        for (int i = 0; i < tiposEntidadesOrigen.size(); i++) {
            String tipoOrigen = tiposEntidadesOrigen.get(i);
            Long idOrigen = idsEntidadesOrigen.get(i);
            
            List<ImagenRelacion> relacionesOrigen = imagenRelacionRepository
                    .findByEntidadTipoAndEntidadIdAndEstadoTrue(tipoOrigen, idOrigen);
            
            for (ImagenRelacion relacionOrigen : relacionesOrigen) {
                Long idImagen = relacionOrigen.getImagen().getIdImagen();
                
                // Evitar duplicados en el destino
                if (!imagenesYaProcesadas.contains(idImagen)) {
                    ImagenRelacionDTO nuevaRelacionDTO = new ImagenRelacionDTO();
                    nuevaRelacionDTO.setIdImagen(idImagen);
                    nuevaRelacionDTO.setEntidadTipo(entidadTipoDestino);
                    nuevaRelacionDTO.setEntidadId(entidadIdDestino);
                    
                    crearRelacion(nuevaRelacionDTO);
                    imagenesYaProcesadas.add(idImagen);
                }
            }
        }
        
        log.info("Fusión completada: {} relaciones únicas fusionadas", imagenesYaProcesadas.size());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> obtenerEstadisticasPorTipoEntidad() {
        log.info("Obteniendo estadísticas por tipo de entidad");
        
        return imagenRelacionRepository.contarRelacionesPorTipoEntidad();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ImagenRelacionDTO> encontrarRelacionesDuplicadas() {
        log.info("Buscando relaciones duplicadas");
        
        List<ImagenRelacion> relacionesDuplicadas = imagenRelacionRepository.findRelacionesDuplicadas();
        
        return relacionesDuplicadas.stream()
                .map(this::convertirARelacionDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ImagenRelacionDTO> encontrarRelacionesSinImagen() {
        log.info("Buscando relaciones sin imagen asociada");
        
        // Esta consulta necesitaría ser implementada en el repository
        // Por ahora, retornamos una lista vacía como placeholder
        return new ArrayList<>();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ImagenRelacionDTO> obtenerRelacionesConEstadisticas() {
        log.info("Obteniendo relaciones con estadísticas");
        
        // Implementación simplificada - en una versión real, esto incluiría más datos
        return obtenerTodasLasRelacionesActivas();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ImagenRelacionDTO> obtenerRelacionesPorRangoFechas(String fechaInicio, String fechaFin) {
        log.info("Obteniendo relaciones por rango de fechas: {} a {}", fechaInicio, fechaFin);
        
        // Convertir strings a LocalDateTime (implementación simplificada)
        LocalDateTime inicio = LocalDateTime.parse(fechaInicio);
        LocalDateTime fin = LocalDateTime.parse(fechaFin);
        
        List<ImagenRelacion> relaciones = imagenRelacionRepository.findByRangoFechas(inicio, fin);
        
        return relaciones.stream()
                .map(this::convertirARelacionDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void desactivarRelacionesPorEntidad(String entidadTipo, Long entidadId) {
        log.info("Desactivando todas las relaciones de entidad {}:{}", entidadTipo, entidadId);
        
        imagenRelacionRepository.desactivarPorEntidad(entidadTipo, entidadId);
        log.info("Relaciones desactivadas exitosamente");
    }

    @Override
    @Transactional
    public void eliminarRelacionesPorEntidad(String entidadTipo, Long entidadId) {
        log.info("Eliminando todas las relaciones de entidad {}:{}", entidadTipo, entidadId);
        
        imagenRelacionRepository.deleteByEntidadTipoAndEntidadId(entidadTipo, entidadId);
        log.info("Relaciones eliminadas exitosamente");
    }

    @Override
    @Transactional
    public void limpiarRelacionesInactivas() {
        log.info("Limpiando relaciones inactivas");
        
        List<ImagenRelacion> relacionesInactivas = imagenRelacionRepository.findByEstado(false);
        int eliminadas = 0;
        
        for (ImagenRelacion relacion : relacionesInactivas) {
            // Verificar si la imagen está siendo usada en otras relaciones activas
            boolean imagenEnUso = imagenRelacionRepository.estaImagenEnUso(relacion.getImagen().getIdImagen());
            
            if (!imagenEnUso) {
                imagenRelacionRepository.delete(relacion);
                eliminadas++;
            }
        }
        
        log.info("Limpieza completada: {} relaciones inactivas eliminadas", eliminadas);
    }

    @Override
    @Transactional
    public void reindexarOrdenes(String entidadTipo, Long entidadId) {
        log.info("Reindexando órdenes para entidad {}:{}", entidadTipo, entidadId);
        
        List<ImagenRelacion> relaciones = imagenRelacionRepository
                .findImagenesConOrden(entidadTipo, entidadId);
        
        for (int i = 0; i < relaciones.size(); i++) {
            ImagenRelacion relacion = relaciones.get(i);
            if (!relacion.getOrden().equals(i + 1)) {
                relacion.setOrden(i + 1);
                imagenRelacionRepository.save(relacion);
            }
        }
        
        log.info("Reindexación completada: {} relaciones reordenadas", relaciones.size());
    }

    @Override
    @Transactional
    public void sincronizarConEstadoImagenes() {
        log.info("Sincronizando relaciones con estado de imágenes");
        
        List<ImagenRelacion> relacionesActivas = imagenRelacionRepository.findByEstado(true);
        int actualizadas = 0;
        
        for (ImagenRelacion relacion : relacionesActivas) {
            if (!relacion.getImagen().getEstado()) {
                relacion.setEstado(false);
                imagenRelacionRepository.save(relacion);
                actualizadas++;
            }
        }
        
        log.info("Sincronización completada: {} relaciones actualizadas", actualizadas);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> validarIntegridadRelaciones() {
        log.info("Validando integridad de relaciones");
        
        List<String> problemas = new ArrayList<>();
        
        // Relaciones con imágenes inactivas
        List<ImagenRelacion> relacionesProblema = imagenRelacionRepository.findRelacionesConImagenInactiva();
        if (!relacionesProblema.isEmpty()) {
            problemas.add("Se encontraron " + relacionesProblema.size() + " relaciones activas con imágenes inactivas");
        }
        
        // Relaciones duplicadas
        List<ImagenRelacion> duplicados = imagenRelacionRepository.findRelacionesDuplicadas();
        if (!duplicados.isEmpty()) {
            problemas.add("Se encontraron " + duplicados.size() + " relaciones duplicadas");
        }
        
        // Órdenes inconsistentes
        // (Implementación simplificada - en una versión real se harían más validaciones)
        
        log.info("Validación completada: {} problemas encontrados", problemas.size());
        return problemas;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ImagenRelacionDTO> buscarRelaciones(String entidadTipo, Long entidadId, 
                                                  Boolean estado, Integer ordenMin, Integer ordenMax) {
        log.info("Buscando relaciones con filtros avanzados");
        
        // Implementación simplificada - en una versión real esto usaría una consulta más compleja
        List<ImagenRelacion> relaciones = imagenRelacionRepository.findByEntidadTipoAndEntidadId(entidadTipo, entidadId);
        
        return relaciones.stream()
                .filter(relacion -> estado == null || relacion.getEstado().equals(estado))
                .filter(relacion -> ordenMin == null || relacion.getOrden() >= ordenMin)
                .filter(relacion -> ordenMax == null || relacion.getOrden() <= ordenMax)
                .map(this::convertirARelacionDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ImagenRelacionDTO> obtenerRelacionesPaginadas(int pagina, int tamanio) {
        log.info("Obteniendo relaciones paginadas - página: {}, tamaño: {}", pagina, tamanio);
        
        // Implementación simplificada - en una versión real usaría Pageable de Spring
        List<ImagenRelacion> todasLasRelaciones = imagenRelacionRepository.findAll();
        
        int inicio = pagina * tamanio;
        int fin = Math.min(inicio + tamanio, todasLasRelaciones.size());
        
        if (inicio >= todasLasRelaciones.size()) {
            return new ArrayList<>();
        }
        
        return todasLasRelaciones.subList(inicio, fin).stream()
                .map(this::convertirARelacionDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ImagenRelacionDTO> buscarRelacionesPorTipoImagen(String tipoMime) {
        log.info("Buscando relaciones por tipo de imagen: {}", tipoMime);
        
        // Implementación simplificada - en una versión real esto se haría con una consulta JOIN
        List<ImagenRelacion> todasLasRelaciones = imagenRelacionRepository.findByEstado(true);
        
        return todasLasRelaciones.stream()
                .filter(relacion -> relacion.getImagen().getTipoMime().contains(tipoMime))
                .map(this::convertirARelacionDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ImagenRelacionDTO> obtenerRelacionesConImagenes(String entidadTipo, Long entidadId) {
        log.info("Obteniendo relaciones con imágenes para entidad {}:{}", entidadTipo, entidadId);
        
        List<ImagenRelacion> relaciones = imagenRelacionRepository.findWithImagenByEntidad(entidadTipo, entidadId);
        
        return relaciones.stream()
                .map(this::convertirARelacionDTO)
                .collect(Collectors.toList());
    }

    // ========== MÉTODOS PRIVADOS ==========

    private ImagenRelacionDTO convertirARelacionDTO(ImagenRelacion relacion) {
        ImagenRelacionDTO dto = new ImagenRelacionDTO();
        dto.setIdImagenRelacion(relacion.getIdImagenRelacion());
        dto.setIdImagen(relacion.getImagen().getIdImagen());
        dto.setEntidadTipo(relacion.getEntidadTipo());
        dto.setEntidadId(relacion.getEntidadId());
        dto.setOrden(relacion.getOrden());
        dto.setEstado(relacion.getEstado());
        dto.setFechaCreacion(relacion.getFechaCreacion());
        /*dto.setFechaActualizacion(relacion.getFechaActualizacion());
        
        // Información de la imagen
        dto.setNombreArchivo(relacion.getImagen().getNombreArchivo());
        dto.setTipoMime(relacion.getImagen().getTipoMime());
        dto.setTamanioBytes(relacion.getImagen().getTamanioBytes());
        dto.setEstadoImagen(relacion.getImagen().getEstado());*/
        
        return dto;
    }
}