package com.shopping.ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.shopping.ecommerce.dto.LoginRequest;
import com.shopping.ecommerce.dto.LoginResponse;
import com.shopping.ecommerce.dto.RegisterRequest;
import com.shopping.ecommerce.entity.User;
import com.shopping.ecommerce.repository.UserRepository;
import com.shopping.ecommerce.security.JwtService;
import com.shopping.ecommerce.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired 
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtService jwtService;
    
    @Override
    public void register(RegisterRequest request) {

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));   // We'll encrypt this next
        user.setRole("CUSTOMER");
        user.setVerified(false);

        userRepository.save(user);
    }
    
    @Override
    public LoginResponse login(LoginRequest request) {
    	
    	User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found"));
    	
    	if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
    		throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid password");
    	}
    	String token = jwtService.generateToken(user);
    	return new LoginResponse(token, user.getEmail(), user.getRole());
    }

}