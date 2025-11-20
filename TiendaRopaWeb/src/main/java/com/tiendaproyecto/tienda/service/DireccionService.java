/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tiendaproyecto.tienda.service;

import com.tiendaproyecto.tienda.domain.Direccion;
import com.tiendaproyecto.tienda.domain.Usuario;
import com.tiendaproyecto.tienda.repository.DireccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class DireccionService {

    @Autowired
    private DireccionRepository direccionRepository;

    @Transactional
    public Direccion guardarDireccion(Direccion direccion) {
        // Si se marca como principal, desmarcar las demás
        if (direccion.getEsPrincipal()) {
            List<Direccion> direcciones = direccionRepository.findByUsuario(direccion.getUsuario());
            direcciones.forEach(d -> d.setEsPrincipal(false));
            direccionRepository.saveAll(direcciones);
        }
        return direccionRepository.save(direccion);
    }

    public List<Direccion> listarPorUsuario(Usuario usuario) {
        return direccionRepository.findByUsuario(usuario);
    }

    public Optional<Direccion> buscarPorId(Long id) {
        return direccionRepository.findById(id);
    }

    @Transactional
    public void eliminarDireccion(Long id) {
        direccionRepository.deleteById(id);
    }

    public Optional<Direccion> obtenerDireccionPrincipal(Usuario usuario) {
        return direccionRepository.findByUsuarioAndEsPrincipalTrue(usuario);
    }
}