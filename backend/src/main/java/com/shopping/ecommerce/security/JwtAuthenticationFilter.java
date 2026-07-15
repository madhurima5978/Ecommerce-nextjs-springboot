package com.shopping.ecommerce.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{
	
	private final JwtService jwtService;
	private final CustomUserDetailsService userDetailsService;
	public JwtAuthenticationFilter(JwtService jwtService,
            CustomUserDetailsService userDetailsService) {
			this.jwtService = jwtService;
			this.userDetailsService = userDetailsService;
			}
	
	@Override
	protected void doFilterInternal(
			@NonNull HttpServletRequest request,
			@NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain)
			throws ServletException, IOException{
		System.out.println("1");

		String authHeader = request.getHeader("Authorization");

		System.out.println("2");

		System.out.println(authHeader);

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
		    System.out.println("3");
		    filterChain.doFilter(request, response);
		    return;
		}

		System.out.println("4");

		String jwt = authHeader.substring(7);

		System.out.println("5");
		String username = jwtService.extractUsername(jwt);
		if (username != null &&
			    SecurityContextHolder.getContext().getAuthentication() == null) {
			
			UserDetails userDetails =
			        userDetailsService.loadUserByUsername(username);
			System.out.println(userDetails.getAuthorities());
			if (jwtService.isTokenValid(jwt, userDetails)) {

			    UsernamePasswordAuthenticationToken authToken =
			            new UsernamePasswordAuthenticationToken(
			                    userDetails,
			                    null,
			                    userDetails.getAuthorities()
			            );

			    authToken.setDetails(
			            new WebAuthenticationDetailsSource()
			                    .buildDetails(request));

			    SecurityContextHolder.getContext()
			            .setAuthentication(authToken);
			    
			}
		}
		filterChain.doFilter(request, response);
		
	}
	
}
