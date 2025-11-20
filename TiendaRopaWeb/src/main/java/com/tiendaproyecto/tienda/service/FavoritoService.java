/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tiendaproyecto.tienda.service;

import com.tiendaproyecto.tienda.domain.Favorito;
import com.tiendaproyecto.tienda.domain.Usuario;
import com.tiendaproyecto.tienda.domain.Producto;
import com.tiendaproyecto.tienda.repository.FavoritoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class FavoritoService {

    @Autowired
    private FavoritoRepository favoritoRepository;

    @Transactional
    public Favorito agregarFavorito(Usuario usuario, Producto producto) {
        if (!favoritoRepository.existsByUsuarioAndProducto(usuario, producto)) {
            Favorito favorito = new Favorito();
            favorito.setUsuario(usuario);
            favorito.setProducto(producto);
            return favoritoRepository.save(favorito);
        }
        return null;
    }

    @Transactional
    public void eliminarFavorito(Usuario usuario, Producto producto) {
        Optional<Favorito> favorito = favoritoRepository.findByUsuarioAndProducto(usuario, producto);
        favorito.ifPresent(favoritoRepository::delete);
    }

    public List<Favorito> listarFavoritos(Usuario usuario) {
        return favoritoRepository.findByUsuario(usuario);
    }

    public boolean esFavorito(Usuario usuario, Producto producto) {
        return favoritoRepository.existsByUsuarioAndProducto(usuario, producto);
    }
}