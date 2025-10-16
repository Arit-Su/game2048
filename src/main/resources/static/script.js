document.addEventListener('DOMContentLoaded', () => {
    // DOM Element References
    const boardElement = document.getElementById('game-board');
    const scoreElement = document.getElementById('score');
    const boardSizeInput = document.getElementById('board-size-input');
    const newGameBtn = document.getElementById('new-game-btn');
    const gameOverOverlay = document.getElementById('game-over-overlay');
    const restartBtn = document.getElementById('restart-btn');
    
    const API_BASE_URL = '/api/games';
    let currentGameId = null;

    /**
     * Renders the game board based on the state received from the API.
     * Dynamically adjusts CSS variables for board size.
     */
    const renderBoard = (gameState) => {
        const board = gameState.board;
        const boardSize = board.length;

        // --- DYNAMIC CSS INJECTION ---
        // Set the CSS variable for the grid layout.
        boardElement.style.setProperty('--board-size', boardSize);
        // --- END DYNAMIC CSS ---

        boardElement.innerHTML = ''; // Clear previous state

        board.forEach(row => {
            row.forEach(cellValue => {
                const tile = document.createElement('div');
                tile.classList.add('tile');
                if (cellValue > 0) {
                    tile.textContent = cellValue;
                    tile.dataset.value = cellValue; // Use data-attribute for styling
                }
                boardElement.appendChild(tile);
            });
        });

        scoreElement.textContent = gameState.score;
        
        if (gameState.gameOver) {
            gameOverOverlay.classList.remove('hidden');
        } else {
            gameOverOverlay.classList.add('hidden');
        }
    };

    /**
     * Starts a new game by calling the backend API with the selected board size.
     */
    const startNewGame = async () => {
        const boardSize = boardSizeInput.value;
        if (boardSize < 3 || boardSize > 10) {
            alert("Please select a board size between 3 and 10.");
            return;
        }

        try {
            // Use URLSearchParams to correctly format the query parameter.
            const params = new URLSearchParams({ boardSize: boardSize });
            const response = await fetch(`${API_BASE_URL}?${params}`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
            });

            if (!response.ok) {
                // Try to parse the error message from the backend
                const errorData = await response.json();
                throw new Error(errorData.message || 'Failed to start a new game.');
            }

            const gameState = await response.json();
            currentGameId = gameState.id;
            renderBoard(gameState);
        } catch (error) {
            console.error(error);
            alert(`Error: ${error.message}`);
        }
    };

    /**
     * Sends a move command to the backend API.
     */
    const makeMove = async (direction) => {
        if (!currentGameId || !gameOverOverlay.classList.contains('hidden')) {
            return; // Don't allow moves if game is over or not started
        }

        try {
            const response = await fetch(`${API_BASE_URL}/${currentGameId}/move?direction=${direction}`, {
                method: 'POST',
            });
            if (!response.ok) {
                throw new Error(`Move failed with status: ${response.status}`);
            }
            const updatedGameState = await response.json();
            renderBoard(updatedGameState);
        } catch (error) {
            console.error('Error making a move:', error);
        }
    };
    
    // Event Listeners
    newGameBtn.addEventListener('click', startNewGame);
    restartBtn.addEventListener('click', startNewGame);

    window.addEventListener('keydown', (e) => {
        e.preventDefault(); // Prevent page scrolling with arrow keys
        switch (e.key) {
            case 'ArrowUp':
                makeMove('UP');
                break;
            case 'ArrowDown':
                makeMove('DOWN');
                break;
            case 'ArrowLeft':
                makeMove('LEFT');
                break;
            case 'ArrowRight':
                makeMove('RIGHT');
                break;
        }
    });

    // Initial game start when the page loads
    startNewGame();
});