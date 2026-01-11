package com.molardev.deckbox.infrastructure.persistence.jpa;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.molardev.deckbox.infrastructure.persistence.entity.FormatEntity;

public interface FormatJpaRepository extends JpaRepository<FormatEntity, UUID> {}
