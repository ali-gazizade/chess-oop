package com.example.chess.Engine;

import com.example.chess.Engine.pieces.Bishop;
import com.example.chess.Engine.pieces.King;
import com.example.chess.Engine.pieces.Knight;
import com.example.chess.Engine.pieces.Pawn;
import com.example.chess.Engine.pieces.Queen;
import com.example.chess.Engine.pieces.Rook;
import com.example.chess.Engine.records.Position;

public class GameBuilder {
    public Game buildStandard() {
        Game game = new Game(8);
        Pawn[] whitePawns = new Pawn[8];
        Pawn[] blackPawns = new Pawn[8];

        // White pieces
        for (int i = 0; i < 8; i++) {
            whitePawns[i] = new Pawn(game, game.getWhitePlayer());

            game.addPiece(whitePawns[i], new Position(i, 1));
        }

        game.addPiece(new Rook(game, game.getWhitePlayer()), new Position(0, 0));
        game.addPiece(new Rook(game, game.getWhitePlayer()), new Position(7, 0));
        game.addPiece(new Knight(game, game.getWhitePlayer()), new Position(1, 0));
        game.addPiece(new Knight(game, game.getWhitePlayer()), new Position(6, 0));
        game.addPiece(new Bishop(game, game.getWhitePlayer()), new Position(2, 0));
        game.addPiece(new Bishop(game, game.getWhitePlayer()), new Position(5, 0));
        game.addPiece(new Queen(game, game.getWhitePlayer()), new Position(3, 0));
        game.addPiece(new King(game, game.getWhitePlayer()), new Position(4, 0));
        // End White pieces

        // Black pieces
        for (int i = 0; i < 8; i++) {
            blackPawns[i] = new Pawn(game, game.getBlackPlayer());

            game.addPiece(blackPawns[i], new Position(i, 6));
        }

        game.addPiece(new Rook(game, game.getBlackPlayer()), new Position(0, 7));
        game.addPiece(new Rook(game, game.getBlackPlayer()), new Position(7, 7));
        game.addPiece(new Knight(game, game.getBlackPlayer()), new Position(1, 7));
        game.addPiece(new Knight(game, game.getBlackPlayer()), new Position(6, 7));
        game.addPiece(new Bishop(game, game.getBlackPlayer()), new Position(2, 7));
        game.addPiece(new Bishop(game, game.getBlackPlayer()), new Position(5, 7));
        game.addPiece(new Queen(game, game.getBlackPlayer()), new Position(3, 7));
        game.addPiece(new King(game, game.getBlackPlayer()), new Position(4, 7));
        // End Black pieces

        System.out.println("Built a standard game");

        return game;
    }
}
