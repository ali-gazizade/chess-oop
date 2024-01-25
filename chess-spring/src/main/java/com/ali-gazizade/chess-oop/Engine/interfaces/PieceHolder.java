package com.example.chess.Engine.interfaces;

import com.example.chess.Engine.Piece;

public interface PieceHolder {
    public void holdPiece(Piece piece);

    public Piece releaseHoldedPiece();
}
