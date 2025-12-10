package com.tiendaproyecto.tienda.repository;

import com.tiendaproyecto.tienda.domain.DetallePedido;
import com.tiendaproyecto.tienda.domain.Pedido;
import com.tiendaproyecto.tienda.domain.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.math.BigDecimal;  // IMPORTANTE: Añadir este import
import java.util.List;

public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {
    
    // Métodos básicos que Spring Data generará automáticamente:
    
    // Buscar detalles por pedido
    List<DetallePedido> findByPedido(Pedido pedido);
    
    // Buscar detalles por producto
    List<DetallePedido> findByProducto(Producto producto);
    
    // Buscar por ID del detalle (coincide con el campo idDetalle de la entidad)
    DetallePedido findByIdDetalle(Long idDetalle);
    
    // Eliminar por pedido
    void deleteByPedido(Pedido pedido);
    
    // Contar por pedido
    Long countByPedido(Pedido pedido);
    
    // Métodos adicionales útiles:
    
    // Buscar por cantidad mayor que
    List<DetallePedido> findByCantidadGreaterThan(Integer cantidad);
    
    // Buscar por cantidad entre
    List<DetallePedido> findByCantidadBetween(Integer minCantidad, Integer maxCantidad);
    
    // Buscar por subtotal mayor que
    List<DetallePedido> findBySubtotalGreaterThan(BigDecimal subtotal);
    
    // Buscar por pedido ordenado por producto
    List<DetallePedido> findByPedidoOrderByProducto(Pedido pedido);
    
    // Buscar por pedido y cantidad mayor que
    List<DetallePedido> findByPedidoAndCantidadGreaterThan(Pedido pedido, Integer cantidad);
    
    // OPCIONAL: Si necesitas buscar por ID de pedido, añade este método:
    // @Query("SELECT d FROM DetallePedido d WHERE d.pedido.id = :pedidoId")
    // List<DetallePedido> findByPedidoId(@Param("pedidoId") Long pedidoId);
}