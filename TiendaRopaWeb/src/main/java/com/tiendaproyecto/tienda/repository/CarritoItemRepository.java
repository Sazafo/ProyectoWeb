package com.tiendaproyecto.tienda.repository;

import com.tiendaproyecto.tienda.domain.CarritoItem;
import com.tiendaproyecto.tienda.domain.Usuario;
import com.tiendaproyecto.tienda.domain.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 
 * Ruta: src/main/java/com/tiendaproyecto/tienda/repository/CarritoItemRepository.java
 */
@Repository
public interface CarritoItemRepository extends JpaRepository<CarritoItem, Long> {
    List<CarritoItem> findByUsuario(Usuario usuario);
    Optional<CarritoItem> findByUsuarioAndProducto(Usuario usuario, Producto producto);
    void deleteByUsuario(Usuario usuario);
}
