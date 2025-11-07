package com.espaciosdeportivos.service;
import com.espaciosdeportivos.dto.IncluyeDTO;

import java.util.List;
public interface IIncluyeService { 

    IncluyeDTO asociarCanchaDisciplinaAReserva(IncluyeDTO dto);

    IncluyeDTO obtenerPorReservaCanchaDisciplina(Long idReserva, Long idCancha, Long idDisciplina);

    void desasociarCanchaDisciplinaDeReserva(Long idReserva, Long idCancha, Long idDisciplina);

    List<IncluyeDTO> obtenerPorReserva(Long idReserva);

    List<IncluyeDTO> obtenerPorCancha(Long idCancha);

    List<IncluyeDTO> obtenerPorDisciplina(Long idDisciplina);

    Double obtenerMontoTotal(Long idReserva, Long idCancha, Long idDisciplina);

}