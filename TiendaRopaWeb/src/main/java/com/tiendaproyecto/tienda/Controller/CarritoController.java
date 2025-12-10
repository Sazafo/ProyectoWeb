package com.tiendaproyecto.tienda.controller;

import com.tiendaproyecto.tienda.domain.*;
import com.tiendaproyecto.tienda.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.math.BigDecimal;
import java.util.List;

/**

 * Ruta: src/main/java/com/tiendaproyecto/tienda/controller/CarritoController.java
 */
@Controller
@RequestMapping("/carrito")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private DireccionService direccionService;

    @Autowired
    private MensajeService mensajeService;

    @Autowired
    private DetallePedidoService detallePedidoService;

    private Usuario obtenerUsuarioActual(Authentication auth) {
        return usuarioService.buscarPorCorreo(auth.getName())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    // HU-010: Ver carrito
    @GetMapping
    public String verCarrito(Model model, Authentication auth) {
        Usuario usuario = obtenerUsuarioActual(auth);
        List<CarritoItem> items = carritoService.obtenerCarrito(usuario);
        BigDecimal total = carritoService.calcularTotal(usuario);
        
        model.addAttribute("items", items);
        model.addAttribute("total", total);
        return "carrito/ver";
    }

    // HU-010: Agregar al carrito
    @PostMapping("/agregar/{idProducto}")
    public String agregarAlCarrito(@PathVariable Long idProducto,
                                   @RequestParam(defaultValue = "1") Integer cantidad,
                                   Authentication auth,
                                   RedirectAttributes redirectAttributes) {
        try {
            Usuario usuario = obtenerUsuarioActual(auth);
            Producto producto = productoService.buscarPorId(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
            
            if (producto.getStock() < cantidad) {
                redirectAttributes.addFlashAttribute("error", "Stock insuficiente");
                return "redirect:/productos/" + idProducto;
            }
            
            carritoService.agregarAlCarrito(usuario, producto, cantidad);
            redirectAttributes.addFlashAttribute("mensaje", "Producto agregado al carrito");
            return "redirect:/carrito";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al agregar al carrito");
            return "redirect:/productos";
        }
    }

    // HU-010: Actualizar cantidad
    @PostMapping("/actualizar/{idItem}")
    public String actualizarCantidad(@PathVariable Long idItem,
                                    @RequestParam Integer cantidad,
                                    RedirectAttributes redirectAttributes) {
        try {
            if (cantidad <= 0) {
                carritoService.eliminarItem(idItem);
            } else {
                carritoService.actualizarCantidad(idItem, cantidad);
            }
            redirectAttributes.addFlashAttribute("mensaje", "Carrito actualizado");
            return "redirect:/carrito";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar");
            return "redirect:/carrito";
        }
    }

    // HU-010: Eliminar item
    @PostMapping("/eliminar/{idItem}")
    public String eliminarItem(@PathVariable Long idItem,
                              RedirectAttributes redirectAttributes) {
        try {
            carritoService.eliminarItem(idItem);
            redirectAttributes.addFlashAttribute("mensaje", "Producto eliminado del carrito");
            return "redirect:/carrito";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar");
            return "redirect:/carrito";
        }
    }

    // HU-011: Checkout
    @GetMapping("/checkout")
    public String mostrarCheckout(Model model, Authentication auth) {
        Usuario usuario = obtenerUsuarioActual(auth);
        List<CarritoItem> items = carritoService.obtenerCarrito(usuario);
        
        if (items.isEmpty()) {
            return "redirect:/carrito";
        }
        
        List<Direccion> direcciones = direccionService.listarPorUsuario(usuario);
        BigDecimal total = carritoService.calcularTotal(usuario);
        
        model.addAttribute("items", items);
        model.addAttribute("direcciones", direcciones);
        model.addAttribute("total", total);
        return "carrito/checkout";
    }

    // HU-012: Procesar pago
    @PostMapping("/procesar-pago")
    public String procesarPago(@RequestParam Long idDireccion,
                              @RequestParam String metodoPago,
                              Authentication auth,
                              RedirectAttributes redirectAttributes) {
        try {
            Usuario usuario = obtenerUsuarioActual(auth);
            List<CarritoItem> items = carritoService.obtenerCarrito(usuario);
            
            if (items.isEmpty()) {
                throw new RuntimeException("Carrito vacío");
            }
            
            // Obtener dirección
            Direccion direccion = direccionService.buscarPorId(idDireccion)
                .orElseThrow(() -> new RuntimeException("Dirección no encontrada"));
            
            String direccionCompleta = direccion.getDireccion() + ", " + 
                                      direccion.getCiudad() + ", " + 
                                      direccion.getCodigoPostal();
            
            // Crear pedido
            BigDecimal total = carritoService.calcularTotal(usuario);
            Pedido pedido = new Pedido(usuario, total, direccionCompleta, metodoPago);
            pedido = pedidoService.crearPedido(pedido);
            
            // Crear detalles del pedido
            for (CarritoItem item : items) {
                detallePedidoService.crearDetalle(pedido, item.getProducto(), 
                                                 item.getCantidad(), item.getPrecioUnitario());
                
                // Reducir stock
                Producto producto = item.getProducto();
                producto.setStock(producto.getStock() - item.getCantidad());
                productoService.actualizarProducto(producto);
            }
            
            // Simular pago (en producción integrar pasarela real)
            boolean pagoExitoso = simularPago(metodoPago);
            
            if (pagoExitoso) {
                pedido.setEstado(Pedido.Estado.PAGADO);
                pedidoService.crearPedido(pedido);
                
                // Crear mensaje
                mensajeService.crearMensaje(usuario, 
                    "Pedido confirmado", 
                    "Tu pedido #" + pedido.getIdPedido() + " ha sido confirmado y pagado.",
                    Mensaje.TipoMensaje.PEDIDO_CONFIRMADO);
                
                // Vaciar carrito
                carritoService.vaciarCarrito(usuario);
                
                redirectAttributes.addFlashAttribute("mensaje", "¡Pago exitoso! Pedido #" + pedido.getIdPedido());
                return "redirect:/comprador/pedidos";
            } else {
                redirectAttributes.addFlashAttribute("error", "Pago fallido. Intenta nuevamente.");
                return "redirect:/carrito/checkout";
            }
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al procesar pago: " + e.getMessage());
            return "redirect:/carrito/checkout";
        }
    }

    // Simulación de pago (HU-012)
    private boolean simularPago(String metodoPago) {
        // Simula 95% de éxito
        return Math.random() < 0.95;
    }
}