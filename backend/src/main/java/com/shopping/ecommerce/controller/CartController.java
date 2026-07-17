package com.shopping.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
	
	@GetMapping("/get")
	public List<CartItemResponse> getCart(Authentication authentication) {
		return cartService.getCart(authentication);
	}
	
	@PutMapping("/item/{id}")
	public CartItemResponse updateCart(@PathVariable long id, @RequestBody int quantity, Authentication authentication)
	{
		return cartService.updateQuantity(id, quantity,authentication);
	}
	@DeleteMapping("/item/{id}")
	public ResponseEntity<Void> deleteItem(
	        @PathVariable Long id,
	        Authentication authentication) {

	    cartService.removeItem(id, authentication);

	    return ResponseEntity.noContent().build();
	}
	@DeleteMapping("clear")
	public ResponseEntity<Void> clearCart(
	        Authentication authentication) {

	    cartService.clearCart (authentication);

	    return ResponseEntity.noContent().build();
	}
	
}
