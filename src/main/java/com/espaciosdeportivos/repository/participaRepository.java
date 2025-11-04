package com.espaciosdeportivos.repository;

import com.espaciosdeportivos.model.Participa;
import com.espaciosdeportivos.model.ParticipaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipaRepository extends JpaRepository<Participa, ParticipaId> {
    
    List<Participa> findByReservaIdReserva(Long idReserva);
    List<Participa> findByReservaIdReservaAndConfirmado(Long idReserva, Boolean confirmado);
    List<Participa> findByReservaIdReservaAndAsistio(Long idReserva, Boolean asistio);
    
    List<Participa> findByInvitadoId(Long idInvitado);
    List<Participa> findByInvitadoIdAndConfirmado(Long idInvitado, Boolean confirmado);
    
    Long countByReservaIdReserva(Long idReserva);
    Long countByReservaIdReservaAndConfirmado(Long idReserva, Boolean confirmado);
    Long countByReservaIdReservaAndAsistio(Long idReserva, Boolean asistio);
    
    boolean existsByInvitadoIdAndReservaIdReserva(Long idInvitado, Long idReserva);
    
    @Query("SELECT p FROM Participa p WHERE p.reserva.idReserva = :idReserva AND p.confirmado = true")
    List<Participa> findInvitadosConfirmadosPorReserva(@Param("idReserva") Long idReserva);
    
    @Query("SELECT p FROM Participa p WHERE p.invitado.id = :idInvitado AND p.reserva.estadoReserva IN ('CONFIRMADA')")
    List<Participa> findReservasActivasPorInvitado(@Param("idInvitado") Long idInvitado);
    
    @Query("SELECT COUNT(p) FROM Participa p WHERE p.reserva.idReserva = :idReserva AND p.asistio = true")
    Long countAsistentesPorReserva(@Param("idReserva") Long idReserva);
}