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
        invitado.setApellidoPaterno(dto.getApellidoPaterno());
        invitado.setApellidoMaterno(dto.getApellidoMaterno());
        invitado.setFechaNacimiento(dto.getFechaNacimiento());
        invitado.setTelefono(dto.getTelefono());
        invitado.setEmail(dto.getEmail());
        invitado.setUrlImagen(dto.getUrlImagen());
        invitado.setEstado(dto.getEstado());
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
                .id(i.getId())
                .nombre(i.getNombre())
                .apellidoPaterno(i.getApellidoPaterno())
                .apellidoMaterno(i.getApellidoMaterno())
                .fechaNacimiento(i.getFechaNacimiento())
                .telefono(i.getTelefono())
                .email(i.getEmail())
                .urlImagen(i.getUrlImagen())
                .estado(i.getEstado())   // ✅ ahora sí se mapea
                .verificado(i.getVerificado())
                .build();
    }

    private Invitado mapToEntity(InvitadoDTO d) {
        return Invitado.builder()
                .id(d.getId())
                .nombre(d.getNombre())
                .apellidoPaterno(d.getApellidoPaterno())
                .apellidoMaterno(d.getApellidoMaterno())
                .fechaNacimiento(d.getFechaNacimiento())
                .telefono(d.getTelefono())
                .email(d.getEmail())
                .urlImagen(d.getUrlImagen())
                .estado(d.getEstado())
                .verificado(d.getVerificado())
                .build();
    }
}
