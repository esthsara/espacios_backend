package com.espaciosdeportivos.config;

import com.espaciosdeportivos.model.Role;
import com.espaciosdeportivos.model.Role.RoleName;
import com.espaciosdeportivos.model.AppUser;
import com.espaciosdeportivos.model.Persona;
import com.espaciosdeportivos.repository.RoleRepository;
import com.espaciosdeportivos.repository.AppUserRepository;
import com.espaciosdeportivos.repository.PersonaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDate;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private AppUserRepository userRepo;

    @Autowired
    private PersonaRepository personaRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Crear roles si no existen
        if (roleRepo.count() == 0) {
            roleRepo.save(new Role(null, RoleName.ROL_SUPERUSUARIO));
            roleRepo.save(new Role(null, RoleName.ROL_ADMINISTRADOR));
            roleRepo.save(new Role(null, RoleName.ROL_CLIENTE));
            System.out.println("Roles iniciales creados.");
        }

        // VERIFICACIÓN DOBLE: Crear superusuario solo si no existe
        if (!userRepo.existsByUsername("superuser")) {
            System.out.println("Creando superusuario inicial...");
            
            // Primero crear la Persona
            Persona personaSuperuser = Persona.builder()
                .nombre("Super")
                .apellidoPaterno("Usuario")
                .apellidoMaterno("Sistema")
                .fechaNacimiento(LocalDate.of(1990, 1, 1))
                .telefono("00000000")
                .email("super@system.com")
                .urlImagen("/default-avatar.png")
                .estado(true)
                .build();
            personaSuperuser = personaRepo.save(personaSuperuser);

            // Luego crear el AppUser
            AppUser su = new AppUser();
            su.setUsername("superuser");
            su.setPassword(passwordEncoder.encode("super123"));
            su.setEmail("super@system.com");
            su.setActivo(true);
            su.setEstadoVerificacion("APROBADO");
            su.setRolSolicitado("SUPERUSUARIO");
            su.setPersona(personaSuperuser); // Vincular con Persona
            
            // Asignar rol
            Role superRole = roleRepo.findByName(RoleName.ROL_SUPERUSUARIO)
                .orElseThrow(() -> new RuntimeException("Rol SUPERUSUARIO no encontrado"));
            su.getRoles().add(superRole);
            
            userRepo.save(su);
            System.out.println("Superusuario inicial creado: superuser / super123");
        } else {
            System.out.println("Superusuario ya existe, omitiendo creación.");
        }
    }
}