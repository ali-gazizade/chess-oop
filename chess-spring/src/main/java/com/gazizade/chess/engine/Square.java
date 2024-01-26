package com.gazizade.chess.engine;

import java.util.HashMap;
import java.util.Optional;

import com.gazizade.chess.engine.enums.NeighborSide;
import com.gazizade.chess.engine.enums.SquareState;
import com.gazizade.chess.engine.interfaces.PieceHolder;
import com.gazizade.chess.engine.pieces.Bishop;
import com.gazizade.chess.engine.pieces.King;
import com.gazizade.chess.engine.pieces.Knight;
import com.gazizade.chess.engine.pieces.Pawn;
import com.gazizade.chess.engine.pieces.Queen;
import com.gazizade.chess.engine.pieces.Rook;
import com.gazizade.chess.engine.records.PieceMoveResult;
import com.gazizade.chess.engine.records.Position;

public class Square implements PieceHolder {
    private Piece piece;
    private SquareState state;
    private Position position = new Position(0, 0);
    private HashMap<NeighborSide, Square> neighbors = new HashMap<>();
    private Game game;

    public Square (Game game) {
        this.game = game;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Piece getPiece() {
        return piece;
    }

    @Override
    public void holdPiece(Piece piece) {
        this.piece = piece;
        this.piece.setSquare(this);
    }

    @Override
    public Piece releaseHoldedPiece() {
        Piece holdedPiece = piece;

        if (piece != null) {
            piece.setSquare(null);
            piece = null;
        }

        return holdedPiece;
    }

    public void putNeighbor(NeighborSide neighborSide, Square square) {
        neighbors.put(neighborSide, square);
    }

    public Optional<Square> getNeighbor(NeighborSide neighborSide) {
        return Optional.ofNullable(neighbors.get(neighborSide));
    }

    public void setState (SquareState state) {
        this.state = state;
    }

    public SquareState getState () {
        return state;
    }

    public void select () throws Exception {
        if (game.getCurrentMove() == null) {
            if (piece != null && piece.getPlayer() == game.getCurrentPlayer()) {
                game.setCurrentMove(new Move(game, game.getCurrentPlayer()));
                game.getCurrentMove().saveStatesBeforeFirstStep();

                piece.select();
                game.getCurrentMove().setFromSquare(this);

                game.getCheckService().cleanSquaresFromUnavailableStates();
            } else {
                throw new Exception("Wrong selection");
            }
        } else {
            game.getCurrentMove().saveStatesBeforeSecondStep(true, true, true, true);

            PieceMoveResult pieceMoveResult = game.getSelectedPiece().move(this);

            if (pieceMoveResult.result()) {
                // After a successful move
                game.switchCurrentPlayer();
                if (piece != null) {
                    piece.unselect();
                }
                for (int i = 0; i < game.getSquares().length; i++) {
                    game.getSquares()[i].setState(null);
                }

                game.getCurrentMove().setToSquare(this);
                if (pieceMoveResult.additionalMoves() != null) {
                    game.getCurrentMove().setAdditionalMoves(pieceMoveResult.additionalMoves());
                }

                game.getCurrentMove().setComplete(true);
                game.addToMoves(game.getCurrentMove());
                game.setCurrentMove(null);
            } else {
                // After a failed move
                if (piece != null) {
                    piece.unselect();
                }
                for (int i = 0; i < game.getSquares().length; i++) {
                    game.getSquares()[i].setState(null);
                }
                game.setCurrentMove(null);

                throw new Exception("Wrong move");
            }
        }
    }

    public boolean isThreatened (Player byPlayer) {
        Square currentSquare;
        
        // Check threats from queen, rooks and bishops
        NeighborSide[] straightDirections = {
            NeighborSide.X_PLUS,
            NeighborSide.X_MINUS,
            NeighborSide.Y_PLUS,
            NeighborSide.Y_MINUS
        };

        NeighborSide[][] diagonalDirections = {
            { NeighborSide.X_PLUS, NeighborSide.Y_PLUS },
            { NeighborSide.X_PLUS, NeighborSide.Y_MINUS },
            { NeighborSide.X_MINUS, NeighborSide.Y_PLUS },
            { NeighborSide.X_MINUS, NeighborSide.Y_MINUS }
        };

        for (int i = 0; i < straightDirections.length; i++) {
            currentSquare = this.getNeighbor(straightDirections[i]).orElse(null);

            while (currentSquare != null) {
                if (currentSquare.getPiece() == null) {
                    currentSquare = currentSquare.getNeighbor(straightDirections[i]).orElse(null);
                } else if (
                    currentSquare.getPiece() != null &&
                    currentSquare.getPiece().getPlayer() == byPlayer && (
                        currentSquare.getPiece() instanceof Rook ||
                        currentSquare.getPiece() instanceof Queen
                    )
                ) {
                    return true;
                } else {
                    currentSquare = null;
                }
            }
        }

        for (int i = 0; i < diagonalDirections.length; i++) {
            currentSquare = this.getNeighbor(diagonalDirections[i][0]).orElse(new Square(game))
                .getNeighbor(diagonalDirections[i][1]).orElse(null);

            while (currentSquare != null) {
                if (currentSquare.getPiece() == null) {
                    currentSquare = currentSquare.getNeighbor(diagonalDirections[i][0]).orElse(new Square(game))
                        .getNeighbor(diagonalDirections[i][1]).orElse(null);
                } else if (
                    currentSquare.getPiece() != null &&
                    currentSquare.getPiece().getPlayer() == byPlayer && (
                        currentSquare.getPiece() instanceof Bishop ||
                        currentSquare.getPiece() instanceof Queen
                    )
                ) {
                    return true;
                } else {
                    currentSquare = null;
                }
            }
        }
        // End Check threats from queen, rooks and bishops

        // Check threats from knights
        Square[] knightSquares = {
            this.getNeighbor(NeighborSide.X_PLUS).orElse(new Square(game))
                .getNeighbor(NeighborSide.Y_PLUS).orElse(new Square(game))
                .getNeighbor(NeighborSide.Y_PLUS).orElse(null),
            this.getNeighbor(NeighborSide.X_PLUS).orElse(new Square(game))
                .getNeighbor(NeighborSide.X_PLUS).orElse(new Square(game))
                .getNeighbor(NeighborSide.Y_PLUS).orElse(null),
            this.getNeighbor(NeighborSide.X_PLUS).orElse(new Square(game))
                .getNeighbor(NeighborSide.X_PLUS).orElse(new Square(game))
                .getNeighbor(NeighborSide.Y_MINUS).orElse(null),
            this.getNeighbor(NeighborSide.X_PLUS).orElse(new Square(game))
                .getNeighbor(NeighborSide.Y_MINUS).orElse(new Square(game))
                .getNeighbor(NeighborSide.Y_MINUS).orElse(null),
            this.getNeighbor(NeighborSide.X_MINUS).orElse(new Square(game))
                .getNeighbor(NeighborSide.Y_MINUS).orElse(new Square(game))
                .getNeighbor(NeighborSide.Y_MINUS).orElse(null),
            this.getNeighbor(NeighborSide.X_MINUS).orElse(new Square(game))
                .getNeighbor(NeighborSide.X_MINUS).orElse(new Square(game))
                .getNeighbor(NeighborSide.Y_MINUS).orElse(null),
            this.getNeighbor(NeighborSide.X_MINUS).orElse(new Square(game))
                .getNeighbor(NeighborSide.X_MINUS).orElse(new Square(game))
                .getNeighbor(NeighborSide.Y_PLUS).orElse(null),
            this.getNeighbor(NeighborSide.X_MINUS).orElse(new Square(game))
                .getNeighbor(NeighborSide.Y_PLUS).orElse(new Square(game))
                .getNeighbor(NeighborSide.Y_PLUS).orElse(null)
        };

        for (int i = 0; i < knightSquares.length; i++) {
            currentSquare = knightSquares[i];

            if (
                currentSquare != null &&
                currentSquare.getPiece() != null &&
                currentSquare.getPiece().getPlayer() == byPlayer &&
                currentSquare.getPiece() instanceof Knight
            ) {
                return true;
            }
        }
        // End Check threats from knights
        
        // Check threats from the king
        Square[] kingSquares = {
            this.getNeighbor(NeighborSide.Y_PLUS).orElse(new Square(game))
            .getNeighbor(NeighborSide.X_PLUS).orElse(null),
            this.getNeighbor(NeighborSide.X_PLUS).orElse(null),
            this.getNeighbor(NeighborSide.X_PLUS).orElse(new Square(game))
            .getNeighbor(NeighborSide.Y_MINUS).orElse(null),
            this.getNeighbor(NeighborSide.Y_MINUS).orElse(null),
            this.getNeighbor(NeighborSide.Y_MINUS).orElse(new Square(game))
            .getNeighbor(NeighborSide.X_MINUS).orElse(null),
            this.getNeighbor(NeighborSide.X_MINUS).orElse(null),
            this.getNeighbor(NeighborSide.X_MINUS).orElse(new Square(game))
            .getNeighbor(NeighborSide.Y_PLUS).orElse(null),
            this.getNeighbor(NeighborSide.Y_PLUS).orElse(null)
        };

        for (int i = 0; i < kingSquares.length; i++) {
            currentSquare = kingSquares[i];

            if (
                currentSquare != null &&
                currentSquare.getPiece() != null &&
                currentSquare.getPiece().getPlayer() == byPlayer &&
                currentSquare.getPiece() instanceof King
            ) {
                return true;
            }
        }
        // End Check threats from the king

        // Check threats from pawns
        if (byPlayer == game.getBlackPlayer()) {
            currentSquare = this.getNeighbor(NeighborSide.X_PLUS).orElse(new Square(game))
                .getNeighbor(NeighborSide.Y_PLUS).orElse(null);
            if (
                currentSquare != null &&
                currentSquare.getPiece() != null &&
                currentSquare.getPiece().getPlayer() == byPlayer &&
                currentSquare.getPiece() instanceof Pawn
            ) {
                return true;
            }

            currentSquare = this.getNeighbor(NeighborSide.X_MINUS).orElse(new Square(game))
                .getNeighbor(NeighborSide.Y_PLUS).orElse(null);
            if (
                currentSquare != null &&
                currentSquare.getPiece() != null &&
                currentSquare.getPiece().getPlayer() == byPlayer &&
                currentSquare.getPiece() instanceof Pawn
            ) {
                return true;
            }
        } else {
            currentSquare = this.getNeighbor(NeighborSide.X_PLUS).orElse(new Square(game))
                .getNeighbor(NeighborSide.Y_MINUS).orElse(null);
            if (
                currentSquare != null &&
                currentSquare.getPiece() != null &&
                currentSquare.getPiece().getPlayer() == byPlayer &&
                currentSquare.getPiece() instanceof Pawn
            ) {
                return true;
            }

            currentSquare = this.getNeighbor(NeighborSide.X_MINUS).orElse(new Square(game))
                .getNeighbor(NeighborSide.Y_MINUS).orElse(null);
            if (
                currentSquare != null &&
                currentSquare.getPiece() != null &&
                currentSquare.getPiece().getPlayer() == byPlayer &&
                currentSquare.getPiece() instanceof Pawn
            ) {
                return true;
            }
        }
        // End Check threats from pawns

        return false;
    }
}
