/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.tiendaproyecto.tienda.repository;

import com.tiendaproyecto.tienda.domain.Favorito;
import com.tiendaproyecto.tienda.domain.Usuario;
import com.tiendaproyecto.tienda.domain.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FavoritoRepository extends JpaRepository<Favorito, Long> {
    List<Favorito> findByUsuario(Usuario usuario);
    Optional<Favorito> findByUsuarioAndProducto(Usuario usuario, Producto producto);
    boolean existsByUsuarioAndProducto(Usuario usuario, Producto producto);
}