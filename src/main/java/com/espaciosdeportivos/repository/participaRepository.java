package com.espaciosdeportivos.repository;

import com.espaciosdeportivos.model.participa;
import com.espaciosdeportivos.model.participaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface participaRepository extends JpaRepository<participa, participaId> {
    
    List<participa> findByReservaIdReserva(Long idReserva);
    List<participa> findByReservaIdReservaAndConfirmado(Long idReserva, Boolean confirmado);
    List<participa> findByReservaIdReservaAndAsistio(Long idReserva, Boolean asistio);
    
    List<participa> findByInvitadoId(Long idInvitado);
    List<participa> findByInvitadoIdAndConfirmado(Long idInvitado, Boolean confirmado);
    
    Long countByReservaIdReserva(Long idReserva);
    Long countByReservaIdReservaAndConfirmado(Long idReserva, Boolean confirmado);
    Long countByReservaIdReservaAndAsistio(Long idReserva, Boolean asistio);
    
    boolean existsByInvitadoIdAndReservaIdReserva(Long idInvitado, Long idReserva);
    
    @Query("SELECT p FROM participa p WHERE p.reserva.idReserva = :idReserva AND p.confirmado = true")
    List<participa> findInvitadosConfirmadosPorReserva(@Param("idReserva") Long idReserva);
    
    @Query("SELECT p FROM participa p WHERE p.invitado.id = :idInvitado AND p.reserva.estadoReserva IN ('CONFIRMADA', 'EN_CURSO')")
    List<participa> findReservasActivasPorInvitado(@Param("idInvitado") Long idInvitado);
    
    @Query("SELECT COUNT(p) FROM participa p WHERE p.reserva.idReserva = :idReserva AND p.asistio = true")
    Long countAsistentesPorReserva(@Param("idReserva") Long idReserva);
}