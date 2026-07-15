package com.shopping.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shopping.ecommerce.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}