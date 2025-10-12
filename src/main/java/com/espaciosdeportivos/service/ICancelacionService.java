package com.espaciosdeportivos.service;

import com.espaciosdeportivos.dto.CancelacionDTO;
import com.espaciosdeportivos.model.Cancelacion;

import java.util.List;

public interface ICancelacionService {

    List<CancelacionDTO> obtenerTodasLasCancelaciones();

    CancelacionDTO obtenerCancelacionPorId(Long id);

    CancelacionDTO crearCancelacion(CancelacionDTO cancelacionDTO);

    CancelacionDTO actualizarCancelacion(Long id, CancelacionDTO cancelacionDTO);

    CancelacionDTO eliminarCancelacion(Long id); //eliminacion lgica 

    Cancelacion obtenerCancelacionConBloqueo(Long id);

    void eliminarCancelacionFisicamente(Long id);
}