package com.molardev.deckbox.infrastructure.persistence.jpa;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.molardev.deckbox.infrastructure.persistence.entity.RuleEntity;

public interface RuleJpaRepository extends JpaRepository<RuleEntity, UUID> {}
