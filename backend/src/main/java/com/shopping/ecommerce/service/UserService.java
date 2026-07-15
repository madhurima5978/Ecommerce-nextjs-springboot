package com.shopping.ecommerce.service;

import com.shopping.ecommerce.dto.LoginRequest;
import com.shopping.ecommerce.dto.LoginResponse;
import com.shopping.ecommerce.dto.RegisterRequest;

public interface UserService {
	void register(RegisterRequest request);
	LoginResponse login(LoginRequest request);
}
