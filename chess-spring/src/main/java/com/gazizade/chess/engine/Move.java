package com.gazizade.chess.engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.gazizade.chess.engine.enums.SquareState;

public class Move {
    private record TakenPiece (Square square, Piece piece, Player takingPlayer) {}

    private record AddedPiece (Square square, Piece piece) {}

    private enum CloneName {
        GAME,
        PLAYER,
        SQUARE_STATES,
        PIECE_HAS_EVER_MOVED
    };

    private Game game;
    private Player playedBy;
    private Square fromSquare;
    private Square toSquare;
    private ArrayList<TakenPiece> takenPieces = new ArrayList<>();
    private ArrayList<AddedPiece> addedPieces = new ArrayList<>();
    private ArrayList<Move> additionalMoves = new ArrayList<>(); // For Castling
    private boolean isComplete = false;
    private HashMap<CloneName, Object> clonesBeforeFirstStep = new HashMap<>();
    private HashMap<CloneName, Object> clonesBeforeSecondStep = new HashMap<>();

    public Move (Game game, Player playedBy) {
        this.game = game;
        this.playedBy = playedBy;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean isComplete) {
        this.isComplete = isComplete;
    }

    public Square getFromSquare() {
        return fromSquare;
    }

    public void setFromSquare(Square fromSquare) {
        this.fromSquare = fromSquare;
    }

    public Square getToSquare() {
        return toSquare;
    }

    public void setToSquare(Square toSquare) {
        this.toSquare = toSquare;
    }

    public void addToTakenPieces (Square square, Piece piece, Player takingPlayer) {
        this.takenPieces.add(new TakenPiece(square, piece, takingPlayer));
    }

    public void addToAddedPieces (Square square, Piece piece) {
        this.addedPieces.add(new AddedPiece(square, piece));
    }

    public void setAdditionalMoves (ArrayList<Move> additionalMoves) {
        this.additionalMoves = additionalMoves;
    }

    public void saveStatesBeforeFirstStep () {
        clonesBeforeFirstStep.put(CloneName.GAME, new Game(game));
        clonesBeforeFirstStep.put(CloneName.PLAYER, new Player(playedBy));
        clonesBeforeFirstStep.put(CloneName.SQUARE_STATES, Arrays.stream(game.getSquares()).map(e ->
            e.getState()
        ).toArray(SquareState[]::new));
    }

    public void saveStatesBeforeSecondStep (
        boolean saveGame,
        boolean savePlayer,
        boolean saveSquareStates,
        boolean savePieceHasEverMoved
    ) {
        if (saveGame) {
            clonesBeforeSecondStep.put(CloneName.GAME, new Game(game));
        }

        if (savePlayer) {
            clonesBeforeSecondStep.put(CloneName.PLAYER, new Player(playedBy));
        }

        if (saveSquareStates) {
            clonesBeforeSecondStep.put(CloneName.SQUARE_STATES, Arrays.stream(game.getSquares()).map(e ->
                e.getState()
            ).toArray(SquareState[]::new));
        }

        if (savePieceHasEverMoved) {
            clonesBeforeSecondStep.put(CloneName.PIECE_HAS_EVER_MOVED, fromSquare.getPiece().getHasEverMoved());
        }
    }

    public Move goToPrevStep () {
        if (isComplete) {
            // Move the piece back
            Piece piece = toSquare.releaseHoldedPiece();
            fromSquare.holdPiece(piece);
            // End Move the piece back

            // Paste the previous states
            Game clonedGame = (Game) clonesBeforeSecondStep.get(CloneName.GAME);
            if (clonedGame != null) {
                game.revertToClonedState(clonedGame);
            }

            Player clonedPlayer = (Player) clonesBeforeSecondStep.get(CloneName.PLAYER);
            if (clonedPlayer != null) {
                playedBy.revertToClonedState(clonedPlayer);
            }

            SquareState[] clonedSquareStates = (SquareState[]) clonesBeforeSecondStep.get(CloneName.SQUARE_STATES);
            if (clonedSquareStates != null && clonedSquareStates.length > 0) {
                for (int i = 0; i < game.getSquares().length; i++) {
                    Square square = game.getSquares()[i];
                    square.setState(clonedSquareStates[i]);
                }
            }

            Boolean pieceHadEverMoved = (Boolean) clonesBeforeSecondStep.get(CloneName.PIECE_HAS_EVER_MOVED);
            if (pieceHadEverMoved != null) {
                fromSquare.getPiece().setHasEverMoved(pieceHadEverMoved);
            }
            // End Paste the previous states

            // Take added pieces back
            addedPieces.forEach(addedPiece -> {
                addedPiece.square().releaseHoldedPiece();
            });
            addedPieces = new ArrayList<>();
            // End Take added pieces back

            // Add taken pieces back to squares. Game and player's taken pieces are already reverted.
            takenPieces.forEach(takenPiece -> {
                takenPiece.square().releaseHoldedPiece();
                takenPiece.square().holdPiece(takenPiece.piece());
            });
            takenPieces = new ArrayList<>();
            // End Add taken pieces back to squares. Game and player's taken pieces are already reverted.

            // Revert additional moves back
            additionalMoves.forEach(move -> {
                move.goToPrevStep().goToPrevStep();
            });
            additionalMoves = new ArrayList<>();
            // End Revert additional moves back

            isComplete = false;
        } else {
            Game clonedGame = (Game) clonesBeforeFirstStep.get(CloneName.GAME);
            if (clonedGame != null) {
                game.revertToClonedState(clonedGame);
            }

            Player clonedPlayer = (Player) clonesBeforeFirstStep.get(CloneName.PLAYER);
            if (clonedPlayer != null) {
                playedBy.revertToClonedState(clonedPlayer);
            }

            SquareState[] clonedSquareStates = (SquareState[]) clonesBeforeFirstStep.get(CloneName.SQUARE_STATES);
            if (clonedSquareStates != null && clonedSquareStates.length > 0) {
                for (int i = 0; i < game.getSquares().length; i++) {
                    Square square = game.getSquares()[i];
                    square.setState(clonedSquareStates[i]);
                }
            }

            setFromSquare(null);
        }

        return this;
    }
}
