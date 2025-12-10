package com.tiendaproyecto.tienda.controller;

import com.tiendaproyecto.tienda.domain.Mensaje;
import com.tiendaproyecto.tienda.domain.Usuario;
import com.tiendaproyecto.tienda.service.MensajeService;
import com.tiendaproyecto.tienda.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

/**

 * Ruta: src/main/java/com/tiendaproyecto/tienda/controller/MensajeController.java
 */
@Controller
@RequestMapping("/mensajes")
public class MensajeController {

    @Autowired
    private MensajeService mensajeService;

    @Autowired
    private UsuarioService usuarioService;

    private Usuario obtenerUsuarioActual(Authentication auth) {
        return usuarioService.buscarPorCorreo(auth.getName())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    // HU-019: Listar mensajes
    @GetMapping
    public String listarMensajes(Model model, Authentication auth) {
        Usuario usuario = obtenerUsuarioActual(auth);
        List<Mensaje> mensajes = mensajeService.listarMensajes(usuario);
        long noLeidos = mensajeService.contarNoLeidos(usuario);
        
        model.addAttribute("mensajes", mensajes);
        model.addAttribute("noLeidos", noLeidos);
        return "mensajes/lista";
    }

    // HU-019: Marcar como leído
    @PostMapping("/{id}/leer")
    public String marcarLeido(@PathVariable Long id,
                             RedirectAttributes redirectAttributes) {
        try {
            mensajeService.marcarComoLeido(id);
            return "redirect:/mensajes";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al marcar mensaje");
            return "redirect:/mensajes";
        }
    }

    // HU-019: Eliminar mensaje
    @PostMapping("/{id}/eliminar")
    public String eliminarMensaje(@PathVariable Long id,
                                  RedirectAttributes redirectAttributes) {
        try {
            mensajeService.eliminarMensaje(id);
            redirectAttributes.addFlashAttribute("mensaje", "Mensaje eliminado");
            return "redirect:/mensajes";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar mensaje");
            return "redirect:/mensajes";
        }
    }
}