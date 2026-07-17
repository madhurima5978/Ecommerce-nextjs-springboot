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

import com.shopping.ecommerce.dto.OrderItemResponse;
import com.shopping.ecommerce.dto.OrderResponse;
import com.shopping.ecommerce.entity.Cart;
import com.shopping.ecommerce.entity.CartItem;
import com.shopping.ecommerce.entity.Order;
import com.shopping.ecommerce.entity.OrderItem;
import com.shopping.ecommerce.entity.Product;
import com.shopping.ecommerce.entity.User;
import com.shopping.ecommerce.enums.OrderStatus;
import com.shopping.ecommerce.repository.CartItemRepository;
import com.shopping.ecommerce.repository.CartRepository;
import com.shopping.ecommerce.repository.OrderItemRepository;
import com.shopping.ecommerce.repository.OrderRepository;
import com.shopping.ecommerce.repository.ProductRepository;
import com.shopping.ecommerce.repository.UserRepository;

@Service
public class OrderServiceImpl implements OrderService{

	@Autowired
	public UserRepository userRepository;
	
	@Autowired
	public CartRepository cartRepository;
	
	@Autowired
	public ProductRepository productRepository;
	
	@Autowired 
	public CartItemRepository cartItemRepository;
	
	@Autowired
	public OrderRepository orderRepository;
	
	@Autowired
	public OrderItemRepository orderItemRepository;
	
	@Override
	@Transactional
	public OrderResponse checkout(Authentication authentication) {
		String email = authentication.getName();
		User user = userRepository.findByEmail(email)
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found"));
		Cart cart = cartRepository.findByUser(user)
		        .orElseThrow(() ->
		                new ResponseStatusException(
		                        HttpStatus.NOT_FOUND,
		                        "Cart not found"));
		List<CartItem> cartItems = cartItemRepository.findByCart(cart);
		if (cartItems.isEmpty()) {
		    throw new ResponseStatusException(
		            HttpStatus.BAD_REQUEST,
		            "Cart is empty");
		}
		BigDecimal total = BigDecimal.ZERO;
		
		for(CartItem cartItem : cartItems)
		{
			BigDecimal subtot = (cartItem.getProduct().getPrice()).multiply(BigDecimal.valueOf(cartItem.getQuantity()));
			total = total.add(subtot);
			if(cartItem.getQuantity()
			        > cartItem.getProduct().getStock()){

			    throw new ResponseStatusException(
			            HttpStatus.CONFLICT,
			            "Insufficient stock");
			}
		}
		Order order = new Order();
		order.setStatus(OrderStatus.PENDING);
		order.setUser(user);
		order.setTotalAmount(total);
		order = orderRepository.save(order);
		
		for(CartItem cartItem : cartItems){

		    OrderItem orderItem = new OrderItem();

		    orderItem.setOrder(order);

		    orderItem.setProduct(cartItem.getProduct());

		    orderItem.setQuantity(cartItem.getQuantity());

		    orderItem.setPriceAtPurchase(
		            cartItem.getProduct().getPrice());

		    orderItemRepository.save(orderItem);

		    Product product = cartItem.getProduct();

		    product.setStock(
		            product.getStock()
		            - cartItem.getQuantity());

		    productRepository.save(product);
		    
		}
		cartItemRepository.deleteByCart(cart);
		return mapToResponse(order);

	}
	
	@Override
	@Transactional
	public List<OrderResponse> orderHistory(Authentication authentication)
	{
		String email = authentication.getName();
		User user = userRepository.findByEmail(email)
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found"));
		List<Order> orders = orderRepository.findByUser(user);
		List<OrderResponse> orderResponses = new ArrayList<>();
		for(Order order : orders)
		{
			OrderResponse orderResponse = mapToResponse(order);
			orderResponses.add(orderResponse);
		}
		return orderResponses;
	}
	
	@Override
	@Transactional
	public OrderResponse getOrderById(long id,Authentication authentication) {
		String email = authentication.getName();
		User user = userRepository.findByEmail(email)
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found"));
		Order order = orderRepository.findById(id)
						.orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND,"Order not found"));
		return mapToResponse(order);
	}
	
	private OrderResponse mapToResponse(Order order) {

	    OrderResponse response = new OrderResponse();

	    response.setId(order.getId());
	    response.setTotalAmount(order.getTotalAmount());
	    response.setStatus(order.getStatus());
	    response.setCreatedAt(order.getCreatedAt());

	    List<OrderItem> orderItems = orderItemRepository.findByOrder(order);

	    List<OrderItemResponse> itemResponses = new ArrayList<>();

	    for (OrderItem orderItem : orderItems) {

	        OrderItemResponse itemResponse = new OrderItemResponse();

	        itemResponse.setProductName(
	                orderItem.getProduct().getName());

	        itemResponse.setQuantity(
	                orderItem.getQuantity());

	        itemResponse.setPriceAtPurchase(
	                orderItem.getPriceAtPurchase());

	        itemResponse.setSubtotal(
	                orderItem.getPriceAtPurchase()
	                        .multiply(
	                                BigDecimal.valueOf(
	                                        orderItem.getQuantity())));

	        itemResponses.add(itemResponse);
	    }

	    response.setOrderitemResponses(itemResponses);

	    return response;
	}
	
}
