package com.chess.Engine;

import java.util.ArrayList;

public class Player {
    private final String name;
    private final Game game;
    private ArrayList<Piece> takenPieces = new ArrayList<>();

    public Player (String name, Game game) {
        this.name = name;
        this.game = game;
    }

    public Player (Player player) {
        this.name = player.name;
        this.game = player.game;
        this.takenPieces = new ArrayList<>(player.takenPieces);
    }

    public void revertToClonedState (Player player) {
        this.takenPieces = player.takenPieces;
    }

    public String getName () {
        return name;
    }

    public ArrayList<Piece> getTakPieces() {
        return takenPieces;
    }

    public void addToTakenPieces (Square square, Piece piece) {
        this.takenPieces.add(piece);
        this.game.getCurrentMove().addToTakenPieces(square, piece, this);
    }
}
