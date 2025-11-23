package com.espaciosdeportivos.service;

import com.espaciosdeportivos.dto.QrDTO;
import com.espaciosdeportivos.model.Qr;

import java.util.List;

public interface IQrService {

    List<QrDTO> obtenerTodosLosQrs();

    QrDTO obtenerQrPorId(Long id);

    QrDTO crearQr(QrDTO qrDTO);

    QrDTO actualizarQr(Long id, QrDTO qrDTO);

    QrDTO eliminarQr(Long id); // baja lógica

    Qr obtenerQrConBloqueo(Long id);

    void eliminarQrFisicamente(Long id);

    List<QrDTO> obtenerQrsPorReserva(Long idReserva);

}