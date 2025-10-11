package com.espaciosdeportivos.service;

import com.espaciosdeportivos.dto.DisciplinaDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IDisciplinaService {
    // CRUD Básico (lo que ya tienes)
    DisciplinaDTO crearDisciplina(DisciplinaDTO disciplinaDTO);
    DisciplinaDTO obtenerDisciplinaPorId(Long idDisciplina);
    List<DisciplinaDTO> obtenerTodasLasDisciplinas();
    DisciplinaDTO actualizarDisciplina(Long idDisciplina, DisciplinaDTO disciplinaDTO);
    void eliminarDisciplinaLogicamente(Long idDisciplina);
    void eliminarDisciplinaFisicamente(Long idDisciplina);
    
    // NUEVOS MÉTODOS RECOMENDADOS
    List<DisciplinaDTO> buscarPorNombre(String nombre);
    List<DisciplinaDTO> buscarPorDescripcion(String descripcion);
    List<DisciplinaDTO> obtenerDisciplinasInactivas();
    DisciplinaDTO activarDisciplina(Long idDisciplina);
    void desactivarMasivo(List<Long> idsDisciplinas);
    
    // Gestión de imágenes específica
    DisciplinaDTO agregarImagenes(Long idDisciplina, List<MultipartFile> archivosImagenes);
    DisciplinaDTO eliminarImagen(Long idDisciplina, Long idImagenRelacion);
    DisciplinaDTO reordenarImagenes(Long idDisciplina, List<Long> idsImagenesOrden);
    
    // Validaciones y verificaciones
    boolean verificarNombreDisponible(String nombre);
    boolean verificarNombreDisponibleParaActualizacion(String nombre, Long idDisciplina);
    boolean puedeEliminarse(Long idDisciplina);
    
    // Estadísticas
    long contarDisciplinasActivas();
    //AGREGAR MAS ADELANTE PARA ESTADISTICAS
    //long contarDisciplinasInactivas();
    List<DisciplinaDTO> obtenerRecientes(int limite);
}