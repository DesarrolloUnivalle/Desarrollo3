import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { ProductoService, Producto } from '../../services/producto.service';

@Component({
  standalone: true,
  selector: 'app-product-detail',
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.css'],
  imports: [CommonModule]
})
export class ProductDetailComponent {
  producto: Producto | null = null;
  error: string = '';

  constructor(private route: ActivatedRoute, private productoService: ProductoService) {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (!isNaN(id)) {
      this.productoService.getProductoById(id).subscribe({
        next: (data) => this.producto = data,
        error: () => this.error = 'No se pudo cargar el producto'
      });
    }
  }
}
