package com.molardev.deckbox.domain.valueobject;

import io.vavr.collection.Seq;
import io.vavr.control.Validation;
import java.util.Objects;
import java.net.URI;

public class CardImageUrl {
    private final String value;

    private CardImageUrl(String value) {
        this.value = value;
    }

    public static Validation<Seq<String>, CardImageUrl> create(String value) {
        io.vavr.collection.List<String> errors = io.vavr.collection.List.empty();
            if (value == null || value.trim().isEmpty()) {
                errors = errors.append("Image URL must not be empty");
            } else {
                try {
                    URI uri = URI.create(value);
                    if (uri.getScheme() == null || (!uri.getScheme().equals("http") && !uri.getScheme().equals("https"))) {
                        errors = errors.append("Image URL must have http or https scheme");
                    }
                } catch (IllegalArgumentException e) {
                    errors = errors.append("Image URL is not a valid URL");
                }
            }
        return errors.isEmpty() ? Validation.valid(new CardImageUrl(value)) : Validation.invalid(errors);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardImageUrl that = (CardImageUrl) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
