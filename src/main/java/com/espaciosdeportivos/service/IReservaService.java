package com.espaciosdeportivos.service;

import com.espaciosdeportivos.dto.ReservaDTO;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface IReservaService {

    // CRUD básico
    List<ReservaDTO> listarTodas();
    ReservaDTO obtenerPorId(Long id);
    ReservaDTO crear(ReservaDTO reservaDTO);
    ReservaDTO actualizar(Long id, ReservaDTO reservaDTO);
    void eliminar(Long id);
    
    // Búsquedas
    List<ReservaDTO> buscarPorCliente(Long idCliente);
    List<ReservaDTO> buscarPorEstado(String estado);
    List<ReservaDTO> buscarPorRangoFechas(LocalDate inicio, LocalDate fin);
    ReservaDTO obtenerPorCodigoReserva(String codigoReserva);
    
    // Métodos de negocio
    ReservaDTO confirmarReserva(Long idReserva);
    ReservaDTO cancelarReserva(Long idReserva, String motivo);
    ReservaDTO marcarComoEnCurso(Long idReserva);
    ReservaDTO marcarComoCompletada(Long idReserva);
    ReservaDTO marcarComoNoShow(Long idReserva);
    
    // Validaciones y reportes
    boolean validarDisponibilidad(LocalDate fecha, LocalTime horaInicio, LocalTime horaFin);
    List<ReservaDTO> buscarReservasActivasDelCliente(Long clienteId);
    List<ReservaDTO> obtenerReservasDelDia(LocalDate fecha);
    //List<ReservaDTO> obtenerReservasProximas();
    Double calcularIngresosEnRango(LocalDate inicio, LocalDate fin);
    
    // Utilidades
    String generarCodigoReserva();
    void validarFechaReserva(LocalDate fechaReserva);
}