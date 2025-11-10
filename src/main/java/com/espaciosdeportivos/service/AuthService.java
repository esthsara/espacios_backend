package com.espaciosdeportivos.service;

import com.espaciosdeportivos.dto.RegistroRequest;
import com.espaciosdeportivos.model.*;
import com.espaciosdeportivos.model.enums.EstadoVerificacion;
import com.espaciosdeportivos.model.enums.RolSolicitado;
import com.espaciosdeportivos.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AppUserRepository userRepository;
    private final PersonaRepository personaRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    // Registro de usuario + persona
    public String registrar(RegistroRequest request) {
        if (userRepository.existsByUsername(request.getUsername()))
            return "El nombre de usuario ya existe";

        // 1️⃣ Crear persona
        Persona persona = Persona.builder()
                .nombre(request.getNombre())
                .apellidoPaterno(request.getApellidoPaterno())
                .apellidoMaterno(request.getApellidoMaterno())
                .fechaNacimiento(request.getFechaNacimiento())
                .telefono(request.getTelefono())
                .urlImagen(request.getUrlImagen())
                .estado(false)
                .email(request.getEmail())
                .build();

        personaRepository.save(persona);

        // 2️⃣ Crear usuario vinculado
        AppUser user = new AppUser();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setPersona(persona);
        user.setActivo(false);
        user.setEstadoVerificacion(EstadoVerificacion.PENDIENTE.name());
        user.setRolSolicitado(request.getRolSolicitado().toUpperCase());
        userRepository.save(user);

        return "Solicitud registrada. Pendiente de aprobación.";
    }
}
