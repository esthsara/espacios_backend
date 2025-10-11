package com.espaciosdeportivos.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface FileStorageService {
    
    // OPERACIONES BÁSICAS (tus métodos actuales)
    String guardarArchivo(MultipartFile archivo, String subcarpeta);
    Resource cargarArchivo(String rutaArchivo);
    void eliminarArchivo(String rutaArchivo);
    String obtenerSubcarpetaPorTipoEntidad(String entidadTipo);
    
    // NUEVAS OPERACIONES RECOMENDADAS
    boolean existeArchivo(String rutaArchivo);
    long obtenerTamanioArchivo(String rutaArchivo);
    String obtenerTipoMimeArchivo(String rutaArchivo);
    boolean validarRutaArchivo(String rutaArchivo);
    
    // OPERACIONES DE MANTENIMIENTO
    void limpiarArchivosTemporales();
    List<String> listarArchivosEnDirectorio(String directorio);
    long calcularUsoDiscoDirectorio(String directorio);
    
    // OPERACIONES DE BACKUP Y MIGRACIÓN
    boolean copiarArchivo(String rutaOrigen, String rutaDestino);
    boolean moverArchivo(String rutaOrigen, String rutaDestino);
    String generarRutaUnica(String nombreOriginal, String subcarpeta);
    
    // OPERACIONES DE VALIDACIÓN
    boolean validarTipoArchivo(MultipartFile archivo, String[] tiposPermitidos);
    boolean validarTamanioArchivo(MultipartFile archivo, long tamanioMaximo);
    String obtenerExtension(String nombreArchivo);
    
    // OPERACIONES DE INFORMACIÓN 
    void mostrarEstadisticasAlmacenamiento();
    String obtenerRutaAbsoluta(String rutaRelativa);
    boolean crearDirectorioSiNoExiste(String directorio);
}