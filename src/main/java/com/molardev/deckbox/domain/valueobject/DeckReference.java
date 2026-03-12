package com.molardev.deckbox.domain.valueobject;

import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;
import java.util.Objects;
import java.util.UUID;

public class DeckReference {
  private final DeckName name;
  private final UUID id;
  private final UUID ownerId;

  private DeckReference(DeckName name, UUID id, UUID ownerId) {
    this.name = name;
    this.id = id;
    this.ownerId = ownerId;
  }

  public static Validation<Seq<String>, DeckReference> create(DeckName name, UUID id, UUID ownerId) {
    Seq<String> errors = List.of();
    if (name == null) {
      errors = errors.append("Deck name must not be null");
    }
    if (id == null) {
      errors = errors.append("Deck ID must not be null");
    }
    if (ownerId == null) {
      errors = errors.append("The owner ID must not be null");
    }

    return Validation.valid(new DeckReference(name, id, ownerId));
  }

  // Overloaded create method that generates a UUID as a String if id is not
  // provided
  public static Validation<Seq<String>, DeckReference> create(DeckName name, UUID ownerId) {
    // Use UUID as a String for id
    UUID generatedId = UUID.randomUUID();
    return create(name, generatedId, ownerId);
  }

  public DeckName getName() {
    return name;
  }

  public UUID getId() {
    return id;
  }

  public UUID getOwnerId() {
    return ownerId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    DeckReference deckName = (DeckReference) o;
    return name.equals(deckName.name) && id.equals(deckName.id) && ownerId.equals(deckName.ownerId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, id, ownerId);
  }

  @Override
  public String toString() {
    return "DeckReference{" +
        "name='" + name + '\'' +
        ", id=" + id +
        ", ownerId=" + ownerId +
        '}';
  }
}
