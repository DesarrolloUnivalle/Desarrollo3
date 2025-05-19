import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ProductoService, Producto } from '../../services/producto.service';

@Component({
  standalone: true,
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css'],
  imports: [CommonModule, RouterModule]
})
export class ProductListComponent {
  productos: Producto[] = [];
  error: string = '';

  constructor(private productoService: ProductoService) {
    this.getProductos();
  }

  getProductos(): void {
    this.productoService.getProductos().subscribe({
      next: (data) => this.productos = data,
      error: (err) => this.error = 'Error al obtener los productos'
    });
  }
}
