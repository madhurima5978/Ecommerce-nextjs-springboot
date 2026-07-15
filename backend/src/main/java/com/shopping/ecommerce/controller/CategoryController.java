package com.shopping.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.shopping.ecommerce.dto.CategoryRequest;
import com.shopping.ecommerce.dto.CategoryResponse;
import com.shopping.ecommerce.service.CategoryService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
public class CategoryController {
	@Autowired
    private CategoryService categoryService;
	
	@GetMapping("/category")
	public List<CategoryResponse> getAll() {
		return categoryService.getAll();
	}
	
	@PostMapping("/category")
	@PreAuthorize("hasRole('ADMIN')")
	public CategoryResponse create(@RequestBody CategoryRequest categoryRequest) {
		
		return categoryService.create(categoryRequest);
	}
	
	@GetMapping("/category/{id}")
	public CategoryResponse getById(@PathVariable Long id) {
		return categoryService.getById(id);
	}
	
	@PutMapping("/category/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public CategoryResponse updateById(@PathVariable Long id, @RequestBody CategoryRequest categoryRequest) {
		return categoryService.update(id, categoryRequest);
	}
	
	@DeleteMapping("/category/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> deleteById(@PathVariable Long id) {
		categoryService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
