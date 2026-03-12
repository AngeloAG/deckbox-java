package com.molardev.deckbox.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "decks")
public class DeckEntity {
  @Id
  private UUID id;
  private String name;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "deck_id")
  private List<CardEntryEntity> cardEntries = new ArrayList<>();

  @ManyToOne
  @JoinColumn(name = "user_id")
  private UserEntity owner;

  public DeckEntity() {
  }

  public DeckEntity(UUID id, String name, UserEntity owner) {
    this.id = id;
    this.name = name;
    this.owner = owner;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<CardEntryEntity> getCardEntries() {
    return cardEntries;
  }

  public void setCardEntries(List<CardEntryEntity> cardEntries) {
    this.cardEntries = cardEntries;
  }

  public UserEntity getOwner() {
    return owner;
  }

  public void setOwner(UserEntity owner) {
    this.owner = owner;
  }

}
