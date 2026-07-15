package com.shopping.ecommerce.service;

import com.shopping.ecommerce.dto.ProductRequest;
import com.shopping.ecommerce.dto.ProductResponse;
import com.shopping.ecommerce.entity.Category;
import com.shopping.ecommerce.entity.Product;
import com.shopping.ecommerce.repository.CategoryRepository;
import com.shopping.ecommerce.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	
	@Override
	public ProductResponse create(ProductRequest request) {
		Product product = new Product();
		product.setName(request.getName());
		product.setDescription(request.getDescription());
		product.setPrice(request.getPrice());
		product.setStock(request.getStock());
		product.setImageUrl(request.getImageUrl());
		Category category =  categoryRepository.findById(request.getCategoryId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"category not found"));
		product.setCategory(category);
		Product saved = productRepository.save(product);
		ProductResponse response = new ProductResponse();
		response = mapToResponse(saved);
		
		return response;
	}
	
	@Override
	public Page<ProductResponse> getAll(int page, int size, String sort, String direction)
	{
		Sort sortdirection = direction.equalsIgnoreCase("desc")
					? Sort.by(sort).descending()
					: Sort.by(sort).ascending();
		PageRequest pageable = PageRequest.of(page,size,Sort.by(sort));
		Page<Product> productPage = productRepository.findAll(pageable);
		
		List<ProductResponse> responses = new ArrayList<>();
		
		for(Product product : productPage.getContent())
		{
			responses.add(mapToResponse(product));
		}
		return new PageImpl<>(responses, pageable, productPage.getTotalElements());
	}
	
	
	
	@Override
	public ProductResponse getById(Long id) {
		Product product = productRepository.findById(id)
							.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"product not found"));
		ProductResponse response = new ProductResponse();
		
		response = mapToResponse(product);
		return response;
	}
	
	@Override
	public ProductResponse update(Long id, ProductRequest request)
	{
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"product not found"));
		product.setName(request.getName());
		product.setDescription(request.getDescription());
		product.setPrice(request.getPrice());
		product.setStock(request.getStock());
		product.setImageUrl(request.getImageUrl());
		Category category = categoryRepository.findById(request.getCategoryId())
		        .orElseThrow(() -> new ResponseStatusException(
		                HttpStatus.NOT_FOUND,
		                "Category not found"));

		product.setCategory(category);
		Product saved = productRepository.save(product);
		ProductResponse response = mapToResponse(saved);
		return response;
	}
	@Override
	public void delete(Long id)
	{
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"product not found"));
		productRepository.delete(product);
	}
	private ProductResponse mapToResponse(Product product){

	    ProductResponse response = new ProductResponse();

	    response.setId(product.getId());
	    response.setName(product.getName());
	    response.setDescription(product.getDescription());
	    response.setPrice(product.getPrice());
	    response.setStock(product.getStock());
	    response.setImageUrl(product.getImageUrl());
	    response.setCategoryId(product.getCategory().getId());
	    response.setCategoryName(product.getCategory().getName());
	    return response;
	}
	public Page<ProductResponse> searchProducts(
	        String keyword,
	        int page,
	        int size){
		Pageable pageable = PageRequest.of(page, size);
		
		Page<Product> products = productRepository.findByNameContainingIgnoreCase(keyword, pageable);
		return products.map(this::mapToResponse);
	}
}
