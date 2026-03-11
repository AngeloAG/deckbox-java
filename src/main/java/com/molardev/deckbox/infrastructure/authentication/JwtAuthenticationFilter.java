package com.molardev.deckbox.infrastructure.authentication;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.molardev.deckbox.application.common.interfaces.ITokenService;

import io.vavr.control.Option;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private final ITokenService tokenService;

  public JwtAuthenticationFilter(ITokenService tokenService) {
    this.tokenService = tokenService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException  {
    String header = request.getHeader("Authorization");
    if(header == null || !header.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    String token = header.substring(7);
    Option<String> userId = tokenService.getUserIdFromToken(token);
    userId.forEach(id -> {
      UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(id, null, List.of());
      SecurityContextHolder.getContext().setAuthentication(auth);
    });

    filterChain.doFilter(request, response);
  }
}
