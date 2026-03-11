package com.molardev.deckbox.infrastructure.controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.molardev.deckbox.application.service.UserService;
import com.molardev.deckbox.domain.errors.CustomError;
import com.molardev.deckbox.infrastructure.controllers.dtos.LoginRequest;
import com.molardev.deckbox.infrastructure.controllers.dtos.LoginResponse;
import com.molardev.deckbox.infrastructure.controllers.dtos.RegisterRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("api/auth")
public class AuthController {
  private final UserService userService;

  public AuthController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/register")
  public ResponseEntity<Object> register(@RequestBody RegisterRequest request) {
      return userService.registerUserWithEmailAndPassword(request.email(), request.password())
        .fold(error -> mapErrors(error), 
              user -> ResponseEntity.status(HttpStatus.CREATED).build());
  }

  @PostMapping("login")
  public ResponseEntity<Object> login(@RequestBody LoginRequest request) {
      return userService.loginUserWithEmailAndPassword(request.email(), request.password())
        .fold(
          errors-> mapErrors(errors), 
          tokenOption -> tokenOption.isEmpty() 
            ? ResponseEntity.notFound().build()
            : ResponseEntity.ok().body(new LoginResponse(tokenOption.get()))
        );
  }
  
  
  private ResponseEntity<Object> mapErrors(CustomError error) {
    return switch (error) {
      case CustomError.ValidationError(var errors) -> ResponseEntity.badRequest().body(Map.of("error", errors.asJava()));
      case CustomError.RepositoryError(var message, var e) -> 
            ResponseEntity.internalServerError().body(Map.of("error", "An error occurred. Try again later"));
      case CustomError.RehydrationError(var errors) -> 
            ResponseEntity.internalServerError().body(Map.of("error", "An error occurred. Try again later"));
      case CustomError.NotFoundError(var message) -> ResponseEntity.notFound().build();
      case CustomError.ConflictError() -> ResponseEntity.status(HttpStatus.CONFLICT).build();
    };
  }
}
