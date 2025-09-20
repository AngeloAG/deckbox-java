package com.molardev.deckbox.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.molardev.deckbox.infrastructure.persistence.entity.CardEntity;

public interface CardJpaRepository extends JpaRepository<CardEntity, String> {}
