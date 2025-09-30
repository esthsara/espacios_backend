package com.espaciosdeportivos.service.impl;

import com.espaciosdeportivos.dto.AdministradorDTO;
import com.espaciosdeportivos.model.Administrador;
import com.espaciosdeportivos.repository.AdministradorRepository;
import com.espaciosdeportivos.service.AdministradorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdministradorServiceImpl implements AdministradorService {

    private final AdministradorRepository administradorRepository;

    @Override
    public AdministradorDTO crearAdministrador(AdministradorDTO dto) {
        Administrador admin = mapToEntity(dto);
        administradorRepository.save(admin);
        return mapToDTO(admin);
    }

    @Override
    public AdministradorDTO obtenerPorId(Long id) {
        return administradorRepository.findById(id)
                .map(this::mapToDTO)
                .orElse(null);
    }

    @Override
    public List<AdministradorDTO> listarTodos() {
        return administradorRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AdministradorDTO actualizarAdministrador(Long id, AdministradorDTO dto) {
        Administrador admin = administradorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Administrador no encontrado"));
        admin.setNombre(dto.getNombre());
        admin.setAPaterno(dto.getAPaterno());
        admin.setAMaterno(dto.getAMaterno());
        admin.setFechaNacimiento(dto.getFechaNacimiento());
        admin.setTelefono(dto.getTelefono());
        admin.setEmail(dto.getEmail());
        admin.setUrlImagen(dto.getUrlImagen());
        //admin.setCi(dto.getCi());
        admin.setCargo(dto.getCargo());
        admin.setDireccion(dto.getDireccion());
        administradorRepository.save(admin);
        return mapToDTO(admin);
    }

    @Override
    public void eliminarAdministrador(Long id) {
        administradorRepository.deleteById(id);
    }

    @Override
    public List<AdministradorDTO> buscarPorNombre(String nombre) {
        return administradorRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // --- Métodos privados de mapeo ---
    private AdministradorDTO mapToDTO(Administrador a) {
        return AdministradorDTO.builder()
                .idPersona(a.getIdPersona())
                .nombre(a.getNombre())
                .aPaterno(a.getAPaterno())
                .aMaterno(a.getAMaterno())
                .fechaNacimiento(a.getFechaNacimiento())
                .telefono(a.getTelefono())
                .email(a.getEmail())
                .urlImagen(a.getUrlImagen())
                //.ci(a.getCi())
                .cargo(a.getCargo())
                .direccion(a.getDireccion())
                .build();
    }

    private Administrador mapToEntity(AdministradorDTO d) {
        return Administrador.builder()
                .idPersona(d.getIdPersona())
                .nombre(d.getNombre())
                .aPaterno(d.getAPaterno())
                .aMaterno(d.getAMaterno())
                .fechaNacimiento(d.getFechaNacimiento())
                .telefono(d.getTelefono())
                .email(d.getEmail())
                .urlImagen(d.getUrlImagen())
                //.ci(d.getCi())
                .cargo(d.getCargo())
                .direccion(d.getDireccion())
                .build();
    }
}
