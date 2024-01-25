package com.chess.Engine.records;

import com.chess.Engine.Player;
import com.chess.Engine.enums.GameSituationResult;

public record GameSituation (GameSituationResult result, Player byPlayer) {}
