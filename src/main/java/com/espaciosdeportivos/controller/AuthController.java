package com.espaciosdeportivos.controller;

import com.espaciosdeportivos.dto.AuthDTO.*;
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
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
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

        // Crear Persona usando el patrón Builder
        Persona persona = Persona.builder()
            .nombre(signUpRequest.getNombre())
            .apellidoPaterno(signUpRequest.getApellidoPaterno())
            .apellidoMaterno(signUpRequest.getApellidoMaterno())
            .fechaNacimiento(signUpRequest.getFechaNacimiento())
            .telefono(signUpRequest.getTelefono())
            .email(signUpRequest.getEmail())
            .urlImagen(signUpRequest.getUrlImagen() != null ? signUpRequest.getUrlImagen() : "")
            .estado(false)
            .build();
        
        persona = personaRepo.save(persona);

        // Crear AppUser
        AppUser user = new AppUser();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(encoder.encode(signUpRequest.getPassword()));
        user.setRolSolicitado(signUpRequest.getRolSolicitado() == null ? "CLIENTE" : signUpRequest.getRolSolicitado().toUpperCase());
        user.setActivo(false);
        user.setEstadoVerificacion("PENDIENTE");
        user.setPersona(persona);
        
        userRepo.save(user);

        return ResponseEntity.ok(new MessageResponse("Solicitud registrada. Pendiente de aprobación por un administrador."));
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // Verificar si existe y está aprobado antes de autenticar
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

            // 🔧 CAMBIO: incluir idPersona en la respuesta del login
            return ResponseEntity.ok(new JwtResponse(
                jwt,
                usuario.getId(),
                userDetails.getUsername(),
                usuario.getEmail(),
                roles,
                usuario.getPersona().getId()
            ));
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Credenciales inválidas."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/session-info")
    public ResponseEntity<?> getSessionInfo() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            AppUser usuario = userRepo.findByUsername(userDetails.getUsername()).orElse(null);
            if (usuario != null) {
                Set<String> roles = userDetails.getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.toSet());

                // incluyendo idPersona también en session-info
                return ResponseEntity.ok(new JwtResponse(
                    null,
                    usuario.getId(),
                    userDetails.getUsername(),
                    usuario.getEmail(),
                    roles,
                    usuario.getPersona().getId()
                ));
            }
        }
        return ResponseEntity.ok(new MessageResponse("No hay sesión activa"));
    }

    @GetMapping("/verify-token")
    public ResponseEntity<?> verifyToken(HttpServletRequest request) {
        String jwt = parseJwt(request);
        Map<String, Object> response = new HashMap<>();
        
        if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            
            response.put("valid", true);
            response.put("username", username);
            response.put("authenticated", auth != null && auth.isAuthenticated());
            response.put("authorities", auth != null ? auth.getAuthorities().toString() : "null");
            
            return ResponseEntity.ok(response);
        } else {
            response.put("valid", false);
            response.put("error", jwt == null ? "No token provided" : "Invalid token");
            return ResponseEntity.status(401).body(response);
        }
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(new MessageResponse("Sesión cerrada exitosamente!"));
    }
}
