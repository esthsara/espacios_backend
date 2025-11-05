package com.espaciosdeportivos.service;

import com.espaciosdeportivos.dto.DisciplinaDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IDisciplinaService {
    // crear disciplina
    DisciplinaDTO crearDisciplina(DisciplinaDTO disciplinaDTO);
    // obtener disciplina por ID
    DisciplinaDTO obtenerDisciplinaPorId(Long idDisciplina);
    // listar todas las disciplinas
    List<DisciplinaDTO> obtenerTodasLasDisciplinas();
    //listar disciplinas 
    List<DisciplinaDTO> listarTodas();
    // actualizar disciplina ojo
    DisciplinaDTO actualizarDisciplina(Long idDisciplina, DisciplinaDTO disciplinaDTO);
    // eliminar disciplina lógicamente
    void eliminarDisciplinaLogicamente(Long idDisciplina);
    // eliminar disciplina físicamente
    void eliminarDisciplinaFisicamente(Long idDisciplina);

    DisciplinaDTO eliminar(Long id, Boolean nuevoEstado);
    
    // NUEVOS MÉTODOS RECOMENDADOS
    List<DisciplinaDTO> buscarPorNombre(String nombre);
    List<DisciplinaDTO> buscarPorDescripcion(String descripcion);
    List<DisciplinaDTO> obtenerDisciplinasInactivas();
    DisciplinaDTO activarDisciplina(Long idDisciplina);
    void desactivarMasivo(List<Long> idsDisciplinas);

    /* buscar disciplinas por nombre */
    //List<DisciplinaDTO> buscarDisciplinasPorNombre(String nombre);


    /// Validaciones y verificaciones
    boolean verificarNombreDisponible(String nombre);
    boolean verificarNombreDisponibleParaActualizacion(String nombre, Long idDisciplina);
    boolean puedeEliminarse(Long idDisciplina);
    
    // Gestión de imágenes específica
    DisciplinaDTO agregarImagenes(Long idDisciplina, List<MultipartFile> archivosImagenes);
    DisciplinaDTO eliminarImagen(Long idDisciplina, Long idImagenRelacion);
    DisciplinaDTO reordenarImagenes(Long idDisciplina, List<Long> idsImagenesOrden);
     
    // Estadísticas
    long contarDisciplinasActivas();
    //AGREGAR MAS ADELANTE PARA ESTADISTICAS
    //long contarDisciplinasInactivas();
    List<DisciplinaDTO> obtenerRecientes(int limite);
}