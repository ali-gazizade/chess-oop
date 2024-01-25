package com.chess.Controllers;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chess.ComputerPlayer.ComputerPlayer;
import com.chess.Engine.Game;
import com.chess.Engine.Square;
import com.chess.dto.GameDTO;
import com.chess.dto.GameDTOMapper;

@RestController
@RequestMapping("/game")
public class GameController {
    private final Game game;
    private final GameDTOMapper gameDTOMapper;
    private final ComputerPlayer computerPlayer;

    @Autowired
    public GameController (Game game, GameDTOMapper gameDTOMapper, ComputerPlayer computerPlayer) {
        this.game = game;
        this.gameDTOMapper = gameDTOMapper;
        this.computerPlayer = computerPlayer;
    }

    @GetMapping("/data")
    public GameDTO data () {
        return gameDTOMapper.mapToDTO(game);
    }

    @PostMapping("/select")
    public GameDTO select(@RequestParam char column, @RequestParam int row) {
        Square square = Arrays.stream(game.getSquares()).filter(e ->
            e.getPosition().x() == column - 'a' &&
            e.getPosition().y() == row - 1
        ).findFirst().orElse(null);

        if (square == null) {
            System.out.println("No such a square");

            return null;
        }

        try {
            square.select();

            if (game.getCurrentMove() == null) { // Computer playing
                computerPlayer.makeMove(game);
            }
        } catch (Exception exception) {
            System.out.println(exception);
        }

        return gameDTOMapper.mapToDTO(game);
    }
}
