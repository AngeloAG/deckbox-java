package com.molardev.deckbox.infrastructure.persistence.jpa;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.molardev.deckbox.infrastructure.persistence.entity.FormatEntity;

public interface FormatJpaRepository extends JpaRepository<FormatEntity, UUID> {
  @Query("SELECT f FROM FormatEntity f LEFT JOIN FETCH f.rules WHERE f.id = :id")
	public Optional<FormatEntity> findByIdWithRules(@Param("id") UUID id);
}
