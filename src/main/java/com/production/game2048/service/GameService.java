package com.production.game2048.service;

import com.production.game2048.model.GameState;
import com.production.game2048.model.MoveDirection;


public interface GameService {

  
    GameState startNewGame(int boardSize);

 
    GameState getGameState(Long id);

    
    GameState move(Long id, MoveDirection direction);
}