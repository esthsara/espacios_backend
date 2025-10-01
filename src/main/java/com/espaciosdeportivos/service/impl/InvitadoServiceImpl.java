package com.espaciosdeportivos.service.impl;

import com.espaciosdeportivos.dto.InvitadoDTO;
import com.espaciosdeportivos.model.Invitado;
import com.espaciosdeportivos.repository.InvitadoRepository;
import com.espaciosdeportivos.service.InvitadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvitadoServiceImpl implements InvitadoService {

    private final InvitadoRepository invitadoRepository;

    @Override
    public InvitadoDTO crearInvitado(InvitadoDTO dto) {
        Invitado invitado = mapToEntity(dto);
        invitadoRepository.save(invitado);
        return mapToDTO(invitado);
    }

    @Override
    public InvitadoDTO obtenerPorId(Long id) {
        return invitadoRepository.findById(id)
                .map(this::mapToDTO)
                .orElse(null);
    }

    @Override
    public List<InvitadoDTO> listarTodos() {
        return invitadoRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public InvitadoDTO actualizarInvitado(Long id, InvitadoDTO dto) {
        Invitado invitado = invitadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invitado no encontrado"));
        invitado.setNombre(dto.getNombre());
        invitado.setAPaterno(dto.getAPaterno());
        invitado.setAMaterno(dto.getAMaterno());
        invitado.setFechaNacimiento(dto.getFechaNacimiento());
        invitado.setTelefono(dto.getTelefono());
        invitado.setEmail(dto.getEmail());
        invitado.setUrlImagen(dto.getUrlImagen()); 
        //invitado.setCi(dto.getCi());
        invitado.setVerificado(dto.getVerificado());
        invitadoRepository.save(invitado);
        return mapToDTO(invitado);
    }

    @Override
    public void eliminarInvitado(Long id) {
        invitadoRepository.deleteById(id);
    }

    @Override
    public List<InvitadoDTO> buscarPorNombre(String nombre) {
        return invitadoRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // --- Métodos privados de mapeo ---
    private InvitadoDTO mapToDTO(Invitado i) {
        return InvitadoDTO.builder()
                .id(i.getId()) // en DB se renombra, pero en la clase base es id
                .nombre(i.getNombre())
                .aPaterno(i.getAPaterno())
                .aMaterno(i.getAMaterno())
                .fechaNacimiento(i.getFechaNacimiento())
                .telefono(i.getTelefono())
                .email(i.getEmail())
                .urlImagen(i.getUrlImagen())
                //.ci(i.getCi())
                .verificado(i.getVerificado())
                .build();
    }

    private Invitado mapToEntity(InvitadoDTO d) {
        return Invitado.builder()
                .id(d.getId()) // importante para update
                .nombre(d.getNombre())
                .aPaterno(d.getAPaterno())
                .aMaterno(d.getAMaterno())
                .fechaNacimiento(d.getFechaNacimiento())
                .telefono(d.getTelefono())
                .email(d.getEmail())
                .urlImagen(d.getUrlImagen())
                //.ci(d.getCi())
                .verificado(d.getVerificado())
                .build();
    }
}
