package com.shopping.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopping.ecommerce.dto.AddToCartRequest;
import com.shopping.ecommerce.dto.CartItemResponse;
import com.shopping.ecommerce.service.CartService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/cart")
@SecurityRequirement(name = "Bearer Authentication")
public class CartController {

	@Autowired
	CartService cartService;
	@PostMapping("/add")
	public CartItemResponse addToCart(@RequestBody AddToCartRequest request, Authentication authentication) {
		return cartService.addToCart(request, authentication);
	}
}
