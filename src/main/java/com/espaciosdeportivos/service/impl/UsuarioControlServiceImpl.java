package com.espaciosdeportivos.service.impl;

import com.espaciosdeportivos.dto.UsuarioControlDTO;
import com.espaciosdeportivos.model.UsuarioControl;
import com.espaciosdeportivos.repository.UsuarioControlRepository;
import com.espaciosdeportivos.service.UsuarioControlService;
import com.espaciosdeportivos.validation.UsuarioControlValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioControlServiceImpl implements UsuarioControlService {

    private final UsuarioControlRepository usuarioControlRepository;
    private final UsuarioControlValidator validator;

    @Autowired
    public UsuarioControlServiceImpl(UsuarioControlRepository usuarioControlRepository,
                                     UsuarioControlValidator validator) {
        this.usuarioControlRepository = usuarioControlRepository;
        this.validator = validator;
    }

    @Override
    public List<UsuarioControlDTO> obtenerTodosLosUsuariosControl() {
        return usuarioControlRepository.findAll().stream()
                .filter(UsuarioControl::getEstado)
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<UsuarioControlDTO> listarTodos() {
        return usuarioControlRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UsuarioControlDTO obtenerUsuarioControlPorId(Long id) {
        UsuarioControl usuario = usuarioControlRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario de control no encontrado"));
        return mapToDTO(usuario);
    }

    @Override
    public List<UsuarioControlDTO> buscarPorNombre(String nombre) {
        return usuarioControlRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UsuarioControlDTO crearUsuarioControl(UsuarioControlDTO dto) {
        validator.validarUsuarioControl(dto);
        UsuarioControl usuario = mapToEntity(dto);
        UsuarioControl guardado = usuarioControlRepository.save(usuario);
        return mapToDTO(guardado);
    }

    @Override
    @Transactional
    public UsuarioControlDTO actualizarUsuarioControl(Long id, UsuarioControlDTO dto) {
        UsuarioControl usuario = usuarioControlRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario de control no encontrado"));

        validator.validarUsuarioControl(dto);

        usuario.setNombre(dto.getNombre());
        usuario.setApellidoPaterno(dto.getAPaterno());
        usuario.setApellidoMaterno(dto.getAMaterno());
        usuario.setFechaNacimiento(dto.getFechaNacimiento());
        usuario.setTelefono(dto.getTelefono());
        usuario.setEmail(dto.getEmail());
        usuario.setUrlImagen(dto.getUrlImagen());
        usuario.setEstado(dto.getEstado());

        usuario.setEstadoOperativo(dto.getEstadoOperativo());
        usuario.setHoraInicioTurno(dto.getHoraInicioTurno());
        usuario.setHoraFinTurno(dto.getHoraFinTurno());
        usuario.setDireccion(dto.getDireccion());

        usuarioControlRepository.save(usuario);
        return mapToDTO(usuario);
    }

    @Override
    public void eliminarUsuarioControl(Long id) {
        UsuarioControl usuario = usuarioControlRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario de control no encontrado"));
        usuario.setEstado(false); // eliminación lógica
        usuarioControlRepository.save(usuario);
    }

    @Override
    @Transactional
    public UsuarioControlDTO cambiarEstado(Long id, Boolean nuevoEstado) {
        UsuarioControl usuario = usuarioControlRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario de control no encontrado"));
        usuario.setEstado(nuevoEstado);
        usuarioControlRepository.save(usuario);
        return mapToDTO(usuario);
    }

    // --- Métodos privados de mapeo ---
    private UsuarioControlDTO mapToDTO(UsuarioControl u) {
        return UsuarioControlDTO.builder()
                .id(u.getId())
                .nombre(u.getNombre())
                .aPaterno(u.getApellidoPaterno())
                .aMaterno(u.getApellidoMaterno())
                .fechaNacimiento(u.getFechaNacimiento())
                .telefono(u.getTelefono())
                .email(u.getEmail())
                .urlImagen(u.getUrlImagen())
                .estado(u.getEstado())
                .estadoOperativo(u.getEstadoOperativo())
                .horaInicioTurno(u.getHoraInicioTurno())
                .horaFinTurno(u.getHoraFinTurno())
                .direccion(u.getDireccion())
                .build();
    }

    private UsuarioControl mapToEntity(UsuarioControlDTO d) {
        return UsuarioControl.builder()
                .id(d.getId())
                .nombre(d.getNombre())
                .apellidoPaterno(d.getAPaterno())
                .apellidoMaterno(d.getAMaterno())
                .fechaNacimiento(d.getFechaNacimiento())
                .telefono(d.getTelefono())
                .email(d.getEmail())
                .urlImagen(d.getUrlImagen())
                .estado(d.getEstado())
                .estadoOperativo(d.getEstadoOperativo())
                .horaInicioTurno(d.getHoraInicioTurno())
                .horaFinTurno(d.getHoraFinTurno())
                .direccion(d.getDireccion())
                .build();
    }
}
