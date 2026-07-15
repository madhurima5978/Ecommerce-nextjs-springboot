package com.shopping.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shopping.ecommerce.service.ProductService;
import com.shopping.ecommerce.service.UserService;
import com.shopping.ecommerce.dto.ProductRequest;
import com.shopping.ecommerce.dto.ProductResponse;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
public class ProductController {

	@Autowired
    private ProductService productService;
	
	@GetMapping("/products")
	public Page<ProductResponse> getAll( @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id")String sort, @RequestParam(defaultValue = "desc")String direction) {
		return productService.getAll(page, size, sort, direction);
	}
	
	@PostMapping("/products")
	@PreAuthorize("hasRole('ADMIN')")
	public ProductResponse create(@RequestBody ProductRequest productRequest) {
		
		return productService.create(productRequest);
	}
	
	@GetMapping("/products/{id}")
	public ProductResponse getById(@PathVariable Long id) {
		return productService.getById(id);
	}
	
	@PutMapping("/products/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ProductResponse updateById(@PathVariable Long id, @RequestBody ProductRequest productRequest) {
		return productService.update(id, productRequest);
	}
	
	@DeleteMapping("/products/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> deleteById(@PathVariable Long id) {
		productService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("products/search")
	public Page<ProductResponse> searchProducts(@RequestParam String keyword, @RequestParam(defaultValue = "0")int page, @RequestParam(defaultValue = "10")int size){
		return productService.searchProducts(keyword, page, size);
	}
}
