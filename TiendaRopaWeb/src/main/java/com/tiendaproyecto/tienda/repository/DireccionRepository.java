/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.tiendaproyecto.tienda.repository;

import com.tiendaproyecto.tienda.domain.Direccion;
import com.tiendaproyecto.tienda.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DireccionRepository extends JpaRepository<Direccion, Long> {
    List<Direccion> findByUsuario(Usuario usuario);
    Optional<Direccion> findByUsuarioAndEsPrincipalTrue(Usuario usuario);
}