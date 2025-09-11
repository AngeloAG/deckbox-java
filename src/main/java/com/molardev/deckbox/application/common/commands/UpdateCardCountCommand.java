package com.molardev.deckbox.application.common.commands;

import java.util.UUID;

public record UpdateCardCountCommand(UUID deckId, String cardId, int count) {}
