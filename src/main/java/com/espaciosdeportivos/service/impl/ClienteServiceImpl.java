package com.espaciosdeportivos.service.impl;

import com.espaciosdeportivos.dto.ClienteDTO;
import com.espaciosdeportivos.model.Cliente;
import com.espaciosdeportivos.repository.ClienteRepository;
import com.espaciosdeportivos.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    @Override
    public ClienteDTO crearCliente(ClienteDTO dto) {
        Cliente cliente = mapToEntity(dto);
        clienteRepository.save(cliente);
        return mapToDTO(cliente);
    }

    @Override
    public ClienteDTO obtenerPorId(Long id) {
        return clienteRepository.findById(id)
                .map(this::mapToDTO)
                .orElse(null);
    }

    @Override
    public List<ClienteDTO> listarTodos() {
        return clienteRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ClienteDTO actualizarCliente(Long id, ClienteDTO dto) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        cliente.setNombre(dto.getNombre());
        cliente.setAPaterno(dto.getAPaterno());
        cliente.setAMaterno(dto.getAMaterno());
        cliente.setFechaNacimiento(dto.getFechaNacimiento());
        cliente.setTelefono(dto.getTelefono());
        cliente.setEmail(dto.getEmail());
        cliente.setUrlImagen(dto.getUrlImagen());
        //cliente.setCi(dto.getCi());
        cliente.setEstadoCliente(dto.getEstadoCliente());
        clienteRepository.save(cliente);
        return mapToDTO(cliente);
    }

    @Override
    public void eliminarCliente(Long id) {
        clienteRepository.deleteById(id);
    }

    @Override
    public List<ClienteDTO> buscarPorNombre(String nombre) {
        return clienteRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // --- Métodos privados de mapeo ---
    private ClienteDTO mapToDTO(Cliente c) {
        return ClienteDTO.builder()
                .idPersona(c.getIdPersona()) // en DB se renombra, pero en la clase base es idPersona
                .nombre(c.getNombre())
                .aPaterno(c.getAPaterno())
                .aMaterno(c.getAMaterno())
                .fechaNacimiento(c.getFechaNacimiento())
                .telefono(c.getTelefono())
                .email(c.getEmail())
                .urlImagen(c.getUrlImagen())
                //.ci(c.getCi())
                .estadoCliente(c.getEstadoCliente())
                .build();
    }

    private Cliente mapToEntity(ClienteDTO d) {
        return Cliente.builder()
                .idPersona(d.getIdPersona()) // importante para update
                .nombre(d.getNombre())
                .aPaterno(d.getAPaterno())
                .aMaterno(d.getAMaterno())
                .fechaNacimiento(d.getFechaNacimiento())
                .telefono(d.getTelefono())
                .email(d.getEmail())
                .urlImagen(d.getUrlImagen())
                //.ci(d.getCi())
                .estadoCliente(d.getEstadoCliente())
                .build();
    }
}
