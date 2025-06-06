import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor() {}

  // Verifica si hay token
  isAuthenticated(): boolean {
    return !!localStorage.getItem('token');
  }

  // Obtiene el token
  getToken(): string | null {
    return localStorage.getItem('token');
  }

  // Cierra sesión
  logout(): void {
    localStorage.removeItem('token');
  }
}
