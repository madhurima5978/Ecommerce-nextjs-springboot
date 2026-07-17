package com.shopping.ecommerce.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.shopping.ecommerce.dto.AddToCartRequest;
import com.shopping.ecommerce.dto.CartItemResponse;
import com.shopping.ecommerce.entity.Cart;
import com.shopping.ecommerce.entity.CartItem;
import com.shopping.ecommerce.entity.Product;
import com.shopping.ecommerce.entity.User;
import com.shopping.ecommerce.repository.CartItemRepository;
import com.shopping.ecommerce.repository.CartRepository;
import com.shopping.ecommerce.repository.ProductRepository;
import com.shopping.ecommerce.repository.UserRepository;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	public UserRepository userRepository;
	
	@Autowired
	public CartRepository cartRepository;
	
	@Autowired
	public ProductRepository productRepository;
	
	@Autowired 
	public CartItemRepository cartItemRepository;
	
	@Transactional
	public CartItemResponse addToCart(AddToCartRequest request,Authentication authentication) {
		
		if(request.getQuantity() <= 0){

		    throw new ResponseStatusException(
		            HttpStatus.BAD_REQUEST,
		            "Quantity must be greater than zero");

		}
		String email = authentication.getName();
		User user = userRepository.findByEmail(email)
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found"));
		Cart cart = cartRepository.findByUser(user).orElse(null);
		if(cart == null)
		{
			cart = new Cart();
			
			cart.setUser(user);
			cart = cartRepository.save(cart);
		}
		Product product = productRepository.findById(request.getProductId())
							.orElseThrow(() -> 
							 new ResponseStatusException(
									 HttpStatus.NOT_FOUND,
									 "product not found"));
		
		CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product)
							.orElse(null);
		
		if(cartItem!=null)
		{
			if(product.getStock()<(cartItem.getQuantity()+request.getQuantity())) {
				throw new ResponseStatusException(
						HttpStatus.BAD_REQUEST,
						 "Insufficient quantity");
			}
			
				cartItem.setQuantity(cartItem.getQuantity()+request.getQuantity());
			
		}
		else {
			if(product.getStock()<(request.getQuantity())) {
				throw new ResponseStatusException(
						HttpStatus.BAD_REQUEST,
						 "Insufficient quantity");
			}
			cartItem = new CartItem();
			cartItem.setCart(cart);
			cartItem.setProduct(product);
			cartItem.setQuantity(request.getQuantity());
		}
		CartItem saved = cartItemRepository.save(cartItem);
		CartItemResponse response = mapToResponse(saved);
		return response;
	}
	public CartItemResponse mapToResponse(CartItem cartItem)
	{
		CartItemResponse cartItemResponse = new CartItemResponse();
		cartItemResponse.setId(cartItem.getId());
		cartItemResponse.setPrice(cartItem.getProduct().getPrice());
		cartItemResponse.setProductId(cartItem.getProduct().getId());
		cartItemResponse.setProductName(cartItem.getProduct().getName());
		cartItemResponse.setQuantity(cartItem.getQuantity());
		cartItemResponse.setTotal(cartItem.getProduct()
		        .getPrice()
		        .multiply(BigDecimal.valueOf(cartItem.getQuantity())));
		return cartItemResponse;
	}

	@Transactional
	public List<CartItemResponse> getCart(Authentication authentication){
		List<CartItem> cartItems = new ArrayList<>();  
		String email = authentication.getName();
		User user = userRepository.findByEmail(email)
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found"));
		Cart cart = cartRepository.findByUser(user).orElse(null);
		if(cart!=null)
		{
			cartItems = cartItemRepository.findByCart(cart);
		}
		List<CartItemResponse> cartItemResponses = new ArrayList<>();
		for(CartItem cartitem : cartItems) {
			CartItemResponse cartItemResponse = mapToResponse(cartitem);
			cartItemResponses.add(cartItemResponse);
			
		}
		return cartItemResponses;
	}
	@Transactional
	public CartItemResponse updateQuantity(
	        Long cartItemId,
	        Integer quantity,
	        Authentication authentication)
	{
	    if (quantity <= 0) {

	        throw new ResponseStatusException(
	                HttpStatus.BAD_REQUEST,
	                "Quantity must be greater than zero");
	    }
		String email = authentication.getName();
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found"));
		
		   CartItem cartItem = cartItemRepository.findById(cartItemId)
		            .orElseThrow(() -> new ResponseStatusException(
		                            HttpStatus.NOT_FOUND,
		                            "Cart item not found"));
		if (!cartItem.getCart()
		            .getUser()
		            .getId()
		            .equals(user.getId())) {

		        throw new ResponseStatusException(
		                HttpStatus.FORBIDDEN,
		                "You cannot modify another user's cart");
		}
		CartItem response = new CartItem();
		
			if (quantity > cartItem.getProduct().getStock()) {

			    throw new ResponseStatusException(
			            HttpStatus.CONFLICT,
			            "Insufficient stock");
			}
			
				cartItem.setQuantity(quantity);
				response=cartItemRepository.save(cartItem);
				
		return mapToResponse(response);
		
	}
	@Transactional
	public void removeItem(Long cartItemId,Authentication authentication)
	{
		String email = authentication.getName();
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found"));
		
		   CartItem cartItem = cartItemRepository.findById(cartItemId)
		            .orElseThrow(() -> new ResponseStatusException(
		                            HttpStatus.NOT_FOUND,
		                            "Cart item not found"));
		   if (!cartItem.getCart()
		            .getUser()
		            .getId()
		            .equals(user.getId())) {

		        throw new ResponseStatusException(
		                HttpStatus.FORBIDDEN,
		                "You cannot modify another user's cart");
		}
		   cartItemRepository.delete(cartItem);
		   
	}
	@Transactional
	public void clearCart(Authentication authentication) {
		String email = authentication.getName();
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found"));
		Cart cart = cartRepository.findByUser(user)
		        .orElseThrow(() ->
		                new ResponseStatusException(
		                        HttpStatus.NOT_FOUND,
		                        "Cart not found"));
		cartItemRepository.deleteByCart(cart);
		
	}
}
