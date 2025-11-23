package com.espaciosdeportivos.service;

import com.espaciosdeportivos.dto.sepracticaDTO;
import java.util.List;

public interface IsepracticaService {
    
    sepracticaDTO asociarDisciplinaACancha(sepracticaDTO dto);

    sepracticaDTO obtenerDisciplinaDeCancha(Long idCancha, Long idDisciplina);

    List<sepracticaDTO> obtenerDisciplinasPorCancha(Long idCancha);

    void desasociarDisciplinaDeCancha(Long idCancha, Long idDisciplina);

    List<sepracticaDTO> listarPorIdCancha(Long idCancha);
    
    List<sepracticaDTO> listarPorIdDisciplina(Long idDisciplina);
}