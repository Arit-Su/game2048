2048 Game Implementation
This is a web-based implementation of the classic 2048 puzzle game, built with a Java Spring Boot backend and a vanilla JavaScript frontend. The application is designed to be robust, maintainable, and easy to run.
Installation & Running
Requirements
Java Development Kit (JDK) 8 or newer
Apache Maven
A modern web browser (e.g., Chrome, Firefox, Edge)
Running the Application
Clone the Repository
code
Sh
git clone https://github.com/your-username/game2048.git
cd game2048
Run with Maven
Use the Maven wrapper to build and run the Spring Boot application.
On macOS / Linux:
code
Sh
./mvnw spring-boot:run
On Windows:
code
Sh
mvnw.cmd spring-boot:run
Play the Game
Once the application has started, open your web browser and navigate to:
http://localhost:8080
Accessing the Database (Optional)
This project uses an H2 file-based database to persist game state. You can access its console for debugging purposes.
Navigate to http://localhost:8080/h2-console.
Use the following JDBC URL to connect: jdbc:h2:file:./data/game2048db
Enter sa for the User Name and leave the password blank.
How to Play
Objective: The goal is to slide tiles on the grid to combine tiles of the same number and create a tile with the value 2048.
Controls: Use the arrow keys (Up, Down, Left, Right) on your keyboard to move all tiles on the board in that direction.
Mechanics:
When two tiles with the same number touch, they merge into a single tile with their sum.
After every valid move, a new tile (either a 2 or a 4) will appear in a random empty spot.
Configuration: You can change the board size (from 3x3 to 10x10) using the input field and start a new game at any time by clicking the New Game button.
Game End: The game is over when the board is completely full and no more merges are possible.
Implementation Details
Backend
Framework: Spring Boot (Java 8)
Architecture: The backend follows a classic 3-tier architecture (Controller, Service, Repository) to ensure a clean separation of concerns.
API: A stateless RESTful API is exposed to manage game state.
Database: An H2 file-based database is used for persistence. This ensures that a game's state is not lost if the application is restarted.
Data Persistence: Spring Data JPA is used for interacting with the database. A custom AttributeConverter cleanly serializes the 2D integer array of the game board into a database-friendly String format.
Game Logic: All game logic, including tile movement, merging, and spawning, is encapsulated within the GameService layer. The logic cleverly handles all four move directions by transforming the board state and applying a single, canonical moveLeft operation, which significantly reduces code duplication.
Frontend
Technology: Vanilla JavaScript (ES6+), HTML5, and CSS3. No external frameworks are used, keeping the frontend lightweight and simple.
Deployment: The frontend is served as static content directly from the embedded web server in the Spring Boot application, creating a single, self-contained, deployable artifact.
Dynamic UI:
The JavaScript frontend communicates with the backend via the fetch API, making asynchronous calls to the REST endpoints.
The game board is rendered dynamically based on the JSON response from the server.
CSS Custom Properties (variables) are updated via JavaScript to allow the board's grid layout and tile sizes to adapt to the configurable board size.
API Endpoints
The following REST endpoints are available:
POST /api/games
Starts a new game.
Query Parameter: boardSize (optional, default: 4).
Returns: The initial GameState object as JSON.
GET /api/games/{id}
Retrieves the current state of a specific game.
Returns: The current GameState object as JSON.
POST /api/games/{id}/move
Submits a move for a specific game.
Query Parameter: direction (required, e.g., UP, DOWN, LEFT, RIGHT).
Returns: The updated GameState object as JSON.
