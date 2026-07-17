package com.shopping.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shopping.ecommerce.entity.Order;
import com.shopping.ecommerce.entity.User;

public interface OrderRepository extends JpaRepository<Order, Long> {

	List<Order> findByUser(User user);
}
