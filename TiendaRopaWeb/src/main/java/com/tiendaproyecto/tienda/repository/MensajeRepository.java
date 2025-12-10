package com.tiendaproyecto.tienda.repository;

import com.tiendaproyecto.tienda.domain.Mensaje;
import com.tiendaproyecto.tienda.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MensajeRepository extends JpaRepository<Mensaje, Long> {
    // JpaRepository ya tiene los métodos save(), findById(), deleteById()
    // Solo necesitas añadir los métodos personalizados
    
    List<Mensaje> findByUsuarioOrderByFechaCreacionDesc(Usuario usuario);
    List<Mensaje> findByUsuarioAndLeidoFalse(Usuario usuario);
    int countByUsuarioAndLeidoFalse(Usuario usuario);
}