package com.example.chess;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.example.chess.ComputerPlayer.ComputerPlayer;
import com.example.chess.Engine.Game;
import com.example.chess.Engine.GameBuilder;

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
