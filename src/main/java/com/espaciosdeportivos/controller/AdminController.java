package com.espaciosdeportivos.controller;

import com.espaciosdeportivos.repository.AppUserRepository;
import com.espaciosdeportivos.repository.RoleRepository;
import com.espaciosdeportivos.model.Administrador;
import com.espaciosdeportivos.model.AppUser;
import com.espaciosdeportivos.model.Cliente;
import com.espaciosdeportivos.model.Persona;
import com.espaciosdeportivos.model.Role;
import com.espaciosdeportivos.model.Role.RoleName;
import com.espaciosdeportivos.dto.AuthDTO.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;
import com.espaciosdeportivos.repository.PersonaRepository;
import com.espaciosdeportivos.repository.ClienteRepository;
import com.espaciosdeportivos.repository.AdministradorRepository;


@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AdminController {

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


    // Devuelve las solicitudes pendientes
    @GetMapping("/solicitudes")
    public List<AppUser> listarSolicitudesPendientes() {
        return userRepo.findByEstadoVerificacion("PENDIENTE");
    }

    // Aprobar una solicitud: asigna el rol solicitado y activa al usuario
    @PostMapping("/solicitudes/{id}/aprobar")
    public ResponseEntity<?> aprobarSolicitud(@PathVariable Long id) {
        AppUser u = userRepo.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        String solicitado = u.getRolSolicitado() == null ? "CLIENTE" : u.getRolSolicitado().toUpperCase();

        RoleName rn;
        switch (solicitado) {
            case "SUPERUSUARIO":
                rn = RoleName.ROL_SUPERUSUARIO;
                break;
            case "ADMINISTRADOR":
                rn = RoleName.ROL_ADMINISTRADOR;
                break;
            default:
                rn = RoleName.ROL_CLIENTE;
        }

        Role role = roleRepo.findByName(rn).orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        u.getRoles().add(role);
        u.setEstadoVerificacion("APROBADO");
        u.setActivo(true);
        userRepo.save(u);


        Persona persona = u.getPersona();

        if (rn == RoleName.ROL_CLIENTE) {
        Cliente cliente = Cliente.builder()
                .id(persona.getId()) // usa el id existente de una Persona
                .nombre(persona.getNombre())
                .apellidoPaterno(persona.getApellidoPaterno())
                .apellidoMaterno(persona.getApellidoMaterno())
                .fechaNacimiento(persona.getFechaNacimiento())
                .telefono(persona.getTelefono())
                .email(persona.getEmail())
                .urlImagen(persona.getUrlImagen())
                .estado(true)
                .categoria("REGULAR")
                .build();
        clienteRepo.save(cliente);
        } else if (rn == RoleName.ROL_ADMINISTRADOR) {
            Administrador admin = Administrador.builder()
                    .id(persona.getId())
                    .nombre(persona.getNombre())
                    .apellidoPaterno(persona.getApellidoPaterno())
                    .apellidoMaterno(persona.getApellidoMaterno())
                    .fechaNacimiento(persona.getFechaNacimiento())
                    .telefono(persona.getTelefono())
                    .email(persona.getEmail())
                    .urlImagen(persona.getUrlImagen())
                    .estado(true)
                    .cargo("Sin definir")
                    .direccion("Pendiente de asignar")
                    .build();
            adminRepo.save(admin);
        }

        persona.setEstado(true);
        personaRepo.save(persona);

        return ResponseEntity.ok(new MessageResponse("Usuario aprobado y rol asignado: " + rn.name()));
    }


    @PostMapping("/solicitudes/{id}/rechazar")
    public ResponseEntity<?> rechazarSolicitud(@PathVariable Long id, @RequestParam(required = false) String motivo) {
        AppUser u = userRepo.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        u.setEstadoVerificacion("RECHAZADO");
        u.setActivo(false);
        userRepo.save(u);
        return ResponseEntity.ok(new MessageResponse("Solicitud rechazada. Motivo: " + (motivo == null ? "Sin motivo" : motivo)));
    }
}
