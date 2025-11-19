/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tiendaproyecto.tienda.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
/**
 *
 * @author bren
 */
@Entity
@Table(name = "vendedores")
public class Vendedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del negocio es obligatorio")
    private String nombreNegocio;

    @NotBlank(message = "El teléfono es obligatorio")
    private String telefono;

    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    private boolean aprobado = false; // Por defecto, pendiente de aprobación

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombreNegocio() { return nombreNegocio; }
    public void setNombreNegocio(String nombreNegocio) { this.nombreNegocio = nombreNegocio; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public boolean isAprobado() { return aprobado; }
    public void setAprobado(boolean aprobado) { this.aprobado = aprobado; }
}
