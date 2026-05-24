package utp.phantom.phantom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utp.phantom.phantom.model.Producto;
import utp.phantom.phantom.repository.ProductoRepository;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tienda")
@CrossOrigin(origins = "http://localhost:4200")
public class HomeController {

    @Autowired
    private ProductoRepository productoRepository;

    private static final Map<String, Long> CATEGORIA_IDS = Map.of(
            "consolas",    1L,
            "juegos",      2L,
            "perifericos", 3L,
            "tarjetas",    4L,
            "sillas",      5L
    );

    @GetMapping("/productos/buscar")
    public List<Producto> buscarProductos(@RequestParam String term) {
         return productoRepository.findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCase(term, term);
    }

    @GetMapping("/categoria/{slug}")
    public ResponseEntity<List<Producto>> categoria(@PathVariable String slug) {

        if (!CATEGORIA_IDS.containsKey(slug)) {
            return ResponseEntity.notFound().build();
        }

        Long categoriaId = CATEGORIA_IDS.get(slug);
        List<Producto> productos = productoRepository.findByCategoriaId(categoriaId);
        return ResponseEntity.ok(productos);
    }
}