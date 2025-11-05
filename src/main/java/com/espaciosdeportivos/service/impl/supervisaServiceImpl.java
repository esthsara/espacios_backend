package com.espaciosdeportivos.service.impl;

import com.espaciosdeportivos.dto.CanchaDTO;
import com.espaciosdeportivos.dto.UsuarioControlDTO;
import com.espaciosdeportivos.model.*;
import com.espaciosdeportivos.repository.*;
import com.espaciosdeportivos.service.ISupervisaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupervisaServiceImpl implements ISupervisaService {

    private final SupervisaRepository supervisaRepository;
    private final UsuarioControlRepository usuarioControlRepository;
    private final CanchaRepository canchaRepository;

    @Override
    @Transactional
    public void asignarCanchaASupervisor(Long idUsuarioControl, Long idCancha) {
        UsuarioControl usuario = usuarioControlRepository.findById(idUsuarioControl)
                .orElseThrow(() -> new EntityNotFoundException("Usuario de control no encontrado con ID: " + idUsuarioControl));

        Cancha cancha = canchaRepository.findById(idCancha)
                .orElseThrow(() -> new EntityNotFoundException("Cancha no encontrada con ID: " + idCancha));

        SupervisaId id = new SupervisaId(idUsuarioControl, idCancha);

        if (supervisaRepository.existsById(id)) {
            return; // Ya existe la relación, no se duplica
        }

        Supervisa nueva = Supervisa.builder()
                .id(id)
                .usuarioControl(usuario)
                .cancha(cancha)
                .build();

        supervisaRepository.save(nueva);
    }

    @Override
    @Transactional
    public void quitarCanchaDeSupervisor(Long idUsuarioControl, Long idCancha) {
        if (!supervisaRepository.existsById_IdUsControlAndId_IdCancha(idUsuarioControl, idCancha)) {
            throw new EntityNotFoundException("Relación no encontrada para eliminar: Usuario ID " + idUsuarioControl + " y Cancha ID " + idCancha);
        }

        supervisaRepository.deleteById_IdUsControlAndId_IdCancha(idUsuarioControl, idCancha);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CanchaDTO> obtenerCanchasSupervisadasPorUsuario(Long idUsuarioControl) {
        usuarioControlRepository.findById(idUsuarioControl)
                .orElseThrow(() -> new EntityNotFoundException("Usuario de control no encontrado con ID: " + idUsuarioControl));

        return supervisaRepository.findById_IdUsControl(idUsuarioControl).stream()
                .map(s -> convertToCanchaDTO(s.getCancha()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioControlDTO> obtenerSupervisoresDeCancha(Long idCancha) {
        canchaRepository.findById(idCancha)
                .orElseThrow(() -> new EntityNotFoundException("Cancha no encontrada con ID: " + idCancha));

        return supervisaRepository.findById_IdCancha(idCancha).stream()
                .map(s -> convertToUsuarioDTO(s.getUsuarioControl()))
                .collect(Collectors.toList());
    }

    // 🔄 Conversión completa a DTOs

    private CanchaDTO convertToCanchaDTO(Cancha cancha) {
        return CanchaDTO.builder()
                .idCancha(cancha.getIdCancha())
                .nombre(cancha.getNombre())
                .costoHora(cancha.getCostoHora())
                .capacidad(cancha.getCapacidad())
                .estado(cancha.getEstado())
                .mantenimiento(cancha.getMantenimiento())
                .horaInicio(cancha.getHoraInicio())
                .horaFin(cancha.getHoraFin())
                .tipoSuperficie(cancha.getTipoSuperficie())
                .tamano(cancha.getTamano())
                .iluminacion(cancha.getIluminacion())
                .cubierta(cancha.getCubierta())
                .urlImagen(cancha.getUrlImagen())
                .idAreadeportiva(cancha.getAreaDeportiva().getIdAreaDeportiva())
                .build();
    }

    private UsuarioControlDTO convertToUsuarioDTO(UsuarioControl usuario) {
        return UsuarioControlDTO.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .aPaterno(usuario.getApellidoPaterno())
                .aMaterno(usuario.getApellidoMaterno())
                .fechaNacimiento(usuario.getFechaNacimiento())
                .telefono(usuario.getTelefono())
                .email(usuario.getEmail())
                .urlImagen(usuario.getUrlImagen())
                .estado(usuario.getEstado())
                .estadoOperativo(usuario.getEstadoOperativo())
                .horaInicioTurno(usuario.getHoraInicioTurno())
                .horaFinTurno(usuario.getHoraFinTurno())
                .direccion(usuario.getDireccion())
                .build();
    }
}
