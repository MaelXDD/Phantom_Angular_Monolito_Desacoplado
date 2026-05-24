import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProductoService } from '../admin/core/services/producto';

@Component({
  selector: 'app-tienda',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './tienda.html',
})
export class Tienda implements OnInit {
  productos: any[] = [];
  termBusqueda = '';
  categorias = ['consolas', 'juegos', 'perifericos', 'tarjetas', 'sillas'];
  categoriaActiva = '';

  constructor(private productoService: ProductoService) {}

  ngOnInit() {
    this.cargarCategoria('consolas');
  }

  cargarCategoria(slug: string) {
    this.categoriaActiva = slug;
    this.productoService.getByCategoria(slug).subscribe({
      next: (data) => (this.productos = data),
      error: (err) => console.error('Error cargando categoría', err),
    });
  }

  buscar() {
    if (!this.termBusqueda.trim()) return;
    this.productoService.buscarProductos(this.termBusqueda).subscribe({
      next: (data) => (this.productos = data),
      error: (err) => console.error('Error buscando', err),
    });
  }
}
