package com.tiendaproyecto.tienda.controller;

import com.tiendaproyecto.tienda.domain.Usuario;
import com.tiendaproyecto.tienda.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class IndexController {

    @Autowired
    private UsuarioService usuarioService;

    // Página principal
    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }

    // Mostrar formulario de login
    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error,
                       @RequestParam(required = false) String logout,
                       Model model) {
        if (error != null) {
            model.addAttribute("error", "Usuario o contraseña incorrectos");
        }
        if (logout != null) {
            model.addAttribute("mensaje", "Sesión cerrada exitosamente");
        }
        return "login";
    }

    // HU-001: Registro de usuario comprador
    @GetMapping("/registro/comprador")
    public String mostrarRegistroComprador(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro_comprador";
    }

    @PostMapping("/registro/comprador")
    public String registrarComprador(@Valid @ModelAttribute Usuario usuario,
                                    BindingResult result,
                                    RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "registro_comprador";
        }
        
        try {
            usuarioService.registrarComprador(usuario);
            redirectAttributes.addFlashAttribute("mensaje", 
                "Registro exitoso. Ya puedes iniciar sesión");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/registro/comprador";
        }
    }

    // HU-002: Registro de vendedor
    @GetMapping("/registro/vendedor")
    public String mostrarRegistroVendedor(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro_vendedor";
    }

    @PostMapping("/registro/vendedor")
    public String registrarVendedor(@Valid @ModelAttribute Usuario usuario,
                                   BindingResult result,
                                   RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "registro_vendedor";
        }
        
        try {
            usuarioService.registrarVendedor(usuario);
            redirectAttributes.addFlashAttribute("mensaje", 
                "Solicitud enviada. Tu cuenta será revisada pronto");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/registro/vendedor";
        }
    }

    // Página de inicio después del login
    @GetMapping("/inicio")
    public String inicio() {
        return "redirect:/productos";
    }
}