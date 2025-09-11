package com.molardev.deckbox.domain.valueobject;

import io.vavr.collection.Seq;
import io.vavr.control.Validation;
import java.util.Objects;
import java.util.UUID;

public class DeckReference {
    private final DeckName name;
		private final UUID id;

    private DeckReference(DeckName name, UUID id) {
        this.name = name;
        this.id = id;
    }

    public static Validation<Seq<String>, DeckReference> create(DeckName name, UUID id) {
        if (name == null) {
            return Validation.invalid(io.vavr.collection.List.of("Deck name must not be null"));
        }
        if (id == null) {
            return Validation.invalid(io.vavr.collection.List.of("Deck ID must not be null"));
        }

				return Validation.valid(new DeckReference(name, id));
    }

    // Overloaded create method that generates a UUID as a String if id is not provided
    public static Validation<Seq<String>, DeckReference> create(DeckName name) {
        // Use UUID as a String for id
        UUID generatedId = UUID.randomUUID();
        return create(name, generatedId);
    }
		
    public DeckName getName() {
        return name;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeckReference deckName = (DeckReference) o;
        return name.equals(deckName.name) && id.equals(deckName.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id);
    }

    @Override
    public String toString() {
        return "DeckReference{" +
								"name='" + name + '\'' +
								", id=" + id +
								'}';
    }
}
