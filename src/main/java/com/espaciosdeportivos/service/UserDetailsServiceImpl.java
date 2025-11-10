package com.espaciosdeportivos.service;

import com.espaciosdeportivos.model.AppUser;
import com.espaciosdeportivos.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AppUserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepo.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        var authorities = user.getRoles().stream()
            // RoleName: ROL_SUPERUSUARIO -> queremos "ROLE_SUPERUSUARIO"
            .map(r -> "ROLE_" + r.getName().name().replace("ROL_", ""))
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

        return new User(
                user.getUsername(),
                user.getPassword(),
                user.getActivo(), // permite login solo si activo == true
                true,
                true,
                true,
                authorities
        );
    }
}

