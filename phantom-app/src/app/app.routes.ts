// app.routes.ts
import { Routes } from '@angular/router';
import { Login } from './admin/auth/login/login'; // Tu componente de login

export const routes: Routes = [
  { path: 'login', component: Login },
  { path: 'inicio', loadComponent: () => import('./tienda/tienda').then((m) => m.TiendaComponent) },
  { path: 'admin', loadComponent: () => import('./admin/admin').then((m) => m.AdminComponent) },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
];
