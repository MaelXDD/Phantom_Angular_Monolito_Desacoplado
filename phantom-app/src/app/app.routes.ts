import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: 'inicio',
    loadComponent: () => import('./tienda/tienda').then((c) => c.Tienda),
  },
  {
    path: 'admin',
    loadComponent: () => import('./admin/admin').then((c) => c.Admin),
  },
  {
    path: 'login',
    loadComponent: () => import('./auth/login/login').then((c) => c.Login),
  },
  {
    path: '',
    redirectTo: '/inicio',
    pathMatch: 'full',
  },
];
