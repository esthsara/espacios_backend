/*package com.espaciosdeportivos.service;


import com.espaciosdeportivos.dto.DisciplinaDTO;
import java.util.List;

public interface IDisciplinaService {

    // Listar todas las disciplinas
    List<DisciplinaDTO> listarTodas();

    // Obtener disciplina por ID
    DisciplinaDTO obtenerPorId(Long id);

    // Crear nueva disciplina
    DisciplinaDTO crear(DisciplinaDTO disciplinaDTO);

    // Actualizar disciplina existente
    DisciplinaDTO actualizar(Long id, DisciplinaDTO disciplinaDTO);

    // Eliminar disciplina por ID
    void eliminar(Long id);

    // Buscar disciplinas por nombre parcial
    List<DisciplinaDTO> buscarPorNombre(String nombre);
}
*/
// service/IDisciplinaService.java
package com.espaciosdeportivos.service;

import com.espaciosdeportivos.dto.DisciplinaDTO;

import java.util.List;

public interface IDisciplinaService {
    DisciplinaDTO crearDisciplina(DisciplinaDTO dto);
    List<DisciplinaDTO> listarTodas();
    DisciplinaDTO obtenerPorId(Long id);
    DisciplinaDTO actualizarDisciplina(Long id, DisciplinaDTO dto);
    void eliminarDisciplina(Long id);
}
