package com.molardev.deckbox.infrastructure.persistence.jpa;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.molardev.deckbox.infrastructure.persistence.entity.UserEntity;

public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {
  Optional<UserEntity> findByEmail(String email);
}
