package com.shopping.ecommerce.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.shopping.ecommerce.dto.CategoryRequest;
import com.shopping.ecommerce.dto.CategoryResponse;
import com.shopping.ecommerce.entity.Category;
import com.shopping.ecommerce.repository.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService{
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	
	@Override
	public CategoryResponse create(CategoryRequest request) {
		Category category = new Category();
		category.setName(request.getName());
		category.setDescription(request.getDescription());
		Category saved = categoryRepository.save(category);
		CategoryResponse response = mapToResponse(saved);
		return response;
	}

	@Override
	public List<CategoryResponse> getAll(){
		List<Category> categories = categoryRepository.findAll();
		List<CategoryResponse> responses = new ArrayList<>();
		for(Category c : categories)
		{
			CategoryResponse response = mapToResponse(c);
			responses.add(response);
		}
		return responses;
	}

	@Override
	public CategoryResponse getById(Long id) {
		Category category = categoryRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"category not found"));
		CategoryResponse response = mapToResponse(category);
		return response;
	}

	@Override
	public CategoryResponse update(Long id,
	                        CategoryRequest request) {
		Category category = categoryRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"category not found"));
		category.setName(request.getName());
		category.setDescription(request.getDescription());
		Category saved = categoryRepository.save(category);
		CategoryResponse response = mapToResponse(saved);
		return response;
	}

	@Override
	public void delete(Long id) {
		Category category = categoryRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"category not found"));
		categoryRepository.delete(category);
	}
	
	
	private CategoryResponse mapToResponse(Category category){

	    CategoryResponse response = new CategoryResponse();

	    response.setId(category.getId());
	    response.setName(category.getName());
	    response.setDescription(category.getDescription());
	    
	    return response;
	}
}
