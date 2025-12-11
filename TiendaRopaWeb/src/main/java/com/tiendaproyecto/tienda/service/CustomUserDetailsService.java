package com.tiendaproyecto.tienda.service;

import com.tiendaproyecto.tienda.domain.Usuario;
import com.tiendaproyecto.tienda.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + correo));

        // DEBUG: Imprime información importante
        System.out.println("=== DEBUG LOGIN ===");
        System.out.println("Correo: " + usuario.getCorreo());
        System.out.println("Rol Enum: " + usuario.getRol());
        System.out.println("Rol name(): " + usuario.getRol().name());
        System.out.println("Contraseña en BD: " + usuario.getContrasena());
        System.out.println("Aprobado: " + usuario.getAprobado());

        List<GrantedAuthority> authorities = new ArrayList<>();
        
        // IMPORTANTE: Convertir Enum a String con prefijo "ROLE_"
        // Ejemplo: Rol.COMPRADOR -> "ROLE_COMPRADOR"
        String authority = "ROLE_" + usuario.getRol().name();
        authorities.add(new SimpleGrantedAuthority(authority));
        
        System.out.println("Autoridad asignada: " + authority);

        // Verificar si el vendedor está aprobado
        boolean enabled = true;
        if (usuario.getRol() == Usuario.Rol.VENDEDOR && !usuario.getAprobado()) {
            enabled = false;
            System.out.println("⚠️ VENDEDOR NO APROBADO - Cuenta deshabilitada");
        }

        return User.builder()
            .username(usuario.getCorreo())
            .password(usuario.getContrasena())
            .authorities(authorities)
            .accountExpired(false)
            .accountLocked(false)
            .credentialsExpired(false)
            .disabled(!enabled)  // Deshabilitar si no está aprobado (vendedores)
            .build();
    }
}