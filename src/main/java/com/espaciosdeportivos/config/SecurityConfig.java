package com.espaciosdeportivos.config;

import com.espaciosdeportivos.security.JwtAuthenticationEntryPoint;
import com.espaciosdeportivos.security.JwtAuthenticationFilter;
import com.espaciosdeportivos.security.JwtUtils;
import com.espaciosdeportivos.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // Le dice a Spring Security: "Ignora todo lo que venga de /img/**"
        return (web) -> web.ignoring().requestMatchers("/img/**");
    }

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtils jwtUtils;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder auth = http.getSharedObject(AuthenticationManagerBuilder.class);
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        return auth.build();
    }

    @Bean
    public JwtAuthenticationFilter authenticationJwtTokenFilter() {
        return new JwtAuthenticationFilter(jwtUtils, userDetailsService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(e -> e.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        // ----------------------------------------------------------------
                        // 1. RUTAS PÚBLICAS (Acceso libre)
                        // ----------------------------------------------------------------
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/public/**").permitAll()
                        .requestMatchers("/img/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        // ----------------------------------------------------------------
                        // 2. CONFIGURACIÓN GEOGRÁFICA (Superusuario + Administrador)
                        // ----------------------------------------------------------------
                        // CRÍTICO: Esto permite cargar los combos en "Mi Área"
                        .requestMatchers("/api/zona/**").hasAnyRole("SUPERUSUARIO", "ADMINISTRADOR")
                        .requestMatchers("/api/macrodistrito/**").hasAnyRole("SUPERUSUARIO", "ADMINISTRADOR")

                        // ----------------------------------------------------------------
                        // 3. RUTAS EXCLUSIVAS SUPERUSUARIO
                        // ----------------------------------------------------------------
                        .requestMatchers("/api/super/**").hasRole("SUPERUSUARIO")

                        // ----------------------------------------------------------------
                        // 4. RUTAS ADMINISTRATIVAS (Superusuario + Administrador)
                        // ----------------------------------------------------------------
                        // Gestión de Áreas Deportivas (CRÍTICO para crear mi área)
                        .requestMatchers("/api/areasdeportivas/**").hasAnyRole("SUPERUSUARIO", "ADMINISTRADOR")

                        // Gestión general
                        .requestMatchers("/api/admin/**").hasAnyRole("SUPERUSUARIO", "ADMINISTRADOR")
                        .requestMatchers("/api/administradores/**").hasAnyRole("SUPERUSUARIO", "ADMINISTRADOR")
                        .requestMatchers("/api/cancha/**").hasAnyRole("SUPERUSUARIO", "ADMINISTRADOR")
                        .requestMatchers("/api/participaciones/**").hasAnyRole("SUPERUSUARIO", "ADMINISTRADOR")
                        .requestMatchers("/api/disciplina/**").hasAnyRole("SUPERUSUARIO", "ADMINISTRADOR")
                        .requestMatchers("/api/usuario_control/**").hasAnyRole("SUPERUSUARIO", "ADMINISTRADOR")
                        .requestMatchers("/api/supervisa/**").hasAnyRole("SUPERUSUARIO", "ADMINISTRADOR")

                        // ----------------------------------------------------------------
                        // 5. RUTAS OPERATIVAS (Clientes + Admins + Superusuario)
                        // ----------------------------------------------------------------
                        .requestMatchers("/api/reservas/**").hasAnyRole("SUPERUSUARIO", "ADMINISTRADOR", "CLIENTE")
                        .requestMatchers("/api/clientes/**").hasAnyRole("SUPERUSUARIO", "ADMINISTRADOR", "CLIENTE")

                        // ----------------------------------------------------------------
                        // 6. RESTO DE RUTAS (Cualquier usuario autenticado)
                        // ----------------------------------------------------------------
                        .anyRequest().authenticated());

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("http://localhost:*", "http://127.0.0.1:*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "Accept",
                "Origin",
                "X-Requested-With",
                "Access-Control-Request-Method",
                "Access-Control-Request-Headers"));
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}