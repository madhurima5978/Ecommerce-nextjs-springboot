package com.shopping.ecommerce.service;

import java.util.List;

import org.springframework.security.core.Authentication;

import com.shopping.ecommerce.dto.AddToCartRequest;
import com.shopping.ecommerce.dto.CartItemResponse;

public interface CartService {
	CartItemResponse addToCart(AddToCartRequest request,Authentication authentication);

//	List<CartItemResponse> getCart();
//
//	CartItemResponse updateQuantity(
//	        Long cartItemId,
//	        Integer quantity);
//
//	void removeItem(Long cartItemId);
//
//	void clearCart();
}
