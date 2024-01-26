package com.gazizade.chess;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.gazizade.chess.computerPlayer.ComputerPlayer;
import com.gazizade.chess.engine.Game;
import com.gazizade.chess.engine.GameBuilder;

@Configuration
public class AppConfig {
    @Bean
    @Scope("singleton")
    public Game game() {
        return (new GameBuilder()).buildStandard();
    }

    @Bean
    @Scope("singleton")
    public ComputerPlayer computerPlayer() {
        return new ComputerPlayer();
    }
}
