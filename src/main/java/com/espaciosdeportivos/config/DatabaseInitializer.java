package com.espaciosdeportivos.config;

import com.espaciosdeportivos.model.Role;
import com.espaciosdeportivos.model.Role.RoleName;
import com.espaciosdeportivos.model.AppUser;
import com.espaciosdeportivos.repository.RoleRepository;
import com.espaciosdeportivos.repository.AppUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.HashSet;
import org.springframework.security.crypto.password.PasswordEncoder;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private AppUserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (roleRepo.count() == 0) {
            roleRepo.save(new Role(null, RoleName.ROL_SUPERUSUARIO));
            roleRepo.save(new Role(null, RoleName.ROL_ADMINISTRADOR));
            roleRepo.save(new Role(null, RoleName.ROL_CLIENTE));
        }

        if (!userRepo.existsByUsername("superuser")) {
            AppUser su = new AppUser();
            su.setUsername("superuser");
            su.setPassword(passwordEncoder.encode("super123"));
            su.setEmail("super@local");
            su.setActivo(true);
            su.setEstadoVerificacion("APROBADO");
            su.setRolSolicitado("SUPERUSUARIO");
            Role r = roleRepo.findByName(RoleName.ROL_SUPERUSUARIO).orElseThrow();
            su.getRoles().add(r);
            userRepo.save(su);
            System.out.println("Superusuario inicial creado: superuser / super123");
        }
    }
}
