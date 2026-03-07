import { useEffect, useState } from 'react'
import './App.css'
import type { Deck } from './templates/deck-template';
import { DeckCard } from './components/deck';

function App() {
  const [decks, setDecks] = useState([] as Deck[]);

  useEffect(() => {
    fetch(`${import.meta.env.VITE_API_URL}/decks`)
      .then((res) => res.json())
      .then((data) => setDecks(data as Deck[]));
  }, [])

  return (
    <>
      {decks.map(deck => <DeckCard deck={deck} onClick={() => {}} />)}
    </>
  )
}

export default App
