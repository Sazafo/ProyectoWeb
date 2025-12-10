package com.tiendaproyecto.tienda.service;

import com.tiendaproyecto.tienda.domain.Mensaje;
import com.tiendaproyecto.tienda.domain.Usuario;
import com.tiendaproyecto.tienda.repository.MensajeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MensajeService {
    
    @Autowired
    private MensajeRepository mensajeRepository;
    
    public List<Mensaje> listarMensajes(Usuario usuario) {
        return mensajeRepository.findByUsuarioOrderByFechaCreacionDesc(usuario);
    }
    
    public int contarNoLeidos(Usuario usuario) {
        return mensajeRepository.countByUsuarioAndLeidoFalse(usuario);
    }
    
    public void marcarComoLeido(Long mensajeId) {
        // CORRECCIÓN: Usar Optional
        Optional<Mensaje> optionalMensaje = mensajeRepository.findById(mensajeId);
        optionalMensaje.ifPresent(mensaje -> {
            mensaje.setLeido(true);
            mensajeRepository.save(mensaje);
        });
    }
    
    public void eliminarMensaje(Long mensajeId) {
        mensajeRepository.deleteById(mensajeId);
    }
    
    public Mensaje crearMensaje(Usuario usuario, String titulo, String contenido, Mensaje.TipoMensaje tipo) {
        Mensaje mensaje = new Mensaje();
        mensaje.setUsuario(usuario);
        mensaje.setTitulo(titulo);
        mensaje.setContenido(contenido);
        mensaje.setTipo(tipo);
        mensaje.setLeido(false);
        mensaje.setFechaCreacion(LocalDateTime.now());
        return mensajeRepository.save(mensaje);
    }
    
    // Métodos adicionales
    public Mensaje obtenerPorId(Long id) {
        return mensajeRepository.findById(id).orElse(null);
    }
    
    public void guardar(Mensaje mensaje) {
        mensajeRepository.save(mensaje);
    }
}