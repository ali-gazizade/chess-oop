package com.gazizade.chess.engine.records;

import com.gazizade.chess.engine.Player;
import com.gazizade.chess.engine.enums.GameSituationResult;

public record GameSituation (GameSituationResult result, Player byPlayer) {}
