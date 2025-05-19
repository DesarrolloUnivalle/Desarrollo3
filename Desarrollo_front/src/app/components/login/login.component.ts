import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { UsuarioService } from '../../services/usuario.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  imports: [FormsModule, CommonModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
  standalone: true,

})
export class LoginComponent {
  email: string = '';
  password: string = '';
  errorMessage: string = '';

  constructor(
    private usuarioService: UsuarioService,
    private router: Router
  ) {}

  login() {
    this.usuarioService.login(this.email, this.password).subscribe({
      next: (response) => {
        localStorage.setItem('token', response.token); // Guarda token
        this.router.navigate(['/home']); // Navega a otra página
      },
      error: (err) => {
        this.errorMessage = 'Credenciales incorrectas';
      }
    });
  }

}

