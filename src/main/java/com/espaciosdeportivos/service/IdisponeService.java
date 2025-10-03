package com.espaciosdeportivos.service;

import java.util.List;

import com.espaciosdeportivos.dto.disponeDTO;
//import com.espaciosdeportivos.model.disponeId;
//import com.espaciosdeportivos.model.dispone;


public interface IdisponeService {
    
    disponeDTO asociarEquipamientoACancha(disponeDTO dto);

    disponeDTO obtenerEquipamientoDeCancha(Long idCancha , Long idEquipamiento);

    List<disponeDTO> obtenerEquipamientosPorCancha(Long idCancha);

    void desasociarEquipamientoDeCancha(Long idCancha ,Long idEquipamiento);

    List<disponeDTO> listarPorIdCancha(Long idCancha);
    
    List<disponeDTO> listarPorIdEquipamiento(Long idEquipamiento); 
}

