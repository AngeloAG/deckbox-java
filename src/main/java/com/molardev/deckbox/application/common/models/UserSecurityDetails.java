package com.molardev.deckbox.application.common.models;

import java.util.UUID;

public record UserSecurityDetails(UUID id, String email, String hashPassword) {
  
}
