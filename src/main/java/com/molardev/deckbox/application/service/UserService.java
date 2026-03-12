package com.molardev.deckbox.application.service;

import org.springframework.stereotype.Service;

import com.molardev.deckbox.application.common.interfaces.IIdentityRepository;
import com.molardev.deckbox.application.common.interfaces.IPasswordEncoder;
import com.molardev.deckbox.application.common.interfaces.ITokenService;
import com.molardev.deckbox.application.common.interfaces.IUserRepository;
import com.molardev.deckbox.domain.entity.User;
import com.molardev.deckbox.domain.errors.CustomError;
import io.vavr.control.Either;
import io.vavr.control.Option;

@Service
public class UserService {

  private final IPasswordEncoder passwordEncoder;
  private final IUserRepository userRepository;
  private final IIdentityRepository iIdentityRepository;
  private final ITokenService tokenService;

  public UserService(IPasswordEncoder passwordEncoder, IUserRepository userRepository,
      IIdentityRepository iIdentityRepository, ITokenService tokenService) {
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
    this.iIdentityRepository = iIdentityRepository;
    this.tokenService = tokenService;
  }

  public Either<CustomError, Void> registerUserWithEmailAndPassword(String email, String password) {
    return userRepository.findByEmail(email)
        .flatMap(userOption -> userOption
            .map(user -> Either.<CustomError, Void>left((CustomError) new CustomError.ConflictError()))
            .getOrElse(() -> User.create(email, io.vavr.collection.List.empty())
                .toEither()
                .mapLeft(errors -> (CustomError) new CustomError.ValidationError(errors))
                .flatMap(createdUser -> userRepository.save(createdUser, passwordEncoder.hash(password))
                    .map(user -> null))));
  }

  public Either<CustomError, Option<String>> loginUserWithEmailAndPassword(String email, String password) {
    return iIdentityRepository.getSecurityDetailsByEmail(email)
        .map(userOption -> {
          return userOption.flatMap(userDetails -> {
            if (!passwordEncoder.matches(password, userDetails.hashPassword())) {
              return Option.none();
            }
            return Option.some(tokenService.generateToken(userDetails.id().toString()));
          });
        });
  }
}
