package com.shopping.ecommerce.service;

import java.util.List;

import org.springframework.security.core.Authentication;

import com.shopping.ecommerce.dto.OrderResponse;

public interface OrderService {

	OrderResponse checkout(Authentication authentication);
	List<OrderResponse> orderHistory(Authentication authentication);
	OrderResponse getOrderById(long id,Authentication authentication);
}
