package com.espaciosdeportivos.service.impl;

import com.espaciosdeportivos.dto.AdministradorDTO;
import com.espaciosdeportivos.model.Administrador;
import com.espaciosdeportivos.repository.AdministradorRepository;
import com.espaciosdeportivos.service.AdministradorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdministradorServiceImpl implements AdministradorService {

    private final AdministradorRepository administradorRepository;

    @Autowired
    public AdministradorServiceImpl(AdministradorRepository administradorRepository) {
        this.administradorRepository = administradorRepository;
    }

    @Override
    public List<AdministradorDTO> obtenerTodoslosAdministradores() {
        return administradorRepository.findByEstadoTrue().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AdministradorDTO> listarTodos() {
        return administradorRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AdministradorDTO obtenerAdministradorPorId(Long id) {
        Administrador admin = administradorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Administrador no encontrado"));
        return mapToDTO(admin);
    }

    @Override
    public List<AdministradorDTO> buscarPorNombre(String nombre) {
        return administradorRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AdministradorDTO crearAdministrador(AdministradorDTO dto) {
        Administrador admin = mapToEntity(dto);
        Administrador guardado = administradorRepository.save(admin);
        return mapToDTO(guardado);
    }

    @Override
    @Transactional
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
        admin.setEstado(dto.getEstado());
        admin.setCargo(dto.getCargo());
        admin.setDireccion(dto.getDireccion());

        administradorRepository.save(admin);
        return mapToDTO(admin);
    }

    @Override
    @Transactional
    public void eliminarAdministrador(Long id) {
        Administrador admin = administradorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Administrador no encontrado"));
        administradorRepository.delete(admin);
    }


    @Override
    @Transactional
    public AdministradorDTO cambiarEstado(Long id, Boolean nuevoEstado) {
        Administrador admin = administradorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Administrador no encontrado"));
        admin.setEstado(nuevoEstado);
        administradorRepository.save(admin);
        return mapToDTO(admin);
    }

    public List<AdministradorDTO> buscarPorRangoFecha(LocalDate inicio, LocalDate fin) {
        return administradorRepository.findByFechaNacimientoBetween(inicio, fin)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<AdministradorDTO> buscarPorNombreApellidos(String nombre, String aPaterno, String aMaterno) {
        return administradorRepository.buscarPorNombreApellidos(nombre, aPaterno, aMaterno)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }



    // --- Métodos privados de mapeo ---
    private AdministradorDTO mapToDTO(Administrador a) {
        return AdministradorDTO.builder()
                .id(a.getId())
                .nombre(a.getNombre())
                .aPaterno(a.getAPaterno())
                .aMaterno(a.getAMaterno())
                .fechaNacimiento(a.getFechaNacimiento())
                .telefono(a.getTelefono()) 
                .email(a.getEmail())
                .urlImagen(a.getUrlImagen())
                .estado(a.getEstado())
                .cargo(a.getCargo())
                .direccion(a.getDireccion())
                .build();
    }

    private Administrador mapToEntity(AdministradorDTO d) {
        return Administrador.builder()
                .id(d.getId())
                .nombre(d.getNombre())
                .aPaterno(d.getAPaterno())
                .aMaterno(d.getAMaterno())
                .fechaNacimiento(d.getFechaNacimiento())
                .telefono(d.getTelefono()) 
                .email(d.getEmail())
                .urlImagen(d.getUrlImagen())
                .estado(d.getEstado())
                .cargo(d.getCargo())
                .direccion(d.getDireccion())
                .build();
    }
}
