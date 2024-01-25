package com.example.chess.Engine;

import com.example.chess.Engine.records.PieceMoveResult;

public abstract class Piece {
    protected final Game game;
    protected final Player player;
    protected Square square;
    protected boolean hasEverMoved = false;
    protected int value;

    public Piece (Game game, Player player) {
        this.game = game;
        this.player = player;
    }

    public PieceMoveResult move (Square square) throws Exception {
        this.square.releaseHoldedPiece();
        square.holdPiece(this);

        return null;
    }

    public Square getSquare() {
        return square;
    }

    public void setSquare(Square square) {
        this.square = (Square) square;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean getHasEverMoved() {
        return hasEverMoved;
    }

    public void setHasEverMoved(boolean hasEverMoved) {
        this.hasEverMoved = hasEverMoved;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void select () {
        this.game.setSelectedPiece(this);
    }

    public void unselect () {
        this.game.setSelectedPiece(null);
    }
}
