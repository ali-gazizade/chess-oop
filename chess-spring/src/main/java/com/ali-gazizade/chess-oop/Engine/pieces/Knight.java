package com.example.chess.Engine.pieces;

import com.example.chess.Engine.Game;
import com.example.chess.Engine.Piece;
import com.example.chess.Engine.Player;
import com.example.chess.Engine.Square;
import com.example.chess.Engine.enums.NeighborSide;
import com.example.chess.Engine.enums.SquareState;
import com.example.chess.Engine.records.PieceMoveResult;

public class Knight extends Piece{
    public Knight(Game game, Player player) {
        super(game, player);

        value = 2;
    }

    private void checkAndSetState (Square square) {
        if (square != null) {
            if (square.getPiece() == null) {
                square.setState(SquareState.CAN_BE_MOVED_TO);
            } else if (square.getPiece().getPlayer() != game.getCurrentPlayer()) {
                square.setState(SquareState.PIECE_CAN_BE_TAKEN);
            }
        }
    }

    public void select () {
        super.select();
        
        Square currentSquare = square
            .getNeighbor(NeighborSide.X_PLUS).orElse(new Square(game))
            .getNeighbor(NeighborSide.Y_PLUS).orElse(new Square(game))
            .getNeighbor(NeighborSide.Y_PLUS).orElse(null);
        checkAndSetState(currentSquare);
        currentSquare = square
            .getNeighbor(NeighborSide.X_PLUS).orElse(new Square(game))
            .getNeighbor(NeighborSide.X_PLUS).orElse(new Square(game))
            .getNeighbor(NeighborSide.Y_PLUS).orElse(null);
        checkAndSetState(currentSquare);
        currentSquare = square
            .getNeighbor(NeighborSide.X_PLUS).orElse(new Square(game))
            .getNeighbor(NeighborSide.X_PLUS).orElse(new Square(game))
            .getNeighbor(NeighborSide.Y_MINUS).orElse(null);
        checkAndSetState(currentSquare);
        currentSquare = square
            .getNeighbor(NeighborSide.X_PLUS).orElse(new Square(game))
            .getNeighbor(NeighborSide.Y_MINUS).orElse(new Square(game))
            .getNeighbor(NeighborSide.Y_MINUS).orElse(null);
        checkAndSetState(currentSquare);
        currentSquare = square
            .getNeighbor(NeighborSide.X_MINUS).orElse(new Square(game))
            .getNeighbor(NeighborSide.Y_MINUS).orElse(new Square(game))
            .getNeighbor(NeighborSide.Y_MINUS).orElse(null);
        checkAndSetState(currentSquare);
        currentSquare = square
            .getNeighbor(NeighborSide.X_MINUS).orElse(new Square(game))
            .getNeighbor(NeighborSide.X_MINUS).orElse(new Square(game))
            .getNeighbor(NeighborSide.Y_MINUS).orElse(null);
        checkAndSetState(currentSquare);
        currentSquare = square
            .getNeighbor(NeighborSide.X_MINUS).orElse(new Square(game))
            .getNeighbor(NeighborSide.X_MINUS).orElse(new Square(game))
            .getNeighbor(NeighborSide.Y_PLUS).orElse(null);
        checkAndSetState(currentSquare);
        currentSquare = square
            .getNeighbor(NeighborSide.X_MINUS).orElse(new Square(game))
            .getNeighbor(NeighborSide.Y_PLUS).orElse(new Square(game))
            .getNeighbor(NeighborSide.Y_PLUS).orElse(null);
        checkAndSetState(currentSquare);
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
