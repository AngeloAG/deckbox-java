export interface Card {
  id: string;
  name: string;
  supertype: string;
  subtypes: string[];
  types: string[];
  legalities: string[];
  image: string;
}