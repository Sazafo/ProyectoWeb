/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tiendaproyecto.tienda.service;

import com.tiendaproyecto.tienda.domain.Producto;
import com.tiendaproyecto.tienda.domain.Usuario;
import com.tiendaproyecto.tienda.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    private final String UPLOAD_DIR = "src/main/resources/static/img/productos/";

    // Publicar producto
    @Transactional
    public Producto publicarProducto(Producto producto, MultipartFile imagen) throws IOException {
        // Guardar imagen si existe
        if (imagen != null && !imagen.isEmpty()) {
            String nombreImagen = guardarImagen(imagen);
            producto.setImagen(nombreImagen);
        }
        producto.setEstado(Producto.Estado.PENDIENTE);
        return productoRepository.save(producto);
    }

    // Guardar imagen en el servidor
    private String guardarImagen(MultipartFile archivo) throws IOException {
        String nombreUnico = UUID.randomUUID().toString() + "_" + archivo.getOriginalFilename();
        Path rutaArchivo = Paths.get(UPLOAD_DIR + nombreUnico);
        Files.createDirectories(rutaArchivo.getParent());
        Files.write(rutaArchivo, archivo.getBytes());
        return nombreUnico;
    }

    // Listar productos aprobados
    public List<Producto> listarProductosAprobados() {
        return productoRepository.findByEstado(Producto.Estado.APROBADO);
    }

    // Listar productos por categoría
    public List<Producto> listarPorCategoria(String categoria) {
        return productoRepository.findByCategoria(categoria);
    }

    // Listar productos del vendedor
    public List<Producto> listarProductosVendedor(Usuario vendedor) {
        return productoRepository.findByVendedor(vendedor);
    }

    // Buscar producto por ID
    public Optional<Producto> buscarPorId(Long id) {
        return productoRepository.findById(id);
    }

    // Buscar productos por nombre
    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    // Actualizar producto
    @Transactional
    public Producto actualizarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    // Eliminar producto
    @Transactional
    public void eliminarProducto(Long id) {
        productoRepository.deleteById(id);
    }
}