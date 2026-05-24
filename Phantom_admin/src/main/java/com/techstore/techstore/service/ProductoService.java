package com.techstore.techstore.service;

import com.techstore.techstore.entity.Categoria;
import com.techstore.techstore.entity.Producto;
import com.techstore.techstore.repository.CategoriaRepository;
import com.techstore.techstore.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    // ── Productos ─────────────────────────────────────────────────────────────

    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    public List<Producto> buscarProductos(String keyword) {
        if (keyword == null || keyword.isBlank()) return listarProductos();
        return productoRepository.buscarPorNombre(keyword);
    }

    public Optional<Producto> obtenerProducto(Long id) {
        return productoRepository.findById(id);
    }

    public void guardarProducto(Producto producto) {
        productoRepository.save(producto);
    }

    public void eliminarProducto(Long id) {
        productoRepository.deleteById(id);
    }

    // ── Categorías ────────────────────────────────────────────────────────────

    public List<Categoria> listarCategorias() {
        return categoriaRepository.findAll();
    }

    public Optional<Categoria> obtenerCategoria(Long id) {
        return categoriaRepository.findById(id);
    }

    public void guardarCategoria(Categoria categoria) {
        categoriaRepository.save(categoria);
    }
    // ── Filtro de Stock Bajo ──────────────────────────────────────────────────
    public List<Producto> obtenerStockBajo(Integer limite) {
        return productoRepository.findByStockLessThanEqual(limite);
    }
    public boolean categoriaEstaVacia(Long id) {
        return productoRepository.findByCategoriaId(id).isEmpty();
    }

    public void eliminarCategoria(Long id) {
        categoriaRepository.deleteById(id);
    }

}