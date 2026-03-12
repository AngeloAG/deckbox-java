package com.molardev.deckbox.infrastructure.persistence;

import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import com.molardev.deckbox.application.common.interfaces.IIdentityRepository;
import com.molardev.deckbox.application.common.interfaces.IUserRepository;
import com.molardev.deckbox.application.common.models.UserSecurityDetails;
import com.molardev.deckbox.domain.entity.User;
import com.molardev.deckbox.domain.errors.CustomError;
import com.molardev.deckbox.infrastructure.persistence.entity.UserEntity;
import com.molardev.deckbox.infrastructure.persistence.jpa.UserJpaRepository;
import com.molardev.deckbox.infrastructure.persistence.translations.UserTranslator;

import io.vavr.control.Either;
import io.vavr.control.Option;

@Repository
public class JpaUserRepository implements IUserRepository, IIdentityRepository {
  private final UserJpaRepository userJpaRepository;

  public JpaUserRepository(UserJpaRepository userJpaRepository) {
    this.userJpaRepository = userJpaRepository;
  }

  @Override
  public Either<CustomError, User> save(User newUser, String hashPassword) {
    UserEntity userEntity = UserTranslator.toEntity(newUser, hashPassword);
    UserEntity savedUser = userJpaRepository.save(userEntity);
    return Either.right(UserTranslator.rehydrate(savedUser));
  }

  @Override
  public Either<CustomError, Option<User>> findByEmail(String email) {
    try {
      return userJpaRepository.findByEmail(email)
          .<Either<CustomError, Option<User>>>map(user -> Either.right(Option.some(UserTranslator.rehydrate(user))))
          .orElseGet(() -> Either.right(Option.none()));
    } catch (Exception e) {
      return Either.left((CustomError) new CustomError.RepositoryError(e.getMessage(), e));
    }
  }

  @Override
  public Either<CustomError, Option<UserSecurityDetails>> getSecurityDetailsByEmail(String email) {
    try {
      return userJpaRepository.findByEmail(email)
          .<Either<CustomError, Option<UserSecurityDetails>>>map(
              user -> Either.right(Option.some(UserTranslator.rehydrateSecurityDetails(user))))
          .orElseGet(() -> Either.right(Option.none()));
    } catch (Exception e) {
      return Either.left((CustomError) new CustomError.RepositoryError(e.getMessage(), e));
    }
  }

  @Override
  public Either<CustomError, Option<UUID>> getCurrentUserId() {
    SecurityContext context = SecurityContextHolder.getContext();
    Authentication authentication = context.getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {
      System.out.println("User Not Authenticated");
      return Either.right(Option.none());
    }

    String userId = authentication.getName();
    System.out.println("User Repo userId: " + userId);
    return Either.right(Option.some(UUID.fromString(userId)));
  }
}
