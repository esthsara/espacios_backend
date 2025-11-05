package com.espaciosdeportivos.service;

import com.espaciosdeportivos.dto.CanchaDTO;
import com.espaciosdeportivos.dto.UsuarioControlDTO;

import java.util.List;

public interface ISupervisaService {

    // Asocia un usuario de control a una cancha
    void asignarCanchaASupervisor(Long idUsuarioControl, Long idCancha);

    // Elimina la relación entre un usuario y una cancha
    void quitarCanchaDeSupervisor(Long idUsuarioControl, Long idCancha);

    // Devuelve todas las canchas que supervisa un usuario
    List<CanchaDTO> obtenerCanchasSupervisadasPorUsuario(Long idUsuarioControl);

    // Devuelve todos los usuarios que supervisan una cancha
    List<UsuarioControlDTO> obtenerSupervisoresDeCancha(Long idCancha);
}
