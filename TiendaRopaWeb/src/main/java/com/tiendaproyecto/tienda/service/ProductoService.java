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

    // Ruta correcta dentro de resources/static
    private final String UPLOAD_DIR = "src/main/resources/static/img/productos/";

    // Publicar producto
    @Transactional
    public Producto publicarProducto(Producto producto, MultipartFile imagen) throws IOException {
        // Guardar imagen si existe
        if (imagen != null && !imagen.isEmpty()) {
            String nombreImagen = guardarImagen(imagen);
            producto.setImagen(nombreImagen);
        } else {
            // Imagen por defecto si no se sube ninguna
            producto.setImagen("default-product.jpg");
        }
        producto.setEstado(Producto.Estado.PENDIENTE);
        return productoRepository.save(producto);
    }

    // Guardar imagen en el servidor
    private String guardarImagen(MultipartFile archivo) throws IOException {
        try {
            // Crear directorio si no existe
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generar nombre único
            String extension = "";
            String originalFilename = archivo.getOriginalFilename();
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String nombreUnico = UUID.randomUUID().toString() + extension;
            
            // Guardar archivo
            Path rutaArchivo = uploadPath.resolve(nombreUnico);
            Files.write(rutaArchivo, archivo.getBytes());
            
            return nombreUnico;
        } catch (IOException e) {
            throw new IOException("Error al guardar la imagen: " + e.getMessage());
        }
    }

    // Listar productos aprobados
    public List<Producto> listarProductosAprobados() {
        return productoRepository.findByEstado(Producto.Estado.APROBADO);
    }

    // Listar productos por categoría
    public List<Producto> listarPorCategoria(String categoria) {
        List<Producto> productos = productoRepository.findByCategoria(categoria);
        // Filtrar solo aprobados
        return productos.stream()
            .filter(p -> p.getEstado() == Producto.Estado.APROBADO)
            .toList();
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