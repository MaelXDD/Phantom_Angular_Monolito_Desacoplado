import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private apiUrl = 'http://localhost:8089/api/auth'; // ← puerto corregido

  constructor(private http: HttpClient) {}

  login(credentials: { email: string; password: string }): Observable<any> {
    return this.http.post(`${this.apiUrl}/login`, credentials).pipe(
      tap((res: any) => {
        localStorage.setItem('usuario', JSON.stringify(res));
        localStorage.setItem('rol', res.rol);
      }),
    );
  }

  registro(usuario: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/registro`, usuario);
  }

  logout() {
    localStorage.removeItem('usuario');
    localStorage.removeItem('rol');
  }

  getUsuario(): any {
    const u = localStorage.getItem('usuario');
    return u ? JSON.parse(u) : null;
  }

  isAdmin(): boolean {
    return localStorage.getItem('rol') === 'ADMIN';
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('usuario');
  }
}
