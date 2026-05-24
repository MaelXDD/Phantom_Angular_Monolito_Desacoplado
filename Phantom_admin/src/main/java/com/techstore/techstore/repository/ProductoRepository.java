package com.techstore.techstore.repository;

import com.techstore.techstore.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    @Query("SELECT p FROM Producto p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Producto> buscarPorNombre(@Param("keyword") String keyword);

    List<Producto> findByCategoriaId(Long categoriaId);

    List<Producto> findByStockLessThanEqual(Integer limite);
}