package com.espaciosdeportivos.service;

import com.espaciosdeportivos.dto.ImagenRelacionDTO;
import java.util.List;

public interface ImagenRelacionService {
    
    // Operaciones CRUD básicas
    ImagenRelacionDTO crearRelacion(ImagenRelacionDTO relacionDTO);
    ImagenRelacionDTO obtenerRelacionPorId(Long idImagenRelacion);
    List<ImagenRelacionDTO> obtenerRelacionesPorEntidad(String entidadTipo, Long entidadId);
    List<ImagenRelacionDTO> obtenerRelacionesActivasPorEntidad(String entidadTipo, Long entidadId);
    List<ImagenRelacionDTO> obtenerTodasLasRelacionesActivas();
    ImagenRelacionDTO actualizarRelacion(Long idImagenRelacion, ImagenRelacionDTO relacionDTO);
    void eliminarRelacionLogicamente(Long idImagenRelacion);
    void eliminarRelacionFisicamente(Long idImagenRelacion);
    
    // Operaciones de ordenamiento
    ImagenRelacionDTO actualizarOrdenRelacion(Long idImagenRelacion, Integer nuevoOrden);
    void reordenarRelaciones(String entidadTipo, Long entidadId, List<Long> idsEnOrden);
    Integer obtenerSiguienteOrdenDisponible(String entidadTipo, Long entidadId);
    ImagenRelacionDTO moverRelacionAPosicion(Long idImagenRelacion, Integer nuevaPosicion);
    void intercambiarPosiciones(Long idImagenRelacion1, Long idImagenRelacion2);
    
    // Operaciones de validación y verificación
    boolean existeRelacionActiva(String entidadTipo, Long entidadId, Long idImagen);
    int contarRelacionesPorEntidad(String entidadTipo, Long entidadId);
    boolean verificarRelacionDuplicada(String entidadTipo, Long entidadId, Long idImagen);
    boolean validarRelacion(ImagenRelacionDTO relacionDTO);
    boolean estaImagenEnUso(Long idImagen);
    int contarEntidadesQueUsanImagen(Long idImagen);
    
    // Operaciones de migración y transferencia
    ImagenRelacionDTO migrarRelacion(Long idImagenRelacion, String nuevaEntidadTipo, Long nuevaEntidadId);
    void transferirTodasLasRelaciones(String entidadTipoOrigen, Long entidadIdOrigen, 
                                     String entidadTipoDestino, Long entidadIdDestino);
    void clonarRelaciones(String entidadTipoOrigen, Long entidadIdOrigen, 
                         String entidadTipoDestino, Long entidadIdDestino);
    void fusionarRelaciones(List<String> tiposEntidadesOrigen, List<Long> idsEntidadesOrigen,
                           String entidadTipoDestino, Long entidadIdDestino);
    
    // Operaciones de reportes y estadísticas
    List<Object[]> obtenerEstadisticasPorTipoEntidad();
    List<ImagenRelacionDTO> encontrarRelacionesDuplicadas();
    List<ImagenRelacionDTO> encontrarRelacionesSinImagen();
    List<ImagenRelacionDTO> obtenerRelacionesConEstadisticas();
    List<ImagenRelacionDTO> obtenerRelacionesPorRangoFechas(String fechaInicio, String fechaFin);
    
    // Operaciones de mantenimiento
    void desactivarRelacionesPorEntidad(String entidadTipo, Long entidadId);
    void eliminarRelacionesPorEntidad(String entidadTipo, Long entidadId);
    void limpiarRelacionesInactivas();
    void reindexarOrdenes(String entidadTipo, Long entidadId);
    void sincronizarConEstadoImagenes();
    List<String> validarIntegridadRelaciones();
    
    // Operaciones de búsqueda avanzada
    List<ImagenRelacionDTO> buscarRelaciones(String entidadTipo, Long entidadId, 
                                           Boolean estado, Integer ordenMin, Integer ordenMax);
    List<ImagenRelacionDTO> obtenerRelacionesPaginadas(int pagina, int tamanio);
    List<ImagenRelacionDTO> buscarRelacionesPorTipoImagen(String tipoMime);
    List<ImagenRelacionDTO> obtenerRelacionesConImagenes(String entidadTipo, Long entidadId);
}