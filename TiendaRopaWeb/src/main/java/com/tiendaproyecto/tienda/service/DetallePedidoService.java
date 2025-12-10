package com.tiendaproyecto.tienda.service;

import com.tiendaproyecto.tienda.domain.DetallePedido;
import com.tiendaproyecto.tienda.domain.Pedido;
import com.tiendaproyecto.tienda.domain.Producto;
import com.tiendaproyecto.tienda.repository.DetallePedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DetallePedidoService {
    
    @Autowired
    private DetallePedidoRepository detallePedidoRepository;
    
    // MÉTODO PRINCIPAL QUE FALTA - es llamado desde CarritoController.java línea 166
    public DetallePedido crearDetalle(Pedido pedido, Producto producto, Integer cantidad, BigDecimal precioUnitario) {
        DetallePedido detalle = new DetallePedido(pedido, producto, cantidad, precioUnitario);
        return detallePedidoRepository.save(detalle);
    }
    
    // Método para guardar cualquier detalle (si ya existe una instancia)
    public DetallePedido guardar(DetallePedido detalle) {
        return detallePedidoRepository.save(detalle);
    }
    
    // Método para buscar por ID
    public DetallePedido obtenerPorId(Long id) {
        Optional<DetallePedido> optional = detallePedidoRepository.findById(id);
        return optional.orElse(null);
    }
    
    // Método para obtener por ID del detalle (usando idDetalle)
    public DetallePedido obtenerPorIdDetalle(Long idDetalle) {
        return detallePedidoRepository.findByIdDetalle(idDetalle);
    }
    
    // Método para obtener todos los detalles de un pedido
    public List<DetallePedido> obtenerDetallesPorPedido(Pedido pedido) {
        return detallePedidoRepository.findByPedido(pedido);
    }
    
    // Método para eliminar un detalle por ID
    public void eliminarDetalle(Long id) {
        detallePedidoRepository.deleteById(id);
    }
    
    // Método para eliminar todos los detalles de un pedido
    public void eliminarDetallesPorPedido(Pedido pedido) {
        detallePedidoRepository.deleteByPedido(pedido);
    }
    
    // Método para actualizar un detalle
    public DetallePedido actualizarDetalle(DetallePedido detalle) {
        return detallePedidoRepository.save(detalle);
    }
    
    // Método para actualizar cantidad de un detalle
    public DetallePedido actualizarCantidad(Long idDetalle, Integer nuevaCantidad) {
        DetallePedido detalle = obtenerPorIdDetalle(idDetalle);
        if (detalle != null) {
            detalle.setCantidad(nuevaCantidad);
            // Recalcular subtotal
            if (detalle.getPrecioUnitario() != null) {
                detalle.setSubtotal(detalle.getPrecioUnitario().multiply(BigDecimal.valueOf(nuevaCantidad)));
            }
            return detallePedidoRepository.save(detalle);
        }
        return null;
    }
    
    // Método para calcular el total de un pedido
    public BigDecimal calcularTotalPedido(Pedido pedido) {
        List<DetallePedido> detalles = obtenerDetallesPorPedido(pedido);
        BigDecimal total = BigDecimal.ZERO;
        
        for (DetallePedido detalle : detalles) {
            if (detalle.getSubtotal() != null) {
                total = total.add(detalle.getSubtotal());
            }
        }
        
        return total;
    }
    
    // Método para obtener detalles por producto
    public List<DetallePedido> obtenerDetallesPorProducto(Producto producto) {
        return detallePedidoRepository.findByProducto(producto);
    }
    
    // Método para contar cuántos detalles tiene un pedido
    public Long contarDetallesPorPedido(Pedido pedido) {
        return detallePedidoRepository.countByPedido(pedido);
    }
    
    // Método para buscar detalles con cantidad mayor a un valor
    public List<DetallePedido> buscarPorCantidadMayorQue(Integer cantidad) {
        return detallePedidoRepository.findByCantidadGreaterThan(cantidad);
    }
    
    // Método para buscar detalles por rango de cantidad
    public List<DetallePedido> buscarPorCantidadEntre(Integer min, Integer max) {
        return detallePedidoRepository.findByCantidadBetween(min, max);
    }
}