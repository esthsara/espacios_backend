package com.espaciosdeportivos.service.impl;

import com.espaciosdeportivos.dto.AdministradorDTO;
import com.espaciosdeportivos.dto.ClienteDTO;
import com.espaciosdeportivos.dto.UsuarioControlDTO;
import com.espaciosdeportivos.model.Administrador;
import com.espaciosdeportivos.model.Cliente;
import com.espaciosdeportivos.model.UsuarioControl;
import com.espaciosdeportivos.repository.AdministradorRepository;
import com.espaciosdeportivos.repository.ReservaRepository;
import com.espaciosdeportivos.repository.SupervisaRepository;
import com.espaciosdeportivos.repository.UsuarioControlRepository;
import com.espaciosdeportivos.service.AdministradorService;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdministradorServiceImpl implements AdministradorService {

    private final AdministradorRepository administradorRepository;
    private final SupervisaRepository supervisaRepository;
    private final ReservaRepository reservaRepository;
    private final UsuarioControlRepository usuarioControlRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public AdministradorServiceImpl(
        AdministradorRepository administradorRepository,
        SupervisaRepository supervisaRepository,
        ReservaRepository reservaRepository,
        UsuarioControlRepository usuarioControlRepository,
        ModelMapper modelMapper
    ) {
        this.administradorRepository = administradorRepository;
        this.supervisaRepository = supervisaRepository;
        this.reservaRepository = reservaRepository;
        this.usuarioControlRepository = usuarioControlRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdministradorDTO> obtenerTodoslosAdministradores() {
        return administradorRepository.findByEstadoTrue().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdministradorDTO> listarTodos() {
        return administradorRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AdministradorDTO obtenerAdministradorPorId(Long id) {
        Administrador admin = administradorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Administrador no encontrado"));
        return mapToDTO(admin);
    }

    @Override
    @Transactional(readOnly = true)
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
        admin.setApellidoPaterno(dto.getAPaterno());
        admin.setApellidoMaterno(dto.getAMaterno());
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

    @Override
    @Transactional(readOnly = true)
    public List<AdministradorDTO> buscarPorRangoFecha(LocalDate inicio, LocalDate fin) {
        return administradorRepository.findByFechaNacimientoBetween(inicio, fin)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdministradorDTO> buscarPorNombreApellidos(String nombre, String aPaterno, String aMaterno) {
        return administradorRepository.buscarPorNombreApellidos(nombre, aPaterno, aMaterno)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioControlDTO> obtenerUsuariosControlPorAdministrador(Long idAdmin) {
        List<UsuarioControl> usuarios = supervisaRepository.findUsuariosControlByAdministradorId(idAdmin);
        return usuarios.stream()
                .map(this::mapToUsuarioControlDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteDTO> obtenerClientesPorAdministrador(Long idAdmin) {
        List<Cliente> clientes = reservaRepository.findClientesByAdministrador(idAdmin);
        return clientes.stream()
                .map(cliente -> modelMapper.map(cliente, ClienteDTO.class))
                .collect(Collectors.toList());
    }

    //admin crea un usuario de control y lo asigna a sus canchas
    @Override
    @Transactional
    public UsuarioControlDTO crearUsuarioControlParaAdministrador(Long idAdmin, UsuarioControlDTO dto) {
        Administrador admin = administradorRepository.findById(idAdmin)
            .orElseThrow(() -> new RuntimeException("Administrador no encontrado con ID: " + idAdmin));

        UsuarioControl usuarioControl = UsuarioControl.builder()
            .nombre(dto.getNombre())
            .apellidoPaterno(dto.getAPaterno())
            .apellidoMaterno(dto.getAMaterno())
            .fechaNacimiento(dto.getFechaNacimiento())
            .telefono(dto.getTelefono())
            .email(dto.getEmail())
            .urlImagen(dto.getUrlImagen())
            .estado(dto.getEstado())
            .direccion(dto.getDireccion())
            .estadoOperativo(dto.getEstadoOperativo())
            .horaInicioTurno(dto.getHoraInicioTurno())
            .horaFinTurno(dto.getHoraFinTurno())
            .build();

        usuarioControlRepository.save(usuarioControl);

        return mapToUsuarioControlDTO(usuarioControl);
    }

    // --- Métodos privados de mapeo ---
    private AdministradorDTO mapToDTO(Administrador a) {
        return AdministradorDTO.builder()
                .id(a.getId())
                .nombre(a.getNombre())
                .aPaterno(a.getApellidoPaterno())
                .aMaterno(a.getApellidoMaterno())
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
                .apellidoPaterno(d.getAPaterno())
                .apellidoMaterno(d.getAMaterno())
                .fechaNacimiento(d.getFechaNacimiento())
                .telefono(d.getTelefono()) 
                .email(d.getEmail())
                .urlImagen(d.getUrlImagen())
                .estado(d.getEstado())
                .cargo(d.getCargo())
                .direccion(d.getDireccion())
                .build();
    }

    private UsuarioControlDTO mapToUsuarioControlDTO(UsuarioControl u) {
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
}
