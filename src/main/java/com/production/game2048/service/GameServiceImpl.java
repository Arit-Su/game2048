package com.production.game2048.service;

import com.production.game2048.exception.GameNotFoundException;
import com.production.game2048.model.GameState;
import com.production.game2048.model.MoveDirection;
import com.production.game2048.repository.GameStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


@Service
public class GameServiceImpl implements GameService {

    private static final int WINNING_TILE = 2048;

    private final GameStateRepository gameStateRepository;
    private final Random random = new Random();

    @Autowired
    public GameServiceImpl(GameStateRepository gameStateRepository) {
        this.gameStateRepository = gameStateRepository;
    }

    @Override
    @Transactional
    public GameState startNewGame(int boardSize) {
        if (boardSize <= 0) {
            throw new IllegalArgumentException("Board size must be positive.");
        }
        GameState newGame = new GameState();
        newGame.setBoard(new int[boardSize][boardSize]);
        newGame.setScore(0);
        newGame.setGameOver(false);
        newGame.setWon(false);

        // Start with two random tiles
        addRandomTile(newGame.getBoard());
        addRandomTile(newGame.getBoard());

        return gameStateRepository.save(newGame);
    }

    @Override
    @Transactional(readOnly = true)
    public GameState getGameState(Long id) {
        return gameStateRepository.findById(id)
                .orElseThrow(() -> new GameNotFoundException("Game with ID " + id + " not found."));
    }

    @Override
    @Transactional
    public GameState move(Long id, MoveDirection direction) {
        GameState gameState = getGameState(id);

        if (gameState.isGameOver()) {
            return gameState;
        }

        int[][] board = gameState.getBoard();
        int boardSize = board.length;
      
        int[][] boardBeforeMove = Arrays.stream(board).map(int[]::clone).toArray(int[][]::new);
        
        int scoreFromMove = 0;


        switch (direction) {
            case LEFT:
                scoreFromMove = moveLeft(board);
                break;
            case RIGHT:
                reverseRows(board);
                scoreFromMove = moveLeft(board);
                reverseRows(board);
                break;
            case UP:
                transpose(board);
                scoreFromMove = moveLeft(board);
                transpose(board);
                break;
            case DOWN:
                transpose(board);
                reverseRows(board);
                scoreFromMove = moveLeft(board);
                reverseRows(board);
                transpose(board);
                break;
        }

       
        if (!Arrays.deepEquals(board, boardBeforeMove)) {
            gameState.setScore(gameState.getScore() + scoreFromMove);
            addRandomTile(board);

            
            if (!gameState.isWon() && hasTile(board, WINNING_TILE)) {
                gameState.setWon(true);
               
            }

            if (!isMovePossible(board)) {
                gameState.setGameOver(true);
            }
        }
        
        gameState.setBoard(board);
        return gameStateRepository.save(gameState);
    }


    private void addRandomTile(int[][] board) {
        List<int[]> emptyCells = new ArrayList<>();
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                if (board[r][c] == 0) {
                    emptyCells.add(new int[]{r, c});
                }
            }
        }

        if (!emptyCells.isEmpty()) {
            int[] cell = emptyCells.get(random.nextInt(emptyCells.size()));
            // 90% chance of 2, 10% chance of 4
            board[cell[0]][cell[1]] = random.nextInt(10) == 0 ? 4 : 2;
        }
    }


    private int moveLeft(int[][] board) {
        int score = 0;
        for (int r = 0; r < board.length; r++) {
            int[] row = board[r];
           
            List<Integer> nonZeroTiles = Arrays.stream(row).filter(tile -> tile != 0).boxed().collect(Collectors.toList());

            
            List<Integer> mergedTiles = new ArrayList<>();
            for (int i = 0; i < nonZeroTiles.size(); i++) {
                if (i + 1 < nonZeroTiles.size() && nonZeroTiles.get(i).equals(nonZeroTiles.get(i + 1))) {
                    int mergedValue = nonZeroTiles.get(i) * 2;
                    mergedTiles.add(mergedValue);
                    score += mergedValue;
                    i++; 
                } else {
                    mergedTiles.add(nonZeroTiles.get(i));
                }
            }

           
            int[] newRow = new int[board.length];
            for (int i = 0; i < mergedTiles.size(); i++) {
                newRow[i] = mergedTiles.get(i);
            }
            board[r] = newRow;
        }
        return score;
    }


    private void transpose(int[][] board) {
        for (int r = 0; r < board.length; r++) {
            for (int c = r + 1; c < board.length; c++) {
                int temp = board[r][c];
                board[r][c] = board[c][r];
                board[c][r] = temp;
            }
        }
    }


    private void reverseRows(int[][] board) {
        for (int r = 0; r < board.length; r++) {
            int left = 0;
            int right = board[r].length - 1;
            while (left < right) {
                int temp = board[r][left];
                board[r][left] = board[r][right];
                board[r][right] = temp;
                left++;
                right--;
            }
        }
    }


    private boolean isMovePossible(int[][] board) {
        int size = board.length;
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (board[r][c] == 0) {
                    return true; 
                }
                
                if (c < size - 1 && board[r][c] == board[r][c + 1]) {
                    return true;
                }
                
                if (r < size - 1 && board[r][c] == board[r + 1][c]) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hasTile(int[][] board, int tileValue) {
        return Arrays.stream(board).flatMapToInt(Arrays::stream).anyMatch(tile -> tile == tileValue);
    }
}