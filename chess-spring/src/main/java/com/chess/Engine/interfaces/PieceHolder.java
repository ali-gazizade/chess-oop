package com.chess.Engine.interfaces;

import com.chess.Engine.Piece;

public interface PieceHolder {
    public void holdPiece(Piece piece);

    public Piece releaseHoldedPiece();
}
