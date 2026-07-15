package com.shopping.ecommerce.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.shopping.ecommerce.dto.ProductRequest;
import com.shopping.ecommerce.dto.ProductResponse;

public interface ProductService {
	ProductResponse create(ProductRequest request);
	
	ProductResponse getById(Long id);
	
	Page<ProductResponse> getAll(int page, int size, String sort,String direction);
	
	ProductResponse update(Long id, ProductRequest request);
	
	void delete(Long id);
	
	Page<ProductResponse> searchProducts(
	        String keyword,
	        int page,
	        int size);
}
