package com.tiendaproyecto.tienda.repository;

import com.tiendaproyecto.tienda.domain.CarritoItem;
import com.tiendaproyecto.tienda.domain.Usuario;
import com.tiendaproyecto.tienda.domain.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CarritoItemRepository extends JpaRepository<CarritoItem, Long> {
    List<CarritoItem> findByUsuario(Usuario usuario);
    
    // Cambia de CarritoItem a Optional<CarritoItem>
    Optional<CarritoItem> findByUsuarioAndProducto(Usuario usuario, Producto producto);
    
    void deleteByUsuario(Usuario usuario);
}