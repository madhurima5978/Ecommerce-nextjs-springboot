package com.shopping.ecommerce.service;

import java.util.List;

import com.shopping.ecommerce.dto.CategoryRequest;
import com.shopping.ecommerce.dto.CategoryResponse;

public interface CategoryService {
	CategoryResponse create(CategoryRequest request);

	List<CategoryResponse> getAll();

	CategoryResponse getById(Long id);

	CategoryResponse update(Long id,
	                        CategoryRequest request);

	void delete(Long id);
}
