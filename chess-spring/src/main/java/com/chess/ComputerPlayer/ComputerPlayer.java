package com.chess.ComputerPlayer;

import com.chess.Engine.Game;

public class ComputerPlayer {
    public void makeMove(Game game) {
        MoveCalculator moveCalculator = new MoveCalculator(game);
        moveCalculator.calculateBestMove();
        SimulationMove move = moveCalculator.getBestMove();

        try {
            move.getFromSquare().select();
            move.getToSquare().select();
        } catch (Exception exception) {
            System.out.println(exception);
        }
    }
}
