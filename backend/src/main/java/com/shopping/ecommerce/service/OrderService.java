package com.shopping.ecommerce.service;

import org.springframework.security.core.Authentication;

import com.shopping.ecommerce.dto.OrderResponse;

public interface OrderService {

	OrderResponse checkout(Authentication authentication);
}
