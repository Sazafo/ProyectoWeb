package com.tiendaproyecto.tienda.service;

import com.tiendaproyecto.tienda.domain.CarritoItem;
import com.tiendaproyecto.tienda.domain.Usuario;
import com.tiendaproyecto.tienda.domain.Producto;
import com.tiendaproyecto.tienda.repository.CarritoItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CarritoService {
    
    @Autowired
    private CarritoItemRepository carritoItemRepository;
    
    public List<CarritoItem> obtenerCarrito(Usuario usuario) {
        return carritoItemRepository.findByUsuario(usuario);
    }
    
    public BigDecimal calcularTotal(Usuario usuario) {
        List<CarritoItem> items = obtenerCarrito(usuario);
        BigDecimal total = BigDecimal.ZERO;
        
        for (CarritoItem item : items) {
            if (item.getProducto() != null && item.getProducto().getPrecio() != null) {
                BigDecimal precio = item.getProducto().getPrecio();
                BigDecimal cantidad = BigDecimal.valueOf(item.getCantidad());
                total = total.add(precio.multiply(cantidad));
            }
        }
        
        return total;
    }
    
    public void agregarAlCarrito(Usuario usuario, Producto producto, Integer cantidad) {
        // CORRECCIÓN: Usar Optional correctamente
        Optional<CarritoItem> optionalItem = carritoItemRepository.findByUsuarioAndProducto(usuario, producto);
        
        if (optionalItem.isPresent()) {
            // Actualizar cantidad si ya existe
            CarritoItem existente = optionalItem.get();
            existente.setCantidad(existente.getCantidad() + cantidad);
            carritoItemRepository.save(existente);
        } else {
            // Crear nuevo item
            CarritoItem item = new CarritoItem();
            item.setUsuario(usuario);
            item.setProducto(producto);
            item.setCantidad(cantidad);
            carritoItemRepository.save(item);
        }
    }
    
    public void eliminarItem(Long itemId) {
        carritoItemRepository.deleteById(itemId);
    }
    
    public void actualizarCantidad(Long itemId, Integer cantidad) {
        carritoItemRepository.findById(itemId).ifPresent(item -> {
            item.setCantidad(cantidad);
            carritoItemRepository.save(item);
        });
    }
    
    public void vaciarCarrito(Usuario usuario) {
        List<CarritoItem> items = obtenerCarrito(usuario);
        carritoItemRepository.deleteAll(items);
    }
    
    // Métodos adicionales
    public CarritoItem obtenerPorId(Long id) {
        return carritoItemRepository.findById(id).orElse(null);
    }
    
    public void guardar(CarritoItem item) {
        carritoItemRepository.save(item);
    }
}