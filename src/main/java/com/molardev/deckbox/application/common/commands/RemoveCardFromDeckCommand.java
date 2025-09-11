package com.molardev.deckbox.application.common.commands;

import java.util.UUID;

public record RemoveCardFromDeckCommand(UUID deckId, String cardId) {}
