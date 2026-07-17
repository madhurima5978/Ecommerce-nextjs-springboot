package com.shopping.ecommerce.dto;

import java.math.BigDecimal;

import com.shopping.ecommerce.enums.OrderStatus;

public class CheckoutResponse {

	private Long orderId;

	private BigDecimal totalAmount;

	private OrderStatus status;

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
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
}
