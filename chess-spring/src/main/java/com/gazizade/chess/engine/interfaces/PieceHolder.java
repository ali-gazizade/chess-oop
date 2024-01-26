package com.gazizade.chess.engine.interfaces;

import com.gazizade.chess.engine.Piece;

public interface PieceHolder {
    public void holdPiece(Piece piece);

    public Piece releaseHoldedPiece();
}
