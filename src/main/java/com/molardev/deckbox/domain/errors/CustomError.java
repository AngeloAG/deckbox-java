package com.molardev.deckbox.domain.errors;

import io.vavr.collection.Seq;

public sealed interface CustomError {
  record ValidationError(Seq<String> errors) implements CustomError {}
  record RepositoryError(String msg, Exception exception) implements CustomError {}
  record RehydrationError(Seq<String> errors) implements CustomError {}
}
