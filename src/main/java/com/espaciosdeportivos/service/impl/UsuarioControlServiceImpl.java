package com.espaciosdeportivos.service.impl;

import com.espaciosdeportivos.dto.UsuarioControlDTO;
import com.espaciosdeportivos.model.UsuarioControl;
import com.espaciosdeportivos.repository.UsuarioControlRepository;
import com.espaciosdeportivos.service.UsuarioControlService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioControlServiceImpl implements UsuarioControlService {

    private final UsuarioControlRepository repository;

    private UsuarioControlDTO mapToDTO(UsuarioControl u){
        return UsuarioControlDTO.builder()
                .idPersona(u.getIdPersona()) // PK heredada, renombrada
                .nombre(u.getNombre())
                .aPaterno(u.getAPaterno())
                .aMaterno(u.getAMaterno())
                .fechaNacimiento(u.getFechaNacimiento())
                .telefono(u.getTelefono())
                .email(u.getEmail())
                .urlImagen(u.getUrlImagen())
                //.ci(u.getCi())
                .estadoOperativo(u.getEstadoOperativo())
                .horaInicioTurno(u.getHoraInicioTurno())
                .horaFinTurno(u.getHoraFinTurno())
                .direccion(u.getDireccion())
                .build();
    }

    private UsuarioControl mapToEntity(UsuarioControlDTO dto){
        return UsuarioControl.builder()
                .idPersona(dto.getIdPersona())
                .nombre(dto.getNombre())
                .aPaterno(dto.getAPaterno())
                .aMaterno(dto.getAMaterno())
                .fechaNacimiento(dto.getFechaNacimiento())
                .telefono(dto.getTelefono())
                .email(dto.getEmail())
                .urlImagen(dto.getUrlImagen())
                //.ci(dto.getCi())
                .estadoOperativo(dto.getEstadoOperativo())
                .horaInicioTurno(dto.getHoraInicioTurno())
                .horaFinTurno(dto.getHoraFinTurno())
                .direccion(dto.getDireccion())
                .build();
    }

    @Override
    public UsuarioControlDTO crear(UsuarioControlDTO dto) {
        return mapToDTO(repository.save(mapToEntity(dto)));
    }

    @Override
    public UsuarioControlDTO actualizar(Long id, UsuarioControlDTO dto) {
        UsuarioControl u = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("UsuarioControl no encontrado"));
        u.setNombre(dto.getNombre());
        u.setAPaterno(dto.getAPaterno());
        u.setAMaterno(dto.getAMaterno());
        u.setFechaNacimiento(dto.getFechaNacimiento());
        u.setTelefono(dto.getTelefono());
        u.setEmail(dto.getEmail());
        u.setUrlImagen(dto.getUrlImagen());
        //u.setCi(dto.getCi());
        u.setEstadoOperativo(dto.getEstadoOperativo());
        u.setHoraInicioTurno(dto.getHoraInicioTurno());
        u.setHoraFinTurno(dto.getHoraFinTurno());
        u.setDireccion(dto.getDireccion());
        return mapToDTO(repository.save(u));
    }

    @Override
    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    @Override
    public UsuarioControlDTO obtenerPorId(Long id) {
        return repository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("UsuarioControl no encontrado"));
    }

    @Override
    public List<UsuarioControlDTO> listar() {
        return repository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<UsuarioControlDTO> buscarPorNombre(String nombre) {
        return repository.findByNombreContainingIgnoreCase(nombre)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
}
