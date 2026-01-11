package com.molardev.deckbox.infrastructure.controllers.dtos;

public class CreateFormatRequest {
  private String name;
  private String description;

  public CreateFormatRequest() {}

  public CreateFormatRequest(String name, String description) {
    this.name = name;
    this.description = description;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }
}
