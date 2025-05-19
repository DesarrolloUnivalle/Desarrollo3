package com.tienda.productos.service.impl;

import com.tienda.productos.dto.ProductDTO;
import com.tienda.productos.model.Categoria;
import com.tienda.productos.model.Product;
import com.tienda.productos.repository.CategoriaRepository;
import com.tienda.productos.repository.ProductRepository;
import com.tienda.productos.service.ProductService;

import jakarta.transaction.Transactional;

import java.util.NoSuchElementException;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoriaRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository, CategoriaRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    @Override
    public Product createProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setNombre(productDTO.getNombre());
        product.setDescripcion(productDTO.getDescripcion());
        product.setPrecio(productDTO.getPrecio());
        product.setStock(productDTO.getStock());

        // Buscar la categoría en la BD y asignarla
        Categoria category = categoryRepository.findById(productDTO.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        product.setCategoria(category);

        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Long id, ProductDTO productDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        product.setNombre(productDTO.getNombre());
        product.setDescripcion(productDTO.getDescripcion());
        product.setPrecio(productDTO.getPrecio());
        product.setStock(productDTO.getStock());

        // Buscar y actualizar la categoría si viene una nueva
        if (productDTO.getCategoriaId() != null) {
            Categoria category = categoryRepository.findById(productDTO.getCategoriaId())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            product.setCategoria(category);
        }

        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado");
        }
        productRepository.deleteById(id);
    }   

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
}

@Override
    public List<Product> searchProducts(String keyword) {
        List<Product> products = productRepository.findByNombreContainingIgnoreCase(keyword)
                .orElseThrow(() -> new NoSuchElementException("No se encontraron productos con la palabra clave: " + keyword));
        return products;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }


}
