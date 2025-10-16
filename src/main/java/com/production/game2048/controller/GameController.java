package com.production.game2048.controller;

import com.production.game2048.model.GameState;
import com.production.game2048.model.MoveDirection;
import com.production.game2048.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/games")
public class GameController {

    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }


    @PostMapping
    public ResponseEntity<GameState> startNewGame(
            @RequestParam(defaultValue = "4") int boardSize) {
        // Input validation is handled by the service layer, which will throw
        // an IllegalArgumentException for non-positive sizes.
        GameState newGame = gameService.startNewGame(boardSize);
        return ResponseEntity.ok(newGame);
    }


    @GetMapping("/{id}")
    public ResponseEntity<GameState> getGameState(@PathVariable Long id) {
        GameState gameState = gameService.getGameState(id);
        return ResponseEntity.ok(gameState);
    }


    @PostMapping("/{id}/move")
    public ResponseEntity<GameState> move(
            @PathVariable Long id,
            @RequestParam MoveDirection direction) {
        // Spring Boot automatically converts the request parameter string (e.g., "UP")
        // to the MoveDirection enum, throwing an error for invalid values.
        GameState updatedGame = gameService.move(id, direction);
        return ResponseEntity.ok(updatedGame);
    }
}