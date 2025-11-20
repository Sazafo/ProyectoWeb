package com.tiendaproyecto.tienda.service;

import com.tiendaproyecto.tienda.domain.Usuario;
import com.tiendaproyecto.tienda.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * ⚠️ Las contraseñas se guardan en TEXTO PLANO
     * NO usar en producción
     */

    // Registrar comprador
    @Transactional
    public Usuario registrarComprador(Usuario usuario) throws Exception {
        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            throw new Exception("El correo ya está registrado");
        }
        // NO se encripta la contraseña
        usuario.setRol(Usuario.Rol.COMPRADOR);
        usuario.setAprobado(true);
        return usuarioRepository.save(usuario);
    }

    // Registrar vendedor
    @Transactional
    public Usuario registrarVendedor(Usuario usuario) throws Exception {
        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            throw new Exception("El correo ya está registrado");
        }
        // NO se encripta la contraseña
        usuario.setRol(Usuario.Rol.VENDEDOR);
        usuario.setAprobado(false); // Pendiente de aprobación
        return usuarioRepository.save(usuario);
    }

    // Buscar usuario por correo
    public Optional<Usuario> buscarPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }

    // Buscar usuario por ID
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    // Actualizar perfil
    @Transactional
    public Usuario actualizarPerfil(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    // Validar contraseña (comparación directa)
    public boolean validarContrasena(String raw, String stored) {
        return raw.equals(stored);
    }
}