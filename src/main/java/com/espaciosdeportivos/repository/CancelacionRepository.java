package com.espaciosdeportivos.repository;

import com.espaciosdeportivos.model.Cancelacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;

import java.util.List;
import java.util.Optional;

@Repository
public interface CancelacionRepository extends JpaRepository<Cancelacion, Long> {

    // Buscar cancelaciones por ID de cliente
    List<Cancelacion> findByCliente_Id(Long idCliente);

    // Buscar cancelación por ID de reserva 
    Optional<Cancelacion> findByReserva_IdReserva(Long idReserva);

    // Buscar cancelaciones por fecha 
    List<Cancelacion> findByFechaCancelacion(LocalDate fecha);

    // Buscar cancelaciones recientes 
    List<Cancelacion> findAllByOrderByFechaCancelacionDescHoraCancelacionDesc();
}