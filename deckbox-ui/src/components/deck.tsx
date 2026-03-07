import { useState } from "react";
import type { Deck } from "../templates/deck-template";


export function DeckCard({ deck, onClick }: {deck: Deck, onClick: (deck: Deck) => void}) {
  const [hovered, setHovered] = useState(false);

  return (
    <div
      onClick={() => onClick?.(deck)}
      onMouseEnter={() => setHovered(true)}
      onMouseLeave={() => setHovered(false)}
      style={{
        position: "relative",
        width: "180px",
        height: "240px",
        borderRadius: "16px",
        cursor: "pointer",
        flexShrink: 0,
        overflow: "hidden",
        transition:
          "transform 0.25s cubic-bezier(.22,.68,0,1.4), box-shadow 0.25s ease",
        transform: hovered
          ? "translateY(-8px) scale(1.03)"
          : "translateY(0) scale(1)",
        boxShadow: hovered
          ? "0 20px 40px rgba(0,0,0,0.55), 0 0 0 1px rgba(255,210,80,0.4)"
          : "0 6px 20px rgba(0,0,0,0.4)",
        background:
          "linear-gradient(160deg, #1a1a2e 0%, #16213e 60%, #0f3460 100%)",
        fontFamily: "'Cinzel', serif",
        userSelect: "none",
      }}>
      {/* Holographic shimmer layer */}
      <div
        style={{
          position: "absolute",
          inset: 0,
          opacity: hovered ? 0.18 : 0,
          transition: "opacity 0.3s ease",
          background:
            "linear-gradient(135deg, transparent 25%, rgba(255,230,100,0.6) 50%, transparent 75%)",
          backgroundSize: "200% 200%",
          animation: hovered ? "shimmer 1.4s linear infinite" : "none",
          pointerEvents: "none",
        }}
      />

      {/* Card border glow */}
      <div
        style={{
          position: "absolute",
          inset: "2px",
          borderRadius: "14px",
          border: `1px solid ${hovered ? "rgba(255,210,80,0.5)" : "rgba(255,255,255,0.08)"}`,
          transition: "border-color 0.3s ease",
          pointerEvents: "none",
        }}
      />

      {/* Pokéball watermark */}
      <div
        style={{
          position: "absolute",
          bottom: "-28px",
          right: "-28px",
          width: "120px",
          height: "120px",
          borderRadius: "50%",
          border: "18px solid rgba(255,255,255,0.04)",
          pointerEvents: "none",
        }}
      />
      <div
        style={{
          position: "absolute",
          bottom: "-8px",
          right: "-8px",
          width: "80px",
          height: "80px",
          borderRadius: "50%",
          border: "12px solid rgba(255,255,255,0.03)",
          pointerEvents: "none",
        }}
      />

      {/* Top accent bar */}
      <div
        style={{
          position: "absolute",
          top: 0,
          left: 0,
          right: 0,
          height: "3px",
          background: "linear-gradient(90deg, #e8b923, #f5d76e, #e8b923)",
          opacity: hovered ? 1 : 0.5,
          transition: "opacity 0.3s ease",
        }}
      />

      {/* Content */}
      <div
        style={{
          position: "relative",
          height: "100%",
          display: "flex",
          flexDirection: "column",
          justifyContent: "space-between",
          padding: "18px 16px 16px",
        }}>
        {/* TCG logo area */}
        <div>
          <div
            style={{
              fontSize: "8px",
              letterSpacing: "3px",
              textTransform: "uppercase",
              color: "rgba(255,210,80,0.7)",
              marginBottom: "6px",
            }}>
            Pokémon TCG
          </div>
          <div
            style={{
              width: "28px",
              height: "2px",
              background: "rgba(255,210,80,0.4)",
            }}
          />
        </div>

        {/* Deck name */}
        <div>
          <div
            style={{
              fontSize: "15px",
              fontWeight: "700",
              color: "#f0e6c8",
              lineHeight: "1.25",
              letterSpacing: "0.5px",
              textShadow: hovered ? "0 0 12px rgba(255,210,80,0.4)" : "none",
              transition: "text-shadow 0.3s ease",
              marginBottom: "8px",
              wordBreak: "break-word",
            }}>
            {deck.name}
          </div>

          {/* ID badge */}
          <div
            style={{
              display: "inline-flex",
              alignItems: "center",
              gap: "5px",
              background: "rgba(255,255,255,0.06)",
              border: "1px solid rgba(255,255,255,0.1)",
              borderRadius: "6px",
              padding: "3px 8px",
            }}>
            <span
              style={{
                fontSize: "8px",
                letterSpacing: "1.5px",
                color: "rgba(255,210,80,0.6)",
                textTransform: "uppercase",
              }}>
              ID
            </span>
            <span
              style={{
                fontFamily: "'Courier New', monospace",
                fontSize: "10px",
                color: "rgba(255,255,255,0.5)",
                letterSpacing: "0.5px",
              }}>
              {deck.id}
            </span>
          </div>
        </div>
      </div>

      <style>{`
        @import url('https://fonts.googleapis.com/css2?family=Cinzel:wght@400;700&display=swap');
        @keyframes shimmer {
          0% { background-position: 200% 0%; }
          100% { background-position: -200% 0%; }
        }
      `}</style>
    </div>
  );
}