package com.shopping.ecommerce.service;

import java.util.List;

import org.springframework.security.core.Authentication;

import com.shopping.ecommerce.dto.AddToCartRequest;
import com.shopping.ecommerce.dto.CartItemResponse;

public interface CartService {
	CartItemResponse addToCart(AddToCartRequest request,Authentication authentication);

	List<CartItemResponse> getCart(Authentication authentication);

	CartItemResponse updateQuantity(
	        Long cartItemId,
	        Integer quantity,
	        Authentication authentication);

	void removeItem(Long cartItemId,Authentication authentication);

	void clearCart(Authentication authentication);
}
