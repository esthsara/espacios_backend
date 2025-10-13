package com.espaciosdeportivos.service;

import com.espaciosdeportivos.dto.ImagenDTO;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface ImagenService {
    // Operaciones básicas de imágenes
    List<ImagenDTO> guardarImagenesParaEntidad(List<MultipartFile> archivos, String entidadTipo, Long entidadId);
    List<ImagenDTO> obtenerImagenesPorEntidad(String entidadTipo, Long entidadId);
    void eliminarImagenLogicamente(Long idImagenRelacion);
    void eliminarImagenFisicamente(Long idImagenRelacion);
    void eliminarTodasImagenesDeEntidad(String entidadTipo, Long entidadId);
    String obtenerUrlAcceso(String rutaArchivo);
    
    // Gestión avanzada de imágenes
    ImagenDTO obtenerImagenPorId(Long idImagen);
    List<ImagenDTO> obtenerTodasLasImagenesActivas();
    ImagenDTO actualizarImagen(Long idImagen, ImagenDTO imagenDTO);
    void desactivarImagen(Long idImagen);
    void activarImagen(Long idImagen);
    
    // Búsquedas y filtros
    List<ImagenDTO> buscarImagenesPorNombre(String nombre);
    List<ImagenDTO> buscarImagenesPorTipoMime(String tipoMime);
    List<ImagenDTO> buscarImagenesPorTamanioMayorA(Long tamanioMinimo);
    List<ImagenDTO> buscarImagenesPorExtension(String extension);
    
    // Gestión de relaciones
    boolean existeRelacionActiva(String entidadTipo, Long entidadId, Long idImagen);
    int contarImagenesPorEntidad(String entidadTipo, Long entidadId);
    void reordenarImagenes(String entidadTipo, Long entidadId, List<Long> idsImagenesRelacionEnOrden);
    boolean cambiarOrdenImagen(Long idImagenRelacion, Integer nuevoOrden);
    
    // Validaciones y utilidades
    boolean validarTipoArchivo(MultipartFile archivo);
    boolean validarTamanioArchivo(MultipartFile archivo);
    String obtenerExtensionArchivo(MultipartFile archivo);
    String formatearTamanioBytes(Long tamanioBytes);
    
    // Migración y mantenimiento
    void migrarImagenEntreEntidades(Long idImagenRelacion, String nuevaEntidadTipo, Long nuevaEntidadId);
    List<ImagenDTO> encontrarImagenesDuplicadas();
    void limpiarImagenesNoUtilizadas();
    Long obtenerEstadisticasUsoImagenes();
    
    // Backup y recuperación
    boolean hacerBackupImagen(Long idImagen, String rutaBackup);
    boolean restaurarImagenDesdeBackup(Long idImagen, String rutaBackup);
    List<ImagenDTO> obtenerImagenesConProblemas();
}