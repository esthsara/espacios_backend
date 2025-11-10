package com.espaciosdeportivos.service;

import java.util.List;

import com.espaciosdeportivos.dto.DisponeDTO;
//import com.espaciosdeportivos.model.disponeId;
//import com.espaciosdeportivos.model.dispone;


public interface IdisponeService {
    
    DisponeDTO asociarEquipamientoACancha(DisponeDTO dto);

    DisponeDTO obtenerEquipamientoDeCancha(Long idCancha , Long idEquipamiento);

    List<DisponeDTO> obtenerEquipamientosPorCancha(Long idCancha);

    void desasociarEquipamientoDeCancha(Long idCancha ,Long idEquipamiento);

    List<DisponeDTO> listarPorIdCancha(Long idCancha);
    
    List<DisponeDTO> listarPorIdEquipamiento(Long idEquipamiento); 
}

