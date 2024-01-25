package com.chess.Engine.records;

import java.util.ArrayList;

import com.chess.Engine.Move;

public record PieceMoveResult(boolean result, ArrayList<Move> additionalMoves) {}
