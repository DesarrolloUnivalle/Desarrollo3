import { Component } from '@angular/core';
import { ProductoService, Producto } from '../../services/producto.service';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule,RouterLink],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {
  productos: Producto[] = [];

  constructor(private productoService: ProductoService) {}

  ngOnInit() {
    this.obtenerTodosLosProductos();
  }

  obtenerTodosLosProductos() {
    this.productoService.getProductos().subscribe(data => {
      this.productos = data;
    });
  }

  buscar(nombre: string) {
    if (nombre.trim() === '') {
      this.obtenerTodosLosProductos();
    } else {
      this.productoService.getProductosByNombre(nombre).subscribe(data => {
        this.productos = data;
      });
    }
  }
}
