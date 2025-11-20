package com.tiendaproyecto.tienda;

import com.tiendaproyecto.tienda.domain.Usuario;
import com.tiendaproyecto.tienda.repository.UsuarioRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // TEMPORAL: Para desarrollo con contraseñas en texto plano
        // NO usar en producción
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public UserDetailsService userDetailsService(UsuarioRepository usuarioRepository) {
        return username -> {
            // Buscar usuario por correo electrónico
            Usuario usuario = usuarioRepository.findByCorreo(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
            
            // Crear UserDetails con la información del usuario
            return User.builder()
                    .username(usuario.getCorreo())
                    .password(usuario.getContrasena())
                    .roles(usuario.getRol().name()) // Convierte el ENUM a String
                    .build();
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/registro/**", "/login", "/css/**", "/js/**", "/img/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/vendedor/**").hasRole("VENDEDOR")
                .requestMatchers("/comprador/**").hasRole("COMPRADOR")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/inicio", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .csrf(csrf -> csrf.disable()); // Deshabilitado para desarrollo

        return http.build();
    }
}