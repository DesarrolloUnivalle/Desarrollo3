package com.tienda.productos.service.impl;

import com.tienda.productos.dto.ProductDTO;
import com.tienda.productos.exception.ResourceNotFoundException;
import com.tienda.productos.model.OrderItem;
import com.tienda.productos.model.Product;
import com.tienda.productos.model.Categoria;
import com.tienda.productos.repository.ProductRepository;
import com.tienda.productos.repository.CategoriaRepository;
import com.tienda.productos.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setNombre(productDTO.getNombre());
        product.setDescripcion(productDTO.getDescripcion());
        product.setPrecio(productDTO.getPrecio());
        product.setStock(productDTO.getStock());
        product.setImagenUrl(productDTO.getImagenUrl());
        
        if (productDTO.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(productDTO.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con id: " + productDTO.getCategoriaId()));
            product.setCategoria(categoria);
        }
        
        Product savedProduct = productRepository.save(product);
        return convertToDTO(savedProduct);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
        
        product.setNombre(productDTO.getNombre());
        product.setDescripcion(productDTO.getDescripcion());
        product.setPrecio(productDTO.getPrecio());
        product.setStock(productDTO.getStock());
        product.setImagenUrl(productDTO.getImagenUrl());
        
        if (productDTO.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(productDTO.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con id: " + productDTO.getCategoriaId()));
            product.setCategoria(categoria);
        }
        
        Product updatedProduct = productRepository.save(product);
        return convertToDTO(updatedProduct);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Producto no encontrado con id: " + id);
        }
        productRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
        return convertToDTO(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> searchProducts(String palabra_clave) {
        if (palabra_clave == null || palabra_clave.trim().isEmpty()) {
            return productRepository.findAll().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        }
        return productRepository.findByNombreContainingIgnoreCase(palabra_clave).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void actualizarStock(Long id, Integer cantidad) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
        
        int nuevoStock = product.getStock() - cantidad;
        if (nuevoStock < 0) {
            throw new IllegalStateException("Stock insuficiente para el producto: " + id);
        }
        
        product.setStock(nuevoStock);
        productRepository.save(product);
    }

    @Override
    @Transactional(readOnly = true)
    public void validarStock(List<OrderItem> items) {
        for (OrderItem item : items) {
            Product product = productRepository.findById(item.getProductoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + item.getProductoId()));
            
            if (product.getStock() < item.getCantidad()) {
                throw new IllegalStateException("Stock insuficiente para el producto: " + item.getProductoId());
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setNombre(product.getNombre());
        dto.setDescripcion(product.getDescripcion());
        dto.setPrecio(product.getPrecio());
        dto.setStock(product.getStock());
        dto.setImagenUrl(product.getImagenUrl());
        if (product.getCategoria() != null) {
            dto.setCategoriaId(product.getCategoria().getId());
        }
        return dto;
    }
}
