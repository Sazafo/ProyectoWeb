/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tiendaproyecto.tienda.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pedidos")
public class Pedido {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido")
    private Long idPedido;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    private Estado estado = Estado.PENDIENTE;

    @Column(name = "fecha_pedido")
    private LocalDateTime fechaPedido = LocalDateTime.now();

    @Column(name = "direccion_envio")
    private String direccionEnvio;

    @Column(name = "metodo_pago")
    private String metodoPago;

    // Enum para estados
    public enum Estado {
        PENDIENTE, PAGADO, ENVIADO, ENTREGADO, CANCELADO
    }

    // Constructor vacío
    public Pedido() {}

    // Constructor con parámetros
    public Pedido(Usuario usuario, BigDecimal total, String direccionEnvio, String metodoPago) {
        this.usuario = usuario;
        this.total = total;
        this.direccionEnvio = direccionEnvio;
        this.metodoPago = metodoPago;
    }

    // Getters y Setters
    public Long getIdPedido() { 
        return idPedido; 
    }
    
    public void setIdPedido(Long idPedido) { 
        this.idPedido = idPedido; 
    }

    public Usuario getUsuario() { 
        return usuario; 
    }
    
    public void setUsuario(Usuario usuario) { 
        this.usuario = usuario; 
    }

    public BigDecimal getTotal() { 
        return total; 
    }
    
    public void setTotal(BigDecimal total) { 
        this.total = total; 
    }

    public Estado getEstado() { 
        return estado; 
    }
    
    public void setEstado(Estado estado) { 
        this.estado = estado; 
    }

    public LocalDateTime getFechaPedido() { 
        return fechaPedido; 
    }
    
    public void setFechaPedido(LocalDateTime fechaPedido) { 
        this.fechaPedido = fechaPedido; 
    }

    public String getDireccionEnvio() { 
        return direccionEnvio; 
    }
    
    public void setDireccionEnvio(String direccionEnvio) { 
        this.direccionEnvio = direccionEnvio; 
    }

    public String getMetodoPago() { 
        return metodoPago; 
    }
    
    public void setMetodoPago(String metodoPago) { 
        this.metodoPago = metodoPago; 
    }
}