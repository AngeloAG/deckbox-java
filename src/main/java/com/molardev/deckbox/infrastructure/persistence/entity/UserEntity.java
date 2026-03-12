package com.molardev.deckbox.infrastructure.persistence.entity;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class UserEntity {
  @Id
  private UUID id;
  private String email;
  private String hash;

  public UserEntity() {
  }

  public UserEntity(UUID id, String email, String hash) {
    this.id = id;
    this.email = email;
    this.hash = hash;
  }

  public UUID getId() {
    return id;
  }

  public String getEmail() {
    return email;
  }

  public String getHash() {
    return hash;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setHash(String hash) {
    this.hash = hash;
  }
}
