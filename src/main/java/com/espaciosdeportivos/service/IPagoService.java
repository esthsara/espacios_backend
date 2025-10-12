package com.espaciosdeportivos.service;

import com.espaciosdeportivos.dto.PagoDTO;
import java.time.LocalDate;
import java.util.List;

public interface IPagoService {

    // CRUD básico
    PagoDTO crear(PagoDTO dto);
    PagoDTO actualizar(Long id, PagoDTO dto);
    PagoDTO obtenerPorId(Long id);
    void eliminar(Long id);
    List<PagoDTO> listarTodos();
    
    // Búsquedas básicas
    List<PagoDTO> buscarPorEstado(String estado);
    List<PagoDTO> buscarPorMetodo(String metodoPago);
    List<PagoDTO> buscarPorTipo(String tipoPago);
    List<PagoDTO> buscarPorFecha(LocalDate fecha);
    List<PagoDTO> buscarPorRangoFechas(LocalDate inicio, LocalDate fin);
    
    // Búsquedas específicas
    PagoDTO obtenerPorCodigoTransaccion(String codigoTransaccion);
    List<PagoDTO> buscarPorReserva(Long idReserva);
    List<PagoDTO> buscarPorReservaYEstado(Long idReserva, String estado);
    List<PagoDTO> buscarPorCliente(Long idCliente);
    List<PagoDTO> buscarPorClienteYEstado(Long idCliente, String estado);
    
    // Operaciones de negocio
    PagoDTO confirmarPago(Long idPago, String codigoTransaccion);
    PagoDTO anularPago(Long idPago, String razon);
    PagoDTO rechazarPago(Long idPago, String razon);
    
    // Reportes y consultas
    Double obtenerTotalPagosConfirmadosPorFecha(LocalDate fecha);
    Double obtenerTotalPagosConfirmadosPorRango(LocalDate inicio, LocalDate fin);
    List<PagoDTO> obtenerPagosConfirmadosEnRango(LocalDate inicio, LocalDate fin);
    Double obtenerSaldoPendienteReserva(Long idReserva);
    
    // Validaciones
    boolean validarCodigoTransaccionUnico(String codigoTransaccion);
    boolean existePagoConMismoMontoYReserva(Double monto, Long idReserva);
}