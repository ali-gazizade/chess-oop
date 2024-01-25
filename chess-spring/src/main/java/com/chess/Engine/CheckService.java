package com.chess.Engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.chess.Engine.enums.GameSituationResult;
import com.chess.Engine.pieces.King;
import com.chess.Engine.pieces.Pawn;
import com.chess.Engine.records.GameSituation;

public class CheckService {
    private Game game;

    public CheckService (Game game) {
        this.game = game;
    }

    public void cleanSquaresFromUnavailableStates () throws Exception {
        Player currentPlayer = game.getCurrentPlayer(); // It will change after a move
        Player opponentPlayer = game.getOpponent();
        Square kingSquare = Arrays.stream(game.getSquares()).filter(e ->
            e.getPiece() instanceof King && e.getPiece().getPlayer() == currentPlayer
        ).findFirst().orElse(null);

        for (int i = 0; i < game.getSquares().length; i++) {
            Square square = game.getSquares()[i];

            if (square.getState() != null) {
                square.select(); // Make a move
                kingSquare = Arrays.stream(game.getSquares()).filter(e ->
                    e.getPiece() instanceof King && e.getPiece().getPlayer() == currentPlayer
                ).findFirst().orElse(null); // Find kingSquare again. Because it may change after the move
                
                if (kingSquare.isThreatened(opponentPlayer)) {
                    game.goToPrevStep(); // Revert the move back before removing the square state
                    square.setState(null);
                } else {
                    game.goToPrevStep(); // Just revert the move back
                }
            }
        }
    }

    public GameSituation checkGameSituation () throws Exception {
        Player currentPlayer = game.getCurrentPlayer(); // It will change after a move
        Player opponentPlayer = game.getOpponent();
        Square kingSquare = Arrays.stream(game.getSquares()).filter(e ->
            e.getPiece() instanceof King && e.getPiece().getPlayer() == currentPlayer
        ).findFirst().orElse(null);
        List<Square> pawnSquares = Arrays.stream(game.getSquares()).filter(e ->
            e.getPiece() != null && e.getPiece().getPlayer() == currentPlayer && e.getPiece() instanceof Pawn
        ).collect(Collectors.toList());
        List<Square> nonPawnPieceSquares = Arrays.stream(game.getSquares()).filter(e ->
            e.getPiece() != null && e.getPiece().getPlayer() == currentPlayer && !(e.getPiece() instanceof Pawn)
        ).collect(Collectors.toList());
        boolean isCheck = kingSquare.isThreatened(opponentPlayer);

        ArrayList<Square> pieceSquares = new ArrayList<>(pawnSquares);
        pieceSquares.addAll(nonPawnPieceSquares);

        for (Square square: pieceSquares) {
            square.select(); // Select a piece
            boolean thereIsAvailableMove = Arrays.stream(game.getSquares()).filter(e -> e.getState() != null).count() > 0;
            game.goToPrevStep(); // Revert the selection back

            if (thereIsAvailableMove) {
                if (isCheck) {
                    return new GameSituation(GameSituationResult.CHECK, null);
                } else {
                    return new GameSituation(null, null);
                }
            }
        }

        if (isCheck) {
            return new GameSituation(GameSituationResult.WIN, opponentPlayer);
        }

        return new GameSituation(GameSituationResult.DRAW, null);
    }
}
