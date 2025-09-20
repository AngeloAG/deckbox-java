package com.molardev.deckbox.infrastructure.persistence.jpa;

import com.molardev.deckbox.infrastructure.persistence.entity.DeckEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface DeckJpaRepository extends JpaRepository<DeckEntity, UUID> {

	@Query("SELECT d FROM DeckEntity d LEFT JOIN FETCH d.cardEntries WHERE d.id = :id")
	public Optional<DeckEntity> findByIdWithCardEntries(@Param("id") UUID id);
}