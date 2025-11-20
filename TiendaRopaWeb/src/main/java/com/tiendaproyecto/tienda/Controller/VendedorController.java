package com.tiendaproyecto.tienda.controller;

import com.tiendaproyecto.tienda.domain.Producto;
import com.tiendaproyecto.tienda.domain.Usuario;
import com.tiendaproyecto.tienda.service.ProductoService;
import com.tiendaproyecto.tienda.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequestMapping("/vendedor")
public class VendedorController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ProductoService productoService;

    // Obtener usuario actual
    private Usuario obtenerUsuarioActual(Authentication auth) {
        return usuarioService.buscarPorCorreo(auth.getName())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    // HU-014: Panel del vendedor
    @GetMapping("/panel")
    public String panelVendedor(Model model, Authentication auth) {
        Usuario vendedor = obtenerUsuarioActual(auth);
        
        // Verificar si el vendedor está aprobado
        if (!vendedor.getAprobado()) {
            model.addAttribute("mensaje", "Tu cuenta está pendiente de aprobación");
            return "vendedor/pendiente";
        }
        
        List<Producto> productos = productoService.listarProductosVendedor(vendedor);
        model.addAttribute("productos", productos);
        model.addAttribute("vendedor", vendedor);
        return "vendedor/panel";
    }

    // HU-015: Inventario de productos
    @GetMapping("/inventario")
    public String verInventario(Model model, Authentication auth) {
        Usuario vendedor = obtenerUsuarioActual(auth);
        List<Producto> productos = productoService.listarProductosVendedor(vendedor);
        model.addAttribute("productos", productos);
        return "vendedor/inventario";
    }

    // Editar producto
    @GetMapping("/productos/{id}/editar")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, Authentication auth) {
        Usuario vendedor = obtenerUsuarioActual(auth);
        Producto producto = productoService.buscarPorId(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        // Verificar que el producto pertenece al vendedor
        if (!producto.getVendedor().getIdUsuario().equals(vendedor.getIdUsuario())) {
            throw new RuntimeException("No autorizado");
        }
        
        model.addAttribute("producto", producto);
        return "vendedor/editar_producto";
    }

    @PostMapping("/productos/{id}/editar")
    public String actualizarProducto(@PathVariable Long id,
                                    @ModelAttribute Producto productoForm,
                                    Authentication auth,
                                    RedirectAttributes redirectAttributes) {
        try {
            Usuario vendedor = obtenerUsuarioActual(auth);
            Producto producto = productoService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
            
            // Verificar propiedad
            if (!producto.getVendedor().getIdUsuario().equals(vendedor.getIdUsuario())) {
                throw new RuntimeException("No autorizado");
            }
            
            // Actualizar datos
            producto.setNombre(productoForm.getNombre());
            producto.setDescripcion(productoForm.getDescripcion());
            producto.setPrecio(productoForm.getPrecio());
            producto.setStock(productoForm.getStock());
            producto.setCategoria(productoForm.getCategoria());
            producto.setTalla(productoForm.getTalla());
            producto.setColor(productoForm.getColor());
            
            // Alerta si stock bajo
            if (producto.getStock() != null && producto.getStock() < 5) {
                redirectAttributes.addFlashAttribute("alerta", 
                    "⚠ Stock bajo: solo quedan " + producto.getStock() + " unidades");
            }
            
            productoService.actualizarProducto(producto);
            redirectAttributes.addFlashAttribute("mensaje", "Producto actualizado exitosamente");
            return "redirect:/vendedor/inventario";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar producto");
            return "redirect:/vendedor/inventario";
        }
    }

    // Eliminar producto
    @PostMapping("/productos/{id}/eliminar")
    public String eliminarProducto(@PathVariable Long id,
                                   Authentication auth,
                                   RedirectAttributes redirectAttributes) {
        try {
            Usuario vendedor = obtenerUsuarioActual(auth);
            Producto producto = productoService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
            
            // Verificar propiedad
            if (!producto.getVendedor().getIdUsuario().equals(vendedor.getIdUsuario())) {
                throw new RuntimeException("No autorizado");
            }
            
            productoService.eliminarProducto(id);
            redirectAttributes.addFlashAttribute("mensaje", "Producto eliminado");
            return "redirect:/vendedor/inventario";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar producto");
            return "redirect:/vendedor/inventario";
        }
    }
}

