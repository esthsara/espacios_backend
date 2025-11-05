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
    @Transactional(readOnly = true)
    public List<ClienteDTO> obtenerTodoslosClientes() {
        return clienteRepository.findByEstadoTrue().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteDTO> listarTodos() {
        return clienteRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteDTO obtenerClientePorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));
        return mapToDTO(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteDTO> buscarPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new RuntimeException("El parámetro nombre es obligatorio para la búsqueda");
        }
        return clienteRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ClienteDTO crearCliente(ClienteDTO dto) {
        // Validar que no exista un cliente con el mismo email
        if (clienteRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Ya existe un cliente con el email: " + dto.getEmail());
        }

        // Validar campos obligatorios adicionales
        if (dto.getEstado() == null) {
            dto.setEstado(true); // Por defecto activo al crear
        }

        Cliente cliente = mapToEntity(dto);
        Cliente guardado = clienteRepository.save(cliente);
        return mapToDTO(guardado);
    }

    @Override
    @Transactional
    public ClienteDTO actualizarCliente(Long id, ClienteDTO dto) {
        // Buscar cliente existente
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));

        // Validar que el email no esté siendo usado por otro cliente
        if (dto.getEmail() != null && !dto.getEmail().equals(cliente.getEmail())) {
            boolean emailExiste = clienteRepository.findAll().stream()
                    .filter(c -> !c.getId().equals(id))
                    .anyMatch(c -> dto.getEmail().equals(c.getEmail()));
            if (emailExiste) {
                throw new RuntimeException("El email " + dto.getEmail() + " ya está en uso por otro cliente");
            }
        }

        // Actualizar campos de Persona
        if (dto.getNombre() != null) {
            cliente.setNombre(dto.getNombre());
        }
        if (dto.getAPaterno() != null) {
            cliente.setApellidoPaterno(dto.getAPaterno());
        }
        if (dto.getAMaterno() != null) {
            cliente.setApellidoMaterno(dto.getAMaterno());
        }
        if (dto.getFechaNacimiento() != null) {
            cliente.setFechaNacimiento(dto.getFechaNacimiento());
        }
        if (dto.getTelefono() != null) {
            cliente.setTelefono(dto.getTelefono());
        }
        if (dto.getEmail() != null) {
            cliente.setEmail(dto.getEmail());
        }
        if (dto.getUrlImagen() != null) {
            cliente.setUrlImagen(dto.getUrlImagen());
        }
        if (dto.getEstado() != null) {
            cliente.setEstado(dto.getEstado());
        }
        
        // Actualizar campos específicos de Cliente
        if (dto.getCategoria() != null) {
            cliente.setCategoria(dto.getCategoria());
        }

        Cliente actualizado = clienteRepository.save(cliente);
        return mapToDTO(actualizado);
    }

    @Override
    @Transactional
    public void eliminarCliente(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));
        cliente.setEstado(false); // eliminación lógica
        clienteRepository.save(cliente);
    }

    @Override
    @Transactional
    public ClienteDTO cambiarEstado(Long id, Boolean nuevoEstado) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));
        
        if (nuevoEstado == null) {
            throw new RuntimeException("El nuevo estado no puede ser nulo");
        }
        
        cliente.setEstado(nuevoEstado);
        Cliente actualizado = clienteRepository.save(cliente);
        return mapToDTO(actualizado);
    }

    // --- Métodos privados de mapeo ---
    
    private ClienteDTO mapToDTO(Cliente cliente) {
        if (cliente == null) {
            return null;
        }
        
        return ClienteDTO.builder()
                .id(cliente.getId())
                .nombre(cliente.getNombre())
                .aPaterno(cliente.getApellidoPaterno())
                .aMaterno(cliente.getApellidoMaterno())
                .fechaNacimiento(cliente.getFechaNacimiento())
                .telefono(cliente.getTelefono()) 
                .email(cliente.getEmail())
                .urlImagen(cliente.getUrlImagen())
                .estado(cliente.getEstado())
                .categoria(cliente.getCategoria())
                .build();
    }

    private Cliente mapToEntity(ClienteDTO dto) {
        if (dto == null) {
            return null;
        }
        
        // Crear Cliente usando constructor y setters (evitar problemas con herencia y builder)
        Cliente cliente = new Cliente();
        
        // Establecer campos de Persona
        cliente.setId(dto.getId()); // Puede ser null para creación nueva
        cliente.setNombre(dto.getNombre());
        cliente.setApellidoPaterno(dto.getAPaterno());
        cliente.setApellidoMaterno(dto.getAMaterno());
        cliente.setFechaNacimiento(dto.getFechaNacimiento());
        cliente.setTelefono(dto.getTelefono());
        cliente.setEmail(dto.getEmail());
        cliente.setUrlImagen(dto.getUrlImagen());
        cliente.setEstado(dto.getEstado() != null ? dto.getEstado() : true); // Por defecto activo
        
        // Establecer campos específicos de Cliente
        cliente.setCategoria(dto.getCategoria() != null ? dto.getCategoria() : "REGULAR");
        
        return cliente;
    }
}