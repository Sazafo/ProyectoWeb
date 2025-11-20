/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tiendaproyecto.tienda.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "direcciones")
public class Direccion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_direccion")
    private Long idDireccion;

    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    @NotBlank(message = "La ciudad es obligatoria")
    private String ciudad;

    @NotBlank(message = "El código postal es obligatorio")
    @Column(name = "codigo_postal")
    private String codigoPostal;

    private String referencia;

    @Column(name = "es_principal")
    private Boolean esPrincipal = false;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    // Constructor vacío
    public Direccion() {}

    // Getters y Setters
    public Long getIdDireccion() { return idDireccion; }
    public void setIdDireccion(Long idDireccion) { this.idDireccion = idDireccion; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }

    public String getCodigoPostal() { return codigoPostal; }
    public void setCodigoPostal(String codigoPostal) { this.codigoPostal = codigoPostal; }

    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }

    public Boolean getEsPrincipal() { return esPrincipal; }
    public void setEsPrincipal(Boolean esPrincipal) { this.esPrincipal = esPrincipal; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}
