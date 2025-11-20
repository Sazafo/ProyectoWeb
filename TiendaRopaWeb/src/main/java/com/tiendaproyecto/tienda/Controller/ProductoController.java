package com.tiendaproyecto.tienda.controller;

import com.tiendaproyecto.tienda.domain.Producto;
import com.tiendaproyecto.tienda.domain.Usuario;
import com.tiendaproyecto.tienda.service.ProductoService;
import com.tiendaproyecto.tienda.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private UsuarioService usuarioService;

    // Listar todos los productos aprobados
    @GetMapping
    public String listarProductos(Model model) {
        List<Producto> productos = productoService.listarProductosAprobados();
        model.addAttribute("productos", productos);
        return "productos/lista";
    }

    // HU-008: Ver detalle de producto
    @GetMapping("/{id}")
    public String verDetalle(@PathVariable Long id, Model model) {
        Producto producto = productoService.buscarPorId(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        model.addAttribute("producto", producto);
        return "productos/detalle";
    }

    // HU-016: Navegación por categorías
    @GetMapping("/categoria/{categoria}")
    public String listarPorCategoria(@PathVariable String categoria, Model model) {
        List<Producto> productos = productoService.listarPorCategoria(categoria);
        model.addAttribute("productos", productos);
        model.addAttribute("categoriaActual", categoria);
        return "productos/lista";
    }

    // HU-006: Publicar producto (Vendedor)
    @GetMapping("/publicar")
    public String mostrarFormularioPublicar(Model model, Authentication auth) {
        if (auth == null || !auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("VENDEDOR"))) {
            return "redirect:/login";
        }
        model.addAttribute("producto", new Producto());
        return "productos/publicar";
    }

    @PostMapping("/publicar")
    public String publicarProducto(@Valid @ModelAttribute Producto producto,
                                  BindingResult result,
                                  @RequestParam(required = false) MultipartFile imagen,
                                  Authentication auth,
                                  RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "productos/publicar";
        }

        try {
            Usuario vendedor = usuarioService.buscarPorCorreo(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            producto.setVendedor(vendedor);
            productoService.publicarProducto(producto, imagen);
            redirectAttributes.addFlashAttribute("mensaje", 
                "Producto publicado. Está pendiente de aprobación");
            return "redirect:/vendedor/panel";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                "Error al publicar producto: " + e.getMessage());
            return "redirect:/productos/publicar";
        }
    }

    // Buscar productos
    @GetMapping("/buscar")
    public String buscarProductos(@RequestParam String q, Model model) {
        List<Producto> productos = productoService.buscarPorNombre(q);
        model.addAttribute("productos", productos);
        model.addAttribute("busqueda", q);
        return "productos/lista";
    }
}
