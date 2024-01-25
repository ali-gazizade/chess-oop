package com.chess.Engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Stream;

import com.chess.Engine.enums.NeighborSide;
import com.chess.Engine.records.Position;

public class Game {
    enum PlayerType {
        WHITE,
        BLACK
    }

    private final int dimensionCount;
    private HashMap<PlayerType, Player> players = new HashMap<>();
    private Piece selectedPiece;
    private Move currentMove;
    private Player currentPlayer;
    private Square[] squares;
    private ArrayList<Move> moves = new ArrayList<>();
    private CheckService checkService = new CheckService(this);

    public Game (int dimensionCount) {
        this.dimensionCount = dimensionCount;
        players.put(PlayerType.WHITE, new Player("White", this));
        players.put(PlayerType.BLACK, new Player("Black", this));
        currentPlayer = this.players.get(PlayerType.WHITE);
        squares = new Square[dimensionCount * dimensionCount];

        for (int i = 0; i < squares.length; i++) {
            squares[i] = new Square(this);
            Square current = squares[i];

            current.setPosition(new Position(
                i % dimensionCount,
                (int) Math.floor(i / dimensionCount)
            ));
        }

        for (int i = 0; i < squares.length; i++) {
            Square current = squares[i];
            
            current.putNeighbor(
                NeighborSide.X_PLUS,
                Stream.of(squares).filter(e -> (
                    e.getPosition().x() == current.getPosition().x() + 1 && e.getPosition().y() == current.getPosition().y()
                )).findFirst().orElse(null)
            );
            current.putNeighbor(
                NeighborSide.X_MINUS,
                Stream.of(squares).filter(e -> (
                    e.getPosition().x() == current.getPosition().x() - 1 && e.getPosition().y() == current.getPosition().y()
                )).findFirst().orElse(null)
            );
            current.putNeighbor(
                NeighborSide.Y_PLUS,
                Stream.of(squares).filter(e -> (
                    e.getPosition().x() == current.getPosition().x() && e.getPosition().y() == current.getPosition().y() + 1
                )).findFirst().orElse(null)
            );
            current.putNeighbor(
                NeighborSide.Y_MINUS,
                Stream.of(squares).filter(e -> (
                    e.getPosition().x() == current.getPosition().x() && e.getPosition().y() == current.getPosition().y() - 1
                )).findFirst().orElse(null)
            );
        }
    }

    public Game (Game game) {
        this.dimensionCount = game.dimensionCount;
        
        this.currentPlayer = game.currentPlayer;
        this.currentMove = game.currentMove;
        this.selectedPiece = game.selectedPiece;
        this.moves = new ArrayList<>(game.moves);
    }

    public void revertToClonedState (Game game) {
        this.currentPlayer = game.currentPlayer;
        this.currentMove = game.currentMove;
        this.selectedPiece = game.selectedPiece;
        this.moves = new ArrayList<>(game.moves);
    }

    public int getDimensionCount() {
        return dimensionCount;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public CheckService getCheckService() {
        return checkService;
    }

    public Player getWhitePlayer () {
        return players.get(PlayerType.WHITE);
    }

    public Player getBlackPlayer () {
        return players.get(PlayerType.BLACK);
    }

    public Piece getSelectedPiece() {
        return selectedPiece;
    }

    public void setSelectedPiece(Piece selectedPiece) {
        this.selectedPiece = selectedPiece;
    }

    public Move getCurrentMove () {
        return currentMove;
    }

    public void setCurrentMove (Move move) {
        this.currentMove = move;
    }

    public Square[] getSquares () {
        return squares;
    }

    public void switchCurrentPlayer () {
        currentPlayer = currentPlayer == players.get(PlayerType.WHITE)
            ? players.get(PlayerType.BLACK)
            : players.get(PlayerType.WHITE);
    }

    public void addPiece (Piece piece, Position position) {
        Square square = Stream.of(squares).filter(
            e -> e.getPosition().x() == position.x() && e.getPosition().y() == position.y()
        ).findFirst().orElse(null);

        square.holdPiece(piece);
    }

    public void addToMoves (Move move) {
        moves.add(move);
    }

    public Player getOpponent () {
        return (
            currentPlayer == players.get(PlayerType.WHITE)
                ? players.get(PlayerType.BLACK)
                : players.get(PlayerType.WHITE)
        );
    }

    public Move getLastMove () {
        if (!moves.isEmpty()) {
            return moves.get(moves.size() - 1);
        } else {
            return null;
        }
    }

    public void goToPrevStep () {
        if (currentMove == null) {
            if (!moves.isEmpty()) {
                currentMove = moves.removeLast();
                currentMove.goToPrevStep();
            }
        } else {
            currentMove.goToPrevStep();
            currentMove = null;
        }
    }
}
