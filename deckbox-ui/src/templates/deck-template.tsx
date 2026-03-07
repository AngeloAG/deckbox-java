import type { CardEntry } from "./card-entry-template";

export interface Deck {
  id: string;
  name: string;
  cardEntries: CardEntry[];
}
