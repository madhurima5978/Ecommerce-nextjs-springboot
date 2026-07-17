package com.shopping.ecommerce.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.shopping.ecommerce.enums.OrderStatus;


public class OrderResponse {

	private Long id;
	
	private BigDecimal totalAmount;
	
	private OrderStatus status;
	
	private LocalDateTime createdAt;

	private List<OrderItemResponse> orderitemResponses;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public List<OrderItemResponse> getOrderitemResponses() {
		return orderitemResponses;
	}

	public void setOrderitemResponses(List<OrderItemResponse> orderitemResponses) {
		this.orderitemResponses = orderitemResponses;
	}
}
