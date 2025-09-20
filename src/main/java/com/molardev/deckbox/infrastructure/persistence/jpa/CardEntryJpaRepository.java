package com.molardev.deckbox.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.molardev.deckbox.infrastructure.persistence.entity.CardEntryEntity;

public interface CardEntryJpaRepository extends JpaRepository<CardEntryEntity, Long> {}