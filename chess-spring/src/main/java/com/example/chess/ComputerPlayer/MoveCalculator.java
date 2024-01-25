package com.example.chess.ComputerPlayer;

import java.util.ArrayList;
import java.util.Arrays;

import com.example.chess.Engine.CheckService;
import com.example.chess.Engine.Game;
import com.example.chess.Engine.Player;
import com.example.chess.Engine.Square;
import com.example.chess.Engine.enums.GameSituationResult;
import com.example.chess.Engine.records.GameSituation;

public class MoveCalculator {
    private final int MAX_LEVELS = 4;
    private final Game game;
    private final CheckService checkService;
    private Player computerPlayer;
    private ArrayList<SimulationMove> startMoves = new ArrayList<>();
    private SimulationMove bestMove = null;

    public MoveCalculator (Game game) {
        this.game = game;
        this.checkService = new CheckService(game);
    }

    public void makeMoves (int level, SimulationMove parentMove) {
        Player currentPlayer = game.getCurrentPlayer();
        SimulationMove currentMove = null;

        try {
            for (int i = 0; i < game.getSquares().length; i++) {
                Square square = game.getSquares()[i];

                if (square.getPiece() == null || square.getPiece().getPlayer() != currentPlayer) {
                    continue;
                }

                square.select(); // Selection

                for (int i2 = 0; i2 < game.getSquares().length; i2++) {
                    Square square2 = game.getSquares()[i2];
        
                    if (square2.getState() == null) {
                        continue;
                    }

                    square2.select(); // Moving a piece

                    currentMove = new SimulationMove(square, square2, currentPlayer);
                    
                    GameSituation gameSituation = checkService.checkGameSituation();

                    // Set current game situation result
                    if (gameSituation.result() == GameSituationResult.WIN) {
                        if (gameSituation.byPlayer() == computerPlayer) {
                            currentMove.setResult(100);
                        } else {
                            currentMove.setResult(-100);
                        }
                    } else if (gameSituation.result() == GameSituationResult.DRAW) {
                        currentMove.setResult(-5);
                    }  else { // Calculation
                        currentMove.setResult(
                            Arrays.stream(game.getSquares())
                                .filter(e -> (e.getPiece() != null && e.getPiece().getPlayer() == computerPlayer))
                                .mapToInt(e -> e.getPiece().getValue())
                                .sum()
                        );

                        currentMove.setResult(
                            currentMove.getResult() - Arrays.stream(game.getSquares())
                                .filter(e -> (e.getPiece() != null && e.getPiece().getPlayer() != computerPlayer))
                                .mapToInt(e -> e.getPiece().getValue())
                                .sum()
                        );
                    }
                    // End Set current game situation result

                    if (parentMove != null) {
                        parentMove.addToChildMoves(currentMove);
                    }

                    if (
                        level != MAX_LEVELS &&
                        gameSituation.result() != GameSituationResult.WIN &&
                        gameSituation.result() != GameSituationResult.DRAW
                    ) {
                        makeMoves(level + 1, currentMove);
                    }

                    if (level == 1) {
                        startMoves.add(currentMove);
                    }

                    game.goToPrevStep();
                }

                game.goToPrevStep();
            }
        } catch (Exception exception) {
            System.out.println(exception);
        }
    }

    private void calculateBackTrackedResult (SimulationMove move) { // Back track moves from grandchilds to grandparents recursively
        int i = 0;
        for (SimulationMove childMove: move.getChildMoves()) {
            if (childMove.getChildMoves().size() > 0) {
                calculateBackTrackedResult(childMove);
            }

            if (i == 0) { // The first child move result overrides the parent move result
                move.setResult(childMove.getResult());
            } else if (childMove.getPlayedBy() == computerPlayer && childMove.getResult() > move.getResult()) { // Computer's best result move
                move.setResult(childMove.getResult());
            } else if (childMove.getPlayedBy() != computerPlayer && childMove.getResult() < move.getResult()) { // Player's worst result move. Best for the player, worst for the computer
                move.setResult(childMove.getResult());
            }

            i++;
        }
    }

    public void calculateBestMove () {
        this.computerPlayer = game.getCurrentPlayer();

        makeMoves(1, null);

        bestMove = startMoves.getFirst();
        for (SimulationMove startMove: startMoves) {
            calculateBackTrackedResult(startMove);

            if (startMove.getResult() > bestMove.getResult()) {
                bestMove = startMove;
            }
        };
    }

    public SimulationMove getBestMove () {
        return bestMove;
    }
}
