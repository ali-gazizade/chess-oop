package com.example.chess.Engine.records;

import com.example.chess.Engine.Player;
import com.example.chess.Engine.enums.GameSituationResult;

public record GameSituation (GameSituationResult result, Player byPlayer) {}
