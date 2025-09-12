package com.molardev.deckbox.infrastructure.persistence.jpa;

import com.molardev.deckbox.infrastructure.persistence.entity.DeckEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface DeckJpaRepository extends JpaRepository<DeckEntity, UUID> {
}