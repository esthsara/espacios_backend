package com.espaciosdeportivos.service;

import com.espaciosdeportivos.dto.SepracticaDTO;
import java.util.List;

public interface IsepracticaService {
    
    SepracticaDTO asociarDisciplinaACancha(SepracticaDTO dto);

    SepracticaDTO obtenerDisciplinaDeCancha(Long idCancha, Long idDisciplina);

    List<SepracticaDTO> obtenerDisciplinasPorCancha(Long idCancha);

    void desasociarDisciplinaDeCancha(Long idCancha, Long idDisciplina);

    List<SepracticaDTO> listarPorIdCancha(Long idCancha);
    
    List<SepracticaDTO> listarPorIdDisciplina(Long idDisciplina);
}