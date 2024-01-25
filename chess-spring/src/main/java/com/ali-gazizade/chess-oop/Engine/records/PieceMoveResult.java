package com.example.chess.Engine.records;

import java.util.ArrayList;

import com.example.chess.Engine.Move;

public record PieceMoveResult(boolean result, ArrayList<Move> additionalMoves) {}
