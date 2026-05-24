import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ProductoService {
  private apiUrl = 'http://localhost:8089/api';

  constructor(private http: HttpClient) {}

  // Tienda pública
  buscarProductos(term: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/tienda/productos/buscar?term=${term}`);
  }

  getByCategoria(slug: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/tienda/categoria/${slug}`);
  }

  // Admin (requiere rol ADMIN en el backend)
  getAll(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/admin/productos`);
  }

  crear(producto: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/admin/productos`, producto);
  }

  actualizar(id: number, producto: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/admin/productos/${id}`, producto);
  }

  eliminar(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/admin/productos/${id}`);
  }
}
