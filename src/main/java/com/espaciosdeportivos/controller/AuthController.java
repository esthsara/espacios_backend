package com.espaciosdeportivos.controller;

import com.espaciosdeportivos.dto.AuthDTO.*;
import com.espaciosdeportivos.dto.SignupRequest;
import com.espaciosdeportivos.model.AppUser;
import com.espaciosdeportivos.model.Persona;
import com.espaciosdeportivos.model.Role;
import com.espaciosdeportivos.model.Role.RoleName;
import com.espaciosdeportivos.repository.AppUserRepository;
import com.espaciosdeportivos.repository.RoleRepository;
import com.espaciosdeportivos.repository.PersonaRepository;
import com.espaciosdeportivos.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true") // ✅ Cambiado de "*" a "http://localhost:3000"
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    AppUserRepository userRepo;

    @Autowired
    RoleRepository roleRepo;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private PersonaRepository personaRepo;


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepo.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: El nombre de usuario ya está en uso."));
        }
        if (userRepo.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: El email ya está en uso."));
        }

        // 1️⃣ Crear entidad Persona básica (sin rol todavía)
        Persona persona = new Persona();
        persona.setNombre(signUpRequest.getNombre());
        persona.setApellidoPaterno(signUpRequest.getApellidoPaterno());
        persona.setApellidoMaterno(signUpRequest.getApellidoMaterno());
        persona.setFechaNacimiento(signUpRequest.getFechaNacimiento());
        persona.setTelefono(signUpRequest.getTelefono());
        persona.setEmail(signUpRequest.getEmail());
        persona.setUrlImagen(signUpRequest.getUrlImagen());
        persona.setEstado(false);
        personaRepo.save(persona);

        // 2️⃣ Crear AppUser
        AppUser user = new AppUser();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(encoder.encode(signUpRequest.getPassword()));
        user.setRolSolicitado(signUpRequest.getRolSolicitado() == null ? "CLIENTE" : signUpRequest.getRolSolicitado().toUpperCase());
        user.setActivo(false);
        user.setEstadoVerificacion("PENDIENTE");
        user.setPersona(persona); // vincula con Persona
        userRepo.save(user);

        return ResponseEntity.ok(new MessageResponse("Solicitud registrada. Pendiente de aprobación por un administrador."));
    }


    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        // opcional: verificar si existe y está aprobado antes de autenticar:
        Optional<AppUser> maybeUser = userRepo.findByUsername(loginRequest.getUsername());
        if (maybeUser.isPresent()) {
            AppUser u = maybeUser.get();
            if (!u.getActivo()) {
                return ResponseEntity.badRequest().body(new MessageResponse("Error: Usuario no aprobado o inactivo."));
            }
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Usuario no encontrado."));
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Set<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toSet());

        AppUser usuario = userRepo.findByUsername(userDetails.getUsername()).orElseThrow();

        return ResponseEntity.ok(new JwtResponse(jwt, usuario.getId(), userDetails.getUsername(), usuario.getEmail(), roles));
    }

    @GetMapping("/session-info")
    public ResponseEntity<?> getSessionInfo() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            AppUser usuario = userRepo.findByUsername(userDetails.getUsername()).orElseThrow();
            Set<String> roles = userDetails.getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.toSet());
            return ResponseEntity.ok(new JwtResponse(null, usuario.getId(), userDetails.getUsername(), usuario.getEmail(), roles));
        }
        return ResponseEntity.ok(new MessageResponse("No hay sesión activa"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(new MessageResponse("Sesión cerrada exitosamente!"));
    }
}
