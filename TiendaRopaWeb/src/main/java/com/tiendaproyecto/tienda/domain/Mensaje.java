package com.tiendaproyecto.tienda.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 
 * Ruta: src/main/java/com/tiendaproyecto/tienda/domain/Mensaje.java
 */
@Entity
@Table(name = "mensajes")
public class Mensaje {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mensaje")
    private Long idMensaje;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String contenido;

    @Enumerated(EnumType.STRING)
    private TipoMensaje tipo;

    private Boolean leido = false;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    // Tipos de mensaje
    public enum TipoMensaje {
        PEDIDO_CONFIRMADO,
        PEDIDO_ENVIADO,
        PEDIDO_ENTREGADO,
        PRODUCTO_APROBADO,
        PRODUCTO_RECHAZADO,
        CUENTA_APROBADA,
        SISTEMA
    }

    // Constructor vacío
    public Mensaje() {}

    // Constructor con parámetros
    public Mensaje(Usuario usuario, String titulo, String contenido, TipoMensaje tipo) {
        this.usuario = usuario;
        this.titulo = titulo;
        this.contenido = contenido;
        this.tipo = tipo;
    }

    // Getters y Setters
    public Long getIdMensaje() { return idMensaje; }
    public void setIdMensaje(Long idMensaje) { this.idMensaje = idMensaje; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }

    public TipoMensaje getTipo() { return tipo; }
    public void setTipo(TipoMensaje tipo) { this.tipo = tipo; }

    public Boolean getLeido() { return leido; }
    public void setLeido(Boolean leido) { this.leido = leido; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
