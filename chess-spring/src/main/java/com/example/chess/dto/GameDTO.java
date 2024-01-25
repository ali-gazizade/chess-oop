package com.example.chess.dto;

import java.util.ArrayList;

public class GameDTO {
    private ArrayList<SquareDTO> squares = new ArrayList<>();

    public ArrayList<SquareDTO> getSquares() {
        return squares;
    }

    public void setSquares(ArrayList<SquareDTO> squares) {
        this.squares = squares;
    }
}
