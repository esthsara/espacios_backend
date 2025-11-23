package com.espaciosdeportivos.service.impl;

import com.espaciosdeportivos.dto.RegistroDTO.*;
import com.espaciosdeportivos.model.*;
import com.espaciosdeportivos.model.Role.RoleName;
import com.espaciosdeportivos.repository.*;
import com.espaciosdeportivos.config.AdminConfig;
import com.espaciosdeportivos.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final AppUserRepository userRepository;
    private final PersonaRepository personaRepository;
    private final RoleRepository roleRepository;
    private final ClienteRepository clienteRepository;
    private final AdministradorRepository administradorRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminConfig adminConfig;

    @Override
    @Transactional
    public String registrarCliente(RegistroClienteRequest request) {
        try {
            // Validaciones básicas
            if (userRepository.existsByUsername(request.getUsername())) {
                return "Error: El nombre de usuario ya está en uso.";
            }
            if (userRepository.existsByEmail(request.getEmail())) {
                return "Error: El email ya está en uso.";
            }

            // Crear Cliente directamente (que extiende de Persona)
            Cliente cliente = Cliente.builder()
                .nombre(request.getNombre())
                .apellidoPaterno(request.getApellidoPaterno())
                .apellidoMaterno(request.getApellidoMaterno())
                .fechaNacimiento(request.getFechaNacimiento())
                .telefono(request.getTelefono())
                .email(request.getEmail())
                .urlImagen(request.getUrlImagen() != null ? request.getUrlImagen() : "")
                .estado(true) // Cliente se activa inmediatamente
                .categoria(request.getCategoria())
                .build();
            
            cliente = clienteRepository.save(cliente);

            // Crear AppUser con aprobación automática
            AppUser user = new AppUser();
            user.setUsername(request.getUsername());
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRolSolicitado("CLIENTE");
            user.setActivo(true); // Activado inmediatamente
            user.setEstadoVerificacion("APROBADO"); // Aprobado automáticamente
            user.setPersona(cliente); // Referencia al Cliente como Persona
            
            // Asignar rol CLIENTE
            Role clienteRole = roleRepository.findByName(RoleName.ROL_CLIENTE)
                .orElseThrow(() -> new RuntimeException("Rol CLIENTE no encontrado"));
            user.getRoles().add(clienteRole);
            
            userRepository.save(user);

            return "Cliente registrado exitosamente. Ya puede iniciar sesión.";
        } catch (Exception e) {
            throw new RuntimeException("Error al registrar cliente: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public String registrarAdministrador(RegistroAdministradorRequest request) {
        try {
            // Validar contraseña de administrador
            if (!request.getAdminPassword().equals(adminConfig.getAdminRegistrationPassword())) {
                return "Error: Contraseña de administrador incorrecta.";
            }

            // Validaciones básicas
            if (userRepository.existsByUsername(request.getUsername())) {
                return "Error: El nombre de usuario ya está en uso.";
            }
            if (userRepository.existsByEmail(request.getEmail())) {
                return "Error: El email ya está en uso.";
            }

            // Crear Administrador directamente (que extiende de Persona)
            Administrador administrador = Administrador.builder()
                .nombre(request.getNombre())
                .apellidoPaterno(request.getApellidoPaterno())
                .apellidoMaterno(request.getApellidoMaterno())
                .fechaNacimiento(request.getFechaNacimiento())
                .telefono(request.getTelefono())
                .email(request.getEmail())
                .urlImagen(request.getUrlImagen() != null ? request.getUrlImagen() : "")
                .estado(false) // Pendiente de aprobación
                .cargo(request.getCargo())
                .direccion(request.getDireccion())
                .build();
            
            administrador = administradorRepository.save(administrador);

            // Crear AppUser pendiente de aprobación
            AppUser user = new AppUser();
            user.setUsername(request.getUsername());
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRolSolicitado("ADMINISTRADOR");
            user.setActivo(false); // Pendiente de aprobación
            user.setEstadoVerificacion("PENDIENTE");
            user.setPersona(administrador); // Referencia al Administrador como Persona
            
            userRepository.save(user);

            return "Solicitud de administrador registrada. Pendiente de aprobación por superusuario.";
        } catch (Exception e) {
            throw new RuntimeException("Error al registrar administrador: " + e.getMessage(), e);
        }
    }
}