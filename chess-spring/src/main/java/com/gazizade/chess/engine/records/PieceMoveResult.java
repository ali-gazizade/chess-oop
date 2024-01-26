package com.gazizade.chess.engine.records;

import java.util.ArrayList;

import com.gazizade.chess.engine.Move;

public record PieceMoveResult(boolean result, ArrayList<Move> additionalMoves) {}
