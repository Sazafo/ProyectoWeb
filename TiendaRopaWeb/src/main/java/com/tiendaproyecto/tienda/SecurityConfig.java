package com.tiendaproyecto.tienda;

import com.tiendaproyecto.tienda.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    // IMPORTANTE: Para contraseñas en texto plano (tu BD las tiene así)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();  // Solo para desarrollo
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Rutas PÚBLICAS (sin autenticación)
                .requestMatchers("/", 
                               "/login", 
                               "/registro_comprador", 
                               "/registro_vendedor", 
                               "/css/**", 
                               "/js/**", 
                               "/img/**", 
                               "/productos", 
                               "/productos/detalle/**").permitAll()
                
                // Rutas por ROL - IMPORTANTE: usar hasRole() sin "ROLE_"
                // hasRole("COMPRADOR") buscará automáticamente "ROLE_COMPRADOR"
                .requestMatchers("/comprador/**", "/carrito/**", "/mensajes").hasRole("COMPRADOR")
                .requestMatchers("/vendedor/**").hasRole("VENDEDOR")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                
                // Cualquier otra ruta requiere autenticación
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/productos", true)  // Redirige a productos después de login
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
            .exceptionHandling(exception -> exception
                .accessDeniedPage("/acceso-denegado")  // Página personalizada para 403
            )
            .csrf(csrf -> csrf.disable());  // Temporal para pruebas

        return http.build();
    }
}