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
            .map(r -> r.getName().name().replace("ROL_", "ROLE_"))
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

        // Nota: el flag 'activo' impide iniciar sesión si es false
        return new User(user.getUsername(), user.getPassword(), user.getActivo(), true, true, true, authorities);
    }
}
