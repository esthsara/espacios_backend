package com.espaciosdeportivos.service;

import com.espaciosdeportivos.dto.participaDTO;
import java.util.List;

public interface IparticipaService {
    
    participaDTO crear(participaDTO participaDTO);
    participaDTO actualizar(Long idReserva, Long idInvitado, participaDTO participaDTO);
    participaDTO confirmarInvitacion(Long idReserva, Long idInvitado);
    participaDTO registrarAsistencia(Long idReserva, Long idInvitado, Boolean asistio);
    participaDTO marcarComoNotificado(Long idReserva, Long idInvitado);
    void eliminar(Long idReserva, Long idInvitado);
    
    List<participaDTO> findByReservaIdReserva(Long idReserva);
    List<participaDTO> findByReservaIdReservaAndConfirmado(Long idReserva, Boolean confirmado);
    List<participaDTO> findByInvitadoId(Long idInvitado);
    List<participaDTO> findInvitadosConfirmadosPorReserva(Long idReserva);
    List<participaDTO> findReservasActivasPorInvitado(Long idInvitado);
    
    participaDTO findByIds(Long idReserva, Long idInvitado);
    boolean existsByInvitadoIdAndReservaIdReserva(Long idInvitado, Long idReserva);
    Long countByReservaIdReserva(Long idReserva);
    Long countByReservaIdReservaAndAsistio(Long idReserva, Boolean asistio);
}