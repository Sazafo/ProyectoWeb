/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.tiendaproyecto.tienda.repository;

import com.tiendaproyecto.tienda.domain.Producto;
import com.tiendaproyecto.tienda.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByCategoria(String categoria);
    List<Producto> findByVendedor(Usuario vendedor);
    List<Producto> findByEstado(Producto.Estado estado);
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
}