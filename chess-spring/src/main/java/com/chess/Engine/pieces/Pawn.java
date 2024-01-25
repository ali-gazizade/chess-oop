package com.chess.Engine.pieces;

import com.chess.Engine.Game;
import com.chess.Engine.Move;
import com.chess.Engine.Piece;
import com.chess.Engine.Player;
import com.chess.Engine.Square;
import com.chess.Engine.enums.NeighborSide;
import com.chess.Engine.enums.SquareState;
import com.chess.Engine.records.PieceMoveResult;

public class Pawn extends Piece{
    public Pawn(Game game, Player player) {
        super(game, player);

        value = 1;
    }
    
    public void select () {
        super.select();

        Square currentSquare;
        Move lastMove = game.getLastMove();

        if (player == game.getWhitePlayer()) {
            // Taking
            currentSquare = square
                .getNeighbor(NeighborSide.X_PLUS).orElse(new Square(game))
                .getNeighbor(NeighborSide.Y_PLUS).orElse(new Square(game));

            if (
                currentSquare.getPiece() != null &&
                currentSquare.getPiece().getPlayer() == game.getBlackPlayer()
            ) {
                currentSquare.setState(SquareState.PIECE_CAN_BE_TAKEN);
            }
            
            currentSquare = square
                .getNeighbor(NeighborSide.X_MINUS).orElse(new Square(game))
                .getNeighbor(NeighborSide.Y_PLUS).orElse(new Square(game));

            if (
                currentSquare.getPiece() != null &&
                currentSquare.getPiece().getPlayer() == game.getBlackPlayer()
            ) {
                currentSquare.setState(SquareState.PIECE_CAN_BE_TAKEN);
            }
            // End Taking

            if (square.getPosition().y() < game.getDimensionCount() - 2) {
                // Move forward
                currentSquare = square.getNeighbor(NeighborSide.Y_PLUS).orElse(null);
                if (currentSquare != null && currentSquare.getPiece() == null) {
                    currentSquare.setState(SquareState.CAN_BE_MOVED_TO);

                    if (square.getPosition().y() == 1) { // 2 steps forward
                        currentSquare = currentSquare.getNeighbor(NeighborSide.Y_PLUS).orElse(null);

                        if (currentSquare != null && currentSquare.getPiece() == null)  {
                            currentSquare.setState(SquareState.CAN_BE_MOVED_TO);
                        }
                    }
                }
                // End Move forward
            } else {
                // Changing to a powerful piece
                currentSquare = square.getNeighbor(NeighborSide.Y_PLUS).orElse(null);
                if (currentSquare != null && currentSquare.getPiece() == null) {
                    currentSquare.setState(SquareState.OTHER);
                }
                // End Changing to a powerful piece

                // Also taking
                currentSquare = square
                    .getNeighbor(NeighborSide.X_PLUS).orElse(new Square(game))
                    .getNeighbor(NeighborSide.Y_PLUS).orElse(new Square(game));

                if (
                    currentSquare.getPiece() != null &&
                    currentSquare.getPiece().getPlayer() == game.getBlackPlayer()
                ) {
                    currentSquare.setState(SquareState.OTHER);
                }
                
                currentSquare = square
                    .getNeighbor(NeighborSide.X_MINUS).orElse(new Square(game))
                    .getNeighbor(NeighborSide.Y_PLUS).orElse(new Square(game));

                if (
                    currentSquare.getPiece() != null &&
                    currentSquare.getPiece().getPlayer() == game.getBlackPlayer()
                ) {
                    currentSquare.setState(SquareState.OTHER);
                }
                // End Also taking
            }

            // Pawn passes
            currentSquare = square.getNeighbor(NeighborSide.X_PLUS).orElse(null);
            if (
                currentSquare != null &&
                lastMove != null &&
                currentSquare.getPiece() instanceof Pawn &&
                lastMove.getToSquare() == currentSquare &&
                lastMove.getFromSquare() == currentSquare
                    .getNeighbor(NeighborSide.Y_PLUS).orElse(new Square(game))
                    .getNeighbor(NeighborSide.Y_PLUS).orElse(null)
            ) {
                currentSquare.getNeighbor(NeighborSide.Y_PLUS).get().setState(SquareState.OTHER);
            }

            currentSquare = square.getNeighbor(NeighborSide.X_MINUS).orElse(null);
            if (
                currentSquare != null &&
                lastMove != null &&
                currentSquare.getPiece() instanceof Pawn &&
                lastMove.getToSquare() == currentSquare &&
                lastMove.getFromSquare() == currentSquare
                    .getNeighbor(NeighborSide.Y_PLUS).orElse(new Square(game))
                    .getNeighbor(NeighborSide.Y_PLUS).orElse(null)
            ) {
                currentSquare.getNeighbor(NeighborSide.Y_PLUS).get().setState(SquareState.OTHER);
            }
            // End Pawn passes
        } else {
            // Taking
            currentSquare = square
                .getNeighbor(NeighborSide.X_PLUS).orElse(new Square(game))
                .getNeighbor(NeighborSide.Y_MINUS).orElse(new Square(game));

            if (
                currentSquare.getPiece() != null &&
                currentSquare.getPiece().getPlayer() == game.getWhitePlayer()
            ) {
                currentSquare.setState(SquareState.PIECE_CAN_BE_TAKEN);
            }
            
            currentSquare = square
                .getNeighbor(NeighborSide.X_MINUS).orElse(new Square(game))
                .getNeighbor(NeighborSide.Y_MINUS).orElse(new Square(game));

            if (
                currentSquare.getPiece() != null &&
                currentSquare.getPiece().getPlayer() == game.getWhitePlayer()
            ) {
                currentSquare.setState(SquareState.PIECE_CAN_BE_TAKEN);
            }
            // End Taking

            if (square.getPosition().y() > 1) {
                // Move forward
                currentSquare = square.getNeighbor(NeighborSide.Y_MINUS).orElse(null);
                if (currentSquare != null && currentSquare.getPiece() == null) {
                    currentSquare.setState(SquareState.CAN_BE_MOVED_TO);

                    if (square.getPosition().y() == 6) { // 2 steps forward
                        currentSquare = currentSquare.getNeighbor(NeighborSide.Y_MINUS).orElse(null);

                        if (currentSquare != null && currentSquare.getPiece() == null)  {
                            currentSquare.setState(SquareState.CAN_BE_MOVED_TO);
                        }
                    }
                }
                // End Move forward
            } else {
                // Changing to a powerful piece
                currentSquare = square.getNeighbor(NeighborSide.Y_MINUS).orElse(null);
                if (currentSquare != null && currentSquare.getPiece() == null) {
                    currentSquare.setState(SquareState.OTHER);
                }
                // End Changing to a powerful piece

                // Also taking
                currentSquare = square
                    .getNeighbor(NeighborSide.X_PLUS).orElse(new Square(game))
                    .getNeighbor(NeighborSide.Y_MINUS).orElse(new Square(game));

                if (
                    currentSquare.getPiece() != null &&
                    currentSquare.getPiece().getPlayer() == game.getWhitePlayer()
                ) {
                    currentSquare.setState(SquareState.OTHER);
                }
                
                currentSquare = square
                    .getNeighbor(NeighborSide.X_MINUS).orElse(new Square(game))
                    .getNeighbor(NeighborSide.Y_MINUS).orElse(new Square(game));

                if (
                    currentSquare.getPiece() != null &&
                    currentSquare.getPiece().getPlayer() == game.getWhitePlayer()
                ) {
                    currentSquare.setState(SquareState.OTHER);
                }
                // End Also taking
            }

            // Pawn passes
            currentSquare = square.getNeighbor(NeighborSide.X_PLUS).orElse(null);
            if (
                currentSquare != null &&
                lastMove != null &&
                currentSquare.getPiece() instanceof Pawn &&
                lastMove.getToSquare() == currentSquare &&
                lastMove.getFromSquare() == currentSquare
                    .getNeighbor(NeighborSide.Y_MINUS).orElse(new Square(game))
                    .getNeighbor(NeighborSide.Y_MINUS).orElse(null)
            ) {
                currentSquare.getNeighbor(NeighborSide.Y_MINUS).get().setState(SquareState.OTHER);
            }

            currentSquare = square.getNeighbor(NeighborSide.X_MINUS).orElse(null);
            if (
                currentSquare != null &&
                lastMove != null &&
                currentSquare.getPiece() instanceof Pawn &&
                lastMove.getToSquare() == currentSquare &&
                lastMove.getFromSquare() == currentSquare
                    .getNeighbor(NeighborSide.Y_MINUS).orElse(new Square(game))
                    .getNeighbor(NeighborSide.Y_MINUS).orElse(null)
            ) {
                currentSquare.getNeighbor(NeighborSide.Y_MINUS).get().setState(SquareState.OTHER);
            }
            // End Pawn passes
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
        } else if (square.getState() == SquareState.OTHER) {
            if (
                (player == game.getWhitePlayer() && square.getPosition().y() == game.getDimensionCount() - 1) ||
                (player == game.getBlackPlayer() && square.getPosition().y() == 0)
            ) { // Reaching to the end
                Move currentMove = game.getCurrentMove();

                if (square.getPiece() != null) { // Taking an opponent piece (if exists)
                    game.getCurrentPlayer().addToTakenPieces(square, square.getPiece());
                    square.releaseHoldedPiece();
                }

                currentMove.addToTakenPieces(this.square, this, game.getCurrentPlayer()); // Take pawn for replacing with another powerful piece
                super.move(square);
                Piece newPiece = new Queen(game, player);
                square.holdPiece(newPiece);
                currentMove.addToAddedPieces(square, newPiece); // Add the replaced powerful piece to the move

                return new PieceMoveResult(true, null);
            } else { // Pawn pass
                super.move(square);

                if (player == game.getWhitePlayer()) {
                    Piece piece = square.getNeighbor(NeighborSide.Y_MINUS).orElse(new Square(game)).getPiece();

                    if (piece == null) {
                        throw new Exception("Pawn pass piece not found");
                    }

                    game.getCurrentPlayer().addToTakenPieces(piece.getSquare(), piece);
                    square.getNeighbor(NeighborSide.Y_MINUS).get().releaseHoldedPiece();
                } else {
                    Piece piece = square.getNeighbor(NeighborSide.Y_PLUS).orElse(new Square(game)).getPiece();

                    if (piece == null) {
                        throw new Exception("Pawn pass piece not found");
                    }

                    game.getCurrentPlayer().addToTakenPieces(piece.getSquare(), piece);
                    square.getNeighbor(NeighborSide.Y_PLUS).get().releaseHoldedPiece();
                }

                return new PieceMoveResult(true, null);
            }
        }

        return new PieceMoveResult(false, null);
    }
}
