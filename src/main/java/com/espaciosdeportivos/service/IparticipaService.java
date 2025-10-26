package com.espaciosdeportivos.service;

import com.espaciosdeportivos.dto.ParticipaDTO;
import java.util.List;

public interface IparticipaService {
    
    ParticipaDTO crear(ParticipaDTO participaDTO);
    ParticipaDTO actualizar(Long idReserva, Long idInvitado, ParticipaDTO participaDTO);
    ParticipaDTO confirmarInvitacion(Long idReserva, Long idInvitado);
    ParticipaDTO registrarAsistencia(Long idReserva, Long idInvitado, Boolean asistio);
    ParticipaDTO marcarComoNotificado(Long idReserva, Long idInvitado);
    void eliminar(Long idReserva, Long idInvitado);
    
    List<ParticipaDTO> findByReservaIdReserva(Long idReserva);
    List<ParticipaDTO> findByReservaIdReservaAndConfirmado(Long idReserva, Boolean confirmado);
    List<ParticipaDTO> findByInvitadoId(Long idInvitado);
    List<ParticipaDTO> findInvitadosConfirmadosPorReserva(Long idReserva);
    List<ParticipaDTO> findReservasActivasPorInvitado(Long idInvitado);
    
    ParticipaDTO findByIds(Long idReserva, Long idInvitado);
    boolean existsByInvitadoIdAndReservaIdReserva(Long idInvitado, Long idReserva);
    Long countByReservaIdReserva(Long idReserva);
    Long countByReservaIdReservaAndAsistio(Long idReserva, Boolean asistio);
}