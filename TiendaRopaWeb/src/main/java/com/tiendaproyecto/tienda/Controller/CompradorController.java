package com.tiendaproyecto.tienda.controller;

import com.tiendaproyecto.tienda.domain.*;
import com.tiendaproyecto.tienda.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequestMapping("/comprador")
public class CompradorController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private DireccionService direccionService;

    @Autowired
    private FavoritoService favoritoService;

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private ProductoService productoService;

    // Obtener usuario actual
    private Usuario obtenerUsuarioActual(Authentication auth) {
        return usuarioService.buscarPorCorreo(auth.getName())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    // HU-017: Editar perfil
    @GetMapping("/perfil")
    public String verPerfil(Model model, Authentication auth) {
        Usuario usuario = obtenerUsuarioActual(auth);
        model.addAttribute("usuario", usuario);
        return "comprador/perfil";
    }

    @PostMapping("/perfil")
    public String actualizarPerfil(@Valid @ModelAttribute Usuario usuarioForm,
                                   BindingResult result,
                                   Authentication auth,
                                   RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "comprador/perfil";
        }

        try {
            Usuario usuario = obtenerUsuarioActual(auth);
            usuario.setNombre(usuarioForm.getNombre());
            usuario.setTelefono(usuarioForm.getTelefono());
            usuarioService.actualizarPerfil(usuario);
            redirectAttributes.addFlashAttribute("mensaje", "Perfil actualizado exitosamente");
            return "redirect:/comprador/perfil";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar perfil");
            return "redirect:/comprador/perfil";
        }
    }

    // HU-018: Gestión de direcciones
    @GetMapping("/direcciones")
    public String listarDirecciones(Model model, Authentication auth) {
        Usuario usuario = obtenerUsuarioActual(auth);
        List<Direccion> direcciones = direccionService.listarPorUsuario(usuario);
        model.addAttribute("direcciones", direcciones);
        model.addAttribute("nuevaDireccion", new Direccion());
        return "comprador/direcciones";
    }

    @PostMapping("/direcciones")
    public String agregarDireccion(@Valid @ModelAttribute Direccion direccion,
                                   BindingResult result,
                                   Authentication auth,
                                   RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "comprador/direcciones";
        }

        try {
            Usuario usuario = obtenerUsuarioActual(auth);
            direccion.setUsuario(usuario);
            direccionService.guardarDireccion(direccion);
            redirectAttributes.addFlashAttribute("mensaje", "Dirección agregada exitosamente");
            return "redirect:/comprador/direcciones";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al agregar dirección");
            return "redirect:/comprador/direcciones";
        }
    }

    @PostMapping("/direcciones/{id}/eliminar")
    public String eliminarDireccion(@PathVariable Long id, 
                                    RedirectAttributes redirectAttributes) {
        try {
            direccionService.eliminarDireccion(id);
            redirectAttributes.addFlashAttribute("mensaje", "Dirección eliminada");
            return "redirect:/comprador/direcciones";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar dirección");
            return "redirect:/comprador/direcciones";
        }
    }

    // HU-013: Historial de pedidos
    @GetMapping("/pedidos")
    public String historialPedidos(Model model, Authentication auth) {
        Usuario usuario = obtenerUsuarioActual(auth);
        List<Pedido> pedidos = pedidoService.listarPedidosUsuario(usuario);
        model.addAttribute("pedidos", pedidos);
        return "comprador/pedidos";
    }

    @GetMapping("/pedidos/{id}")
    public String verDetallePedido(@PathVariable Long id, Model model, Authentication auth) {
        Pedido pedido = pedidoService.buscarPorId(id)
            .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        
        // Verificar que el pedido pertenece al usuario actual
        Usuario usuario = obtenerUsuarioActual(auth);
        if (!pedido.getUsuario().getIdUsuario().equals(usuario.getIdUsuario())) {
            throw new RuntimeException("Acceso no autorizado");
        }
        
        model.addAttribute("pedido", pedido);
        return "comprador/pedido_detalle";
    }

    // HU-020: Lista de deseos (Favoritos)
    @GetMapping("/favoritos")
    public String listarFavoritos(Model model, Authentication auth) {
        Usuario usuario = obtenerUsuarioActual(auth);
        List<Favorito> favoritos = favoritoService.listarFavoritos(usuario);
        model.addAttribute("favoritos", favoritos);
        return "comprador/favoritos";
    }

    @PostMapping("/favoritos/{idProducto}")
    public String agregarFavorito(@PathVariable Long idProducto,
                                  Authentication auth,
                                  RedirectAttributes redirectAttributes) {
        try {
            Usuario usuario = obtenerUsuarioActual(auth);
            Producto producto = productoService.buscarPorId(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
            
            favoritoService.agregarFavorito(usuario, producto);
            redirectAttributes.addFlashAttribute("mensaje", "Producto agregado a favoritos");
            return "redirect:/productos/" + idProducto;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al agregar favorito");
            return "redirect:/productos";
        }
    }

    @PostMapping("/favoritos/{idProducto}/eliminar")
    public String eliminarFavorito(@PathVariable Long idProducto,
                                   Authentication auth,
                                   RedirectAttributes redirectAttributes) {
        try {
            Usuario usuario = obtenerUsuarioActual(auth);
            Producto producto = productoService.buscarPorId(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
            
            favoritoService.eliminarFavorito(usuario, producto);
            redirectAttributes.addFlashAttribute("mensaje", "Favorito eliminado");
            return "redirect:/comprador/favoritos";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar favorito");
            return "redirect:/comprador/favoritos";
        }
    }
}

