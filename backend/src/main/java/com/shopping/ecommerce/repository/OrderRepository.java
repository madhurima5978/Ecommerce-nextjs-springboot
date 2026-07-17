package com.shopping.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shopping.ecommerce.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
