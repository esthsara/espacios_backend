package com.espaciosdeportivos.service.impl;

import com.espaciosdeportivos.dto.ClienteDTO;
import com.espaciosdeportivos.model.Cliente;
import com.espaciosdeportivos.repository.ClienteRepository;
import com.espaciosdeportivos.service.ClienteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    @Autowired
    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public List<ClienteDTO> obtenerTodoslosClientes() {
        return clienteRepository.findByEstadoTrue().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ClienteDTO> listarTodos() {
        return clienteRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ClienteDTO obtenerClientePorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        return mapToDTO(cliente);
    }

    @Override
    public List<ClienteDTO> buscarPorNombre(String nombre) {
        return clienteRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ClienteDTO crearCliente(ClienteDTO dto) {
        Cliente cliente = mapToEntity(dto);
        Cliente guardado = clienteRepository.save(cliente);
        return mapToDTO(guardado);
    }

    @Override
    @Transactional
    public ClienteDTO actualizarCliente(Long id, ClienteDTO dto) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        cliente.setNombre(dto.getNombre());
        cliente.setApellidoPaterno(dto.getAPaterno());
        cliente.setApellidoMaterno(dto.getAMaterno());
        cliente.setFechaNacimiento(dto.getFechaNacimiento());
        cliente.setTelefono(dto.getTelefono()); 
        cliente.setEmail(dto.getEmail());
        cliente.setUrlImagen(dto.getUrlImagen());
        cliente.setEstado(dto.getEstado());
        cliente.setEstadoCliente(dto.getEstadoCliente());

        clienteRepository.save(cliente);
        return mapToDTO(cliente);
    }

    @Override
    public void eliminarCliente(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        cliente.setEstado(false); // eliminación lógica
        clienteRepository.save(cliente);
    }

    @Override
    @Transactional
    public ClienteDTO cambiarEstado(Long id, Boolean nuevoEstado) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        cliente.setEstado(nuevoEstado);
        clienteRepository.save(cliente);
        return mapToDTO(cliente);
    }

    // --- Métodos privados de mapeo ---
    private ClienteDTO mapToDTO(Cliente c) {
        return ClienteDTO.builder()
                .id(c.getId())
                .nombre(c.getNombre())
                .aPaterno(c.getApellidoPaterno())
                .aMaterno(c.getApellidoMaterno())
                .fechaNacimiento(c.getFechaNacimiento())
                .telefono(c.getTelefono()) 
                .email(c.getEmail())
                .urlImagen(c.getUrlImagen())
                .estado(c.getEstado())
                .estadoCliente(c.getEstadoCliente())
                .build();
    }

    private Cliente mapToEntity(ClienteDTO d) {
        return Cliente.builder()
                .id(d.getId())
                .nombre(d.getNombre())
                .apellidoPaterno(d.getAPaterno())
                .apellidoMaterno(d.getAMaterno())
                .fechaNacimiento(d.getFechaNacimiento())
                .telefono(d.getTelefono()) 
                .email(d.getEmail())
                .urlImagen(d.getUrlImagen())
                .estado(d.getEstado())
                .estadoCliente(d.getEstadoCliente())
                .build();
    }
}
