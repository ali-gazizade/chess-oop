package com.gazizade.chess.engine.pieces;

import java.util.ArrayList;

import com.gazizade.chess.engine.Game;
import com.gazizade.chess.engine.Move;
import com.gazizade.chess.engine.Piece;
import com.gazizade.chess.engine.Player;
import com.gazizade.chess.engine.Square;
import com.gazizade.chess.engine.enums.NeighborSide;
import com.gazizade.chess.engine.enums.SquareState;
import com.gazizade.chess.engine.records.PieceMoveResult;

public class King extends Piece{
    public King(Game game, Player player) {
        super(game, player);

        value = 10;
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
            .getNeighbor(NeighborSide.Y_PLUS).orElse(null);
        checkAndSetState(currentSquare);
        currentSquare = square
            .getNeighbor(NeighborSide.X_PLUS).orElse(null);
        checkAndSetState(currentSquare);
        currentSquare = square
            .getNeighbor(NeighborSide.X_PLUS).orElse(new Square(game))
            .getNeighbor(NeighborSide.Y_MINUS).orElse(null);
        checkAndSetState(currentSquare);
        currentSquare = square
            .getNeighbor(NeighborSide.Y_MINUS).orElse(null);
        checkAndSetState(currentSquare);
        currentSquare = square
            .getNeighbor(NeighborSide.X_MINUS).orElse(new Square(game))
            .getNeighbor(NeighborSide.Y_MINUS).orElse(null);
        checkAndSetState(currentSquare);
        currentSquare = square
            .getNeighbor(NeighborSide.X_MINUS).orElse(null);
        checkAndSetState(currentSquare);
        currentSquare = square
            .getNeighbor(NeighborSide.X_MINUS).orElse(new Square(game))
            .getNeighbor(NeighborSide.Y_PLUS).orElse(null);
        checkAndSetState(currentSquare);
        currentSquare = square
            .getNeighbor(NeighborSide.Y_PLUS).orElse(null);
        checkAndSetState(currentSquare);

        // Check for castling to the right side
        Square rightRookSquare = square
            .getNeighbor(NeighborSide.X_PLUS).orElse(new Square(game))
            .getNeighbor(NeighborSide.X_PLUS).orElse(new Square(game))
            .getNeighbor(NeighborSide.X_PLUS).orElse(null);
        if (
            square.getPosition().x() == 4 &&
            !hasEverMoved &&
            rightRookSquare != null &&
            rightRookSquare.getPiece() instanceof Rook &&
            !((Rook) rightRookSquare.getPiece()).getHasEverMoved()
        ) {
            if (player == game.getWhitePlayer() && square.getPosition().y() == 0) {
                Square xPlusSquare = square.getNeighbor(NeighborSide.X_PLUS).get();
                Square xPlusPlusSquare = square.getNeighbor(NeighborSide.X_PLUS).get()
                    .getNeighbor(NeighborSide.X_PLUS).get();

                if (
                    xPlusSquare.getPiece() == null &&
                    xPlusPlusSquare.getPiece() == null &&
                    !square.isThreatened(game.getBlackPlayer()) &&
                    !xPlusSquare.isThreatened(game.getBlackPlayer()) &&
                    !xPlusPlusSquare.isThreatened(game.getBlackPlayer())
                ) {
                    xPlusPlusSquare.setState(SquareState.OTHER);
                }
            } else if (player == game.getBlackPlayer() && square.getPosition().y() == 7) {
                Square xPlusSquare = square.getNeighbor(NeighborSide.X_PLUS).get();
                Square xPlusPlusSquare = square.getNeighbor(NeighborSide.X_PLUS).get()
                    .getNeighbor(NeighborSide.X_PLUS).get();

                if (
                    xPlusSquare.getPiece() == null &&
                    xPlusPlusSquare.getPiece() == null &&
                    !square.isThreatened(game.getWhitePlayer()) &&
                    !xPlusSquare.isThreatened(game.getWhitePlayer()) &&
                    !xPlusPlusSquare.isThreatened(game.getWhitePlayer())
                ) {
                    xPlusPlusSquare.setState(SquareState.OTHER);
                }
            }
        }
        // End Check for castling to the right side

        // Check for castling to the left side
        Square leftRookSquare = square
            .getNeighbor(NeighborSide.X_MINUS).orElse(new Square(game))
            .getNeighbor(NeighborSide.X_MINUS).orElse(new Square(game))
            .getNeighbor(NeighborSide.X_MINUS).orElse(new Square(game))
            .getNeighbor(NeighborSide.X_MINUS).orElse(null);
        if (
            square.getPosition().x() == 4 &&
            !hasEverMoved &&
            leftRookSquare != null &&
            leftRookSquare.getPiece() instanceof Rook &&
            !((Rook) leftRookSquare.getPiece()).getHasEverMoved()
        ) {
            if (player == game.getWhitePlayer() && square.getPosition().y() == 0) {
                Square xMinusSquare = square.getNeighbor(NeighborSide.X_MINUS).get();
                Square xMinusMinusSquare = square.getNeighbor(NeighborSide.X_MINUS).get()
                    .getNeighbor(NeighborSide.X_MINUS).get();
                Square xMinusMinusMinusSquare = square.getNeighbor(NeighborSide.X_MINUS).get()
                    .getNeighbor(NeighborSide.X_MINUS).get()
                    .getNeighbor(NeighborSide.X_MINUS).get();

                if (
                    xMinusSquare.getPiece() == null &&
                    xMinusMinusSquare.getPiece() == null &&
                    xMinusMinusMinusSquare.getPiece() == null &&
                    !square.isThreatened(game.getBlackPlayer()) &&
                    !xMinusSquare.isThreatened(game.getBlackPlayer()) &&
                    !xMinusMinusSquare.isThreatened(game.getBlackPlayer())
                ) {
                    xMinusMinusSquare.setState(SquareState.OTHER);
                }
            } else if (player == game.getBlackPlayer() && square.getPosition().y() == 7) {
                Square xMinusSquare = square.getNeighbor(NeighborSide.X_MINUS).get();
                Square xMinusMinusSquare = square.getNeighbor(NeighborSide.X_MINUS).get()
                    .getNeighbor(NeighborSide.X_MINUS).get();
                Square xMinusMinusMinusSquare = square.getNeighbor(NeighborSide.X_MINUS).get()
                    .getNeighbor(NeighborSide.X_MINUS).get()
                    .getNeighbor(NeighborSide.X_MINUS).get();

                if (
                    xMinusSquare.getPiece() == null &&
                    xMinusMinusSquare.getPiece() == null &&
                    xMinusMinusMinusSquare.getPiece() == null &&
                    !square.isThreatened(game.getWhitePlayer()) &&
                    !xMinusSquare.isThreatened(game.getWhitePlayer()) &&
                    !xMinusMinusSquare.isThreatened(game.getWhitePlayer())
                ) {
                    xMinusMinusSquare.setState(SquareState.OTHER);
                }
            }
        }
        // End Check for castling to the left side
    }

    @Override
    public PieceMoveResult move (Square square) throws Exception {
        if (square.getState() == SquareState.CAN_BE_MOVED_TO) {
            super.move(square);
            hasEverMoved = true;

            return new PieceMoveResult(true, null);
        } else if (square.getState() == SquareState.PIECE_CAN_BE_TAKEN) {
            game.getCurrentPlayer().addToTakenPieces(square, square.getPiece());
            super.move(square);
            hasEverMoved = true;

            return new PieceMoveResult(true, null);
        } else if (square.getState() == SquareState.OTHER) { // Castling
            super.move(square);
            hasEverMoved = true;

            ArrayList<Move> additionalMoves = new ArrayList<>();
            Move rookMove = new Move(game, game.getCurrentPlayer());
            Square xPlusSquare = square.getNeighbor(NeighborSide.X_PLUS).get();
            Square xMinusSquare = square.getNeighbor(NeighborSide.X_MINUS).get();
            Square xMinusMinusSquare = square
                .getNeighbor(NeighborSide.X_MINUS).get()
                .getNeighbor(NeighborSide.X_MINUS).get();

            if (xPlusSquare.getPiece() instanceof Rook) {
                rookMove.setFromSquare(xPlusSquare);
                rookMove.saveStatesBeforeSecondStep(false, false, false, true);
                
                xPlusSquare.getPiece().move(xMinusSquare);
                rookMove.setToSquare(xMinusSquare);
            } else if (xMinusMinusSquare.getPiece() instanceof Rook) {
                rookMove.setFromSquare(xMinusMinusSquare);
                rookMove.saveStatesBeforeSecondStep(false, false, false, true);

                xMinusMinusSquare.getPiece().move(xPlusSquare);
                rookMove.setToSquare(xPlusSquare);
            }
            rookMove.setComplete(true);
            additionalMoves.add(rookMove);

            return new PieceMoveResult(true, additionalMoves);
        }

        return new PieceMoveResult(false, null);
    }
}
