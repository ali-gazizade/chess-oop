package com.gazizade.chess.dto;

import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.stereotype.Component;

import com.gazizade.chess.engine.Game;
import com.gazizade.chess.engine.enums.SquareState;
import com.gazizade.chess.engine.pieces.Bishop;
import com.gazizade.chess.engine.pieces.King;
import com.gazizade.chess.engine.pieces.Knight;
import com.gazizade.chess.engine.pieces.Pawn;
import com.gazizade.chess.engine.pieces.Queen;
import com.gazizade.chess.engine.pieces.Rook;

@Component
public class GameDTOMapper {
    public GameDTO mapToDTO (Game game) {
        GameDTO gameDTO = new GameDTO();
        ArrayList<SquareDTO> squareDTOs = new ArrayList<>();
        
        gameDTO.setSquares(squareDTOs);

        Arrays.asList(game.getSquares()).forEach(square -> {
            SquareDTO squareDTO = new SquareDTO();

            squareDTO.setPiece(
                square.getPiece() instanceof Pawn
                    ? "Pawn"
                    : square.getPiece() instanceof Knight
                        ? "Knight"
                        : square.getPiece() instanceof Bishop
                            ? "Bishop"
                            : square.getPiece() instanceof Rook
                                ? "Rook"
                                : square.getPiece() instanceof Queen
                                    ? "Queen"
                                    : square.getPiece() instanceof King
                                        ? "King"
                                        : null
            );

            if (square.getPiece() != null) {
                squareDTO.setPieceColor(square.getPiece().getPlayer().getName());
            }

            squareDTO.setState(
                square.getState() == SquareState.CAN_BE_MOVED_TO
                    ? "CAN_BE_MOVED_TO"
                    : square.getState() == SquareState.PIECE_CAN_BE_TAKEN
                        ? "PIECE_CAN_BE_TAKEN"
                        : square.getState() == SquareState.OTHER
                            ? "OTHER"
                            : null
            );

            squareDTO.setColumn((char) ('a' + square.getPosition().x()));
            squareDTO.setRow(square.getPosition().y() + 1);

            squareDTOs.add(squareDTO);
        });

        return gameDTO;
    }
}
