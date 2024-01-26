package com.gazizade.chess.computerPlayer;

import java.util.ArrayList;

import com.gazizade.chess.engine.Player;
import com.gazizade.chess.engine.Square;

public class SimulationMove {
    private final Square fromSquare;
    private final Square toSquare;
    private final Player playedBy;
    private ArrayList<SimulationMove> childMoves = new ArrayList<>();
    private int result = 0;

    public SimulationMove(Square fromSquare, Square toSquare, Player playedBy) {
        this.fromSquare = fromSquare;
        this.toSquare = toSquare;
        this.playedBy = playedBy;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public Square getFromSquare() {
        return fromSquare;
    }

    public Square getToSquare() {
        return toSquare;
    }

    public Player getPlayedBy() {
        return playedBy;
    }

    public ArrayList<SimulationMove> getChildMoves() {
        return childMoves;
    }

    public void addToChildMoves (SimulationMove simulationMove) {
        childMoves.add(simulationMove);
    }

    public void emptyChildmoves () {
        this.childMoves = new ArrayList<>();
    }
}
