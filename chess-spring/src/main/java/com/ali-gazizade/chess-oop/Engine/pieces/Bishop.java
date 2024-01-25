package com.example.chess.Engine.pieces;

import com.example.chess.Engine.Game;
import com.example.chess.Engine.Piece;
import com.example.chess.Engine.Player;
import com.example.chess.Engine.Square;
import com.example.chess.Engine.enums.NeighborSide;
import com.example.chess.Engine.enums.SquareState;
import com.example.chess.Engine.records.PieceMoveResult;

public class Bishop extends Piece{
    public Bishop(Game game, Player player) {
        super(game, player);

        value = 3;
    }

    public void select () {
        super.select();

        NeighborSide[][] directions = {
            { NeighborSide.X_PLUS, NeighborSide.Y_PLUS},
            { NeighborSide.X_PLUS, NeighborSide.Y_MINUS},
            { NeighborSide.X_MINUS, NeighborSide.Y_PLUS},
            { NeighborSide.X_MINUS, NeighborSide.Y_MINUS}
        };

        for (int i = 0; i < directions.length; i++) {
            Square currentSquare = square
                .getNeighbor(directions[i][0]).orElse(new Square(game))
                .getNeighbor(directions[i][1]).orElse(null);

            while (currentSquare != null) {
                if (currentSquare.getPiece() == null) {
                    currentSquare.setState(SquareState.CAN_BE_MOVED_TO);
                    currentSquare = currentSquare
                        .getNeighbor(directions[i][0]).orElse(new Square(game))
                        .getNeighbor(directions[i][1]).orElse(null);
                } else if (currentSquare.getPiece().getPlayer() != game.getCurrentPlayer()) {
                    currentSquare.setState(SquareState.PIECE_CAN_BE_TAKEN);
                    currentSquare = null;
                } else {
                    currentSquare = null;
                }
            }
        }
    }

    @Override
    public PieceMoveResult move (Square square) throws Exception {
        if (square.getState() == SquareState.CAN_BE_MOVED_TO) {
            super.move(square);

            return new PieceMoveResult(true, null);
        } else if (square.getState() == SquareState.PIECE_CAN_BE_TAKEN) {
            game.getCurrentPlayer().addToTakenPieces(square, square.getPiece());
            super.move(square);

            return new PieceMoveResult(true, null);
        }

        return new PieceMoveResult(false, null);
    }
}
