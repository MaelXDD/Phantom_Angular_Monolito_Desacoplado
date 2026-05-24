package utp.phantom.phantom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import utp.phantom.phantom.model.Producto;
import utp.phantom.phantom.model.Usuario;
import utp.phantom.phantom.repository.ProductoRepository;
import utp.phantom.phantom.repository.UsuarioRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class ApiController {

    // ==========================================
    // INYECCIÓN DE DEPENDENCIAS
    // ==========================================
    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Mapa estático para las categorías de la tienda
    private static final Map<String, Long> CATEGORIA_IDS = Map.of(
            "consolas",    1L,
            "juegos",      2L,
            "perifericos", 3L,
            "tarjetas",    4L,
            "sillas",      5L
    );

    // ==========================================
    // ENDPOINTS DE AUTENTICACIÓN (/api/auth/...)
    // ==========================================

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();

            if (passwordEncoder.matches(password, usuario.getPassword())) {
                return ResponseEntity.ok(Map.of(
                        "status", "success",
                        "nombre", usuario.getNombre(),
                        "email", usuario.getEmail(),
                        "rol", usuario.getRol() != null ? usuario.getRol().toUpperCase() : "USER"
                ));
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Credenciales de acceso incorrectas"));
    }

    @PostMapping("/auth/registro")
    public ResponseEntity<?> registrar(@RequestBody Usuario usuario) {
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "El correo electrónico ya se encuentra registrado"));
        }

        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        if (usuario.getRol() == null || usuario.getRol().isEmpty()) {
            usuario.setRol("USER");
        }

        Usuario nuevoUsuario = usuarioRepository.save(usuario);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "mensaje", "Usuario registrado correctamente",
                "id", nuevoUsuario.getId()
        ));
    }

    // ==========================================
    // ENDPOINTS DE LA TIENDA PÚBLICA (/api/tienda/...)
    // ==========================================

    @GetMapping("/tienda/productos/buscar")
    public List<Producto> buscarProductos(@RequestParam String term) {
        return productoRepository.findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCase(term, term);
    }

    @GetMapping("/tienda/categoria/{slug}")
    public ResponseEntity<List<Producto>> categoria(@PathVariable String slug) {
        if (!CATEGORIA_IDS.containsKey(slug)) {
            return ResponseEntity.notFound().build();
        }

        Long categoriaId = CATEGORIA_IDS.get(slug);
        List<Producto> productos = productoRepository.findByCategoriaId(categoriaId);
        return ResponseEntity.ok(productos);
    }

    // ==========================================
    // ENDPOINTS DE ADMINISTRACIÓN (/api/admin/productos/...)
    // ==========================================

    @GetMapping("/admin/productos")
    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    @GetMapping("/admin/productos/{id}")
    public ResponseEntity<Producto> obtenerProductoPorId(@PathVariable Long id) {
        return productoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/admin/productos")
    public Producto guardarProducto(@RequestBody Producto producto) {
        return productoRepository.save(producto);
    }

    @PutMapping("/admin/productos/{id}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Long id, @RequestBody Producto productoDetalles) {
        return productoRepository.findById(id)
                .map(producto -> {
                    producto.setNombre(productoDetalles.getNombre());
                    producto.setDescripcion(productoDetalles.getDescripcion());
                    producto.setPrecio(productoDetalles.getPrecio());
                    producto.setStock(productoDetalles.getStock());
                    producto.setImagenUrl(productoDetalles.getImagenUrl());

                    Producto actualizado = productoRepository.save(producto);
                    return ResponseEntity.ok(actualizado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/admin/productos/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}