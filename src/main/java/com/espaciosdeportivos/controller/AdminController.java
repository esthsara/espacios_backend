package com.espaciosdeportivos.controller;

import com.espaciosdeportivos.repository.AppUserRepository;
import com.espaciosdeportivos.repository.RoleRepository;
import com.espaciosdeportivos.model.Administrador;
import com.espaciosdeportivos.model.AppUser;
import com.espaciosdeportivos.model.Persona;
import com.espaciosdeportivos.model.Role;
import com.espaciosdeportivos.model.Role.RoleName;
import com.espaciosdeportivos.dto.AuthDTO.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.http.HttpStatus;
import java.util.List;
import java.util.stream.Collectors;
import com.espaciosdeportivos.repository.PersonaRepository;
import com.espaciosdeportivos.repository.ClienteRepository;
import com.espaciosdeportivos.repository.AdministradorRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private AppUserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private PersonaRepository personaRepo;

    @Autowired
    private ClienteRepository clienteRepo;

    @Autowired
    private AdministradorRepository adminRepo;

    @PreAuthorize("hasAnyRole('SUPERUSUARIO','ADMINISTRADOR')")
    @GetMapping("/solicitudes")
    public ResponseEntity<List<AppUser>> listarSolicitudesPendientes() {
        logCurrentUser();
        try {
            List<AppUser> pendientes = userRepo.findByEstadoVerificacion("PENDIENTE");
            logger.info("Encontradas {} solicitudes pendientes", pendientes.size());
            
            for (AppUser usuario : pendientes) {
                if (usuario.getPersona() != null) {
                    // Forzar la inicialización de las colecciones perezosas
                    usuario.getPersona().getComentario().size(); // Esto inicializa la colección
                }
                usuario.getRoles().size(); // Esto inicializa los roles
            }
            
            return ResponseEntity.ok(pendientes);
        } catch (Exception e) {
            logger.error("Error al obtener solicitudes: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SUPERUSUARIO','ADMINISTRADOR')")
    @PostMapping("/solicitudes/{id}/aprobar")
    public ResponseEntity<?> aprobarSolicitud(@PathVariable Long id) {
        logCurrentUser();
        logger.info("Iniciando aprobación para solicitud ID: {}", id);

        try {
            // 1. Buscar usuario
            AppUser usuario = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
            
            logger.info("Usuario encontrado: {}", usuario.getUsername());

            // 2. Determinar rol solicitado
            String solicitado = usuario.getRolSolicitado() == null ? "CLIENTE" : usuario.getRolSolicitado().toUpperCase();
            RoleName rolName = determinarRoleName(solicitado);
            logger.info("Rol solicitado: {} -> {}", solicitado, rolName);

            // 3. Buscar rol en base de datos
            Role rol = roleRepo.findByName(rolName)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + rolName));

            // 4. Asignar rol al usuario
            if (!usuario.getRoles().contains(rol)) {
                usuario.getRoles().add(rol);
            }
            usuario.setEstadoVerificacion("APROBADO");
            usuario.setActivo(true);
            userRepo.save(usuario);
            logger.info("Rol asignado y usuario activado: {}", usuario.getUsername());

            // 5. Obtener y activar persona
            Persona persona = usuario.getPersona();
            if (persona == null) {
                logger.error("Usuario no tiene persona asociada: {}", usuario.getId());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new MessageResponse("Error: Usuario no tiene datos de persona asociados."));
            }

            persona.setEstado(true);
            persona = personaRepo.save(persona);
            logger.info("Persona activada: {}", persona.getId());

            // 6. Crear entidad específica según el rol
            if (rolName == RoleName.ROL_CLIENTE) {
                crearClienteSeguro(persona);
            } else if (rolName == RoleName.ROL_ADMINISTRADOR) {
                crearAdministradorSeguro(persona);
            }
            // Para SUPERUSUARIO no se crea entidad adicional

            logger.info("Aprobación completada exitosamente para usuario: {}", usuario.getUsername());
            return ResponseEntity.ok(new MessageResponse(
                "Usuario aprobado exitosamente. Rol asignado: " + rolName.name().replace("ROL_", "")
            ));

        } catch (Exception e) {
            logger.error("Error crítico al aprobar solicitud ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error al procesar la aprobación: " + e.getMessage()));
        }
    }

    private void crearClienteSeguro(Persona persona) {

        logger.info("Cliente ya fue creado automáticamente en el registro para persona ID: {}", persona.getId());
    }

    private void crearAdministradorSeguro(Persona persona) {
        try {
            // Verificar si ya existe como Administrador
            boolean existe = adminRepo.existsById(persona.getId());
            if (!existe) {
                // Convertir la Persona existente a Administrador
                // Esto funciona porque Administrador extiende de Persona
                Administrador admin = new Administrador();
                admin.setId(persona.getId()); // Mismo ID de persona
                admin.setNombre(persona.getNombre());
                admin.setApellidoPaterno(persona.getApellidoPaterno());
                admin.setApellidoMaterno(persona.getApellidoMaterno());
                admin.setFechaNacimiento(persona.getFechaNacimiento());
                admin.setTelefono(persona.getTelefono());
                admin.setEmail(persona.getEmail());
                admin.setUrlImagen(persona.getUrlImagen());
                admin.setEstado(true);
                admin.setCargo("Administrador General");
                admin.setDireccion("Por asignar");
                
                adminRepo.save(admin);
                logger.info("Administrador creado exitosamente para persona ID: {}", persona.getId());
            } else {
                logger.info("Administrador ya existe para persona ID: {}", persona.getId());
            }
        } catch (Exception e) {
            logger.error("Error al crear administrador para persona ID {}: {}", persona.getId(), e.getMessage());
            // NO relanzar la excepción - continuar con la aprobación
        }
    }

    @PreAuthorize("hasAnyRole('SUPERUSUARIO','ADMINISTRADOR')")
    @PostMapping("/solicitudes/{id}/rechazar")
    public ResponseEntity<?> rechazarSolicitud(@PathVariable Long id, @RequestParam(required = false) String motivo) {
        logCurrentUser();
        logger.info("Iniciando rechazo para solicitud ID: {}", id);

        try {
            AppUser usuario = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

            usuario.setEstadoVerificacion("RECHAZADO");
            usuario.setActivo(false);
            userRepo.save(usuario);

            logger.info("Solicitud rechazada exitosamente para usuario: {}", usuario.getUsername());
            return ResponseEntity.ok(new MessageResponse(
                "Solicitud rechazada. Motivo: " + (motivo == null ? "No especificado" : motivo)
            ));

        } catch (Exception e) {
            logger.error("Error al rechazar solicitud ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error al rechazar la solicitud: " + e.getMessage()));
        }
    }

    private RoleName determinarRoleName(String solicitado) {
        switch (solicitado) {
            case "SUPERUSUARIO":
                return RoleName.ROL_SUPERUSUARIO;
            case "ADMINISTRADOR":
                return RoleName.ROL_ADMINISTRADOR;
            default:
                return RoleName.ROL_CLIENTE;
        }
    }

    private void logCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            logger.info("[AUTH] Usuario actual: principal={}, authorities={}",
                    auth.getName(), auth.getAuthorities());
        }
    }
}