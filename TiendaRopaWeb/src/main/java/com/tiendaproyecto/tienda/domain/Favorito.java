/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tiendaproyecto.tienda.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "favoritos")
public class Favorito {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_favorito")
    private Long idFavorito;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Producto producto;

    @Column(name = "fecha_agregado")
    private LocalDateTime fechaAgregado = LocalDateTime.now();

    // Constructor vacío
    public Favorito() {}

    // Constructor con parámetros
    public Favorito(Usuario usuario, Producto producto) {
        this.usuario = usuario;
        this.producto = producto;
    }

    // Getters y Setters
    public Long getIdFavorito() { 
        return idFavorito; 
    }
    
    public void setIdFavorito(Long idFavorito) { 
        this.idFavorito = idFavorito; 
    }

    public Usuario getUsuario() { 
        return usuario; 
    }
    
    public void setUsuario(Usuario usuario) { 
        this.usuario = usuario; 
    }

    public Producto getProducto() { 
        return producto; 
    }
    
    public void setProducto(Producto producto) { 
        this.producto = producto; 
    }

    public LocalDateTime getFechaAgregado() { 
        return fechaAgregado; 
    }
    
    public void setFechaAgregado(LocalDateTime fechaAgregado) { 
        this.fechaAgregado = fechaAgregado; 
    }
}