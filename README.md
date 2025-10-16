# 2048 Game - Spring Boot Implementation

This repository contains a full-stack, production-ready implementation of the classic game 2048. The backend is built with Java and Spring Boot, providing a RESTful API to manage the game state. The frontend is a clean, responsive single-page application built with vanilla HTML, CSS, and JavaScript, served directly from the Spring Boot application.

## Features

*   **Classic 2048 Gameplay:** Slide and merge tiles to reach the 2048 tile.
*   **Configurable Board Size:** Start a new game with a board of any size (e.g., 3x3, 4x4, 5x5).
*   **Persistent Game State:** Your game is saved to a file-based database, so you can stop and resume your session later.
*   **Dynamic UI:** The frontend dynamically renders the board and tiles based on the chosen size.
*   **Scoring:** Your score increases as you merge tiles.
*   **Game Over Detection:** The game correctly identifies when no more moves are possible.

---

## Gameplay Instructions

1.  **Objective:** The goal of the game is to combine tiles with the same number to create a tile with the value **2048**.
2.  **Controls:** Use the **arrow keys** (`↑`, `↓`, `←`, `→`) on your keyboard to move all tiles on the board in that direction.
3.  **Merging:** When two tiles with the same number touch as a result of a move, they will merge into a single tile with their sum.
4.  **New Tiles:** After every successful move, a new tile (either a '2' or a '4') will appear in a random empty spot on the board.
5.  **Game End:** The game ends when the board is full and there are no more possible merges.

---

## Technical Implementation Details

This application is built following modern software engineering best practices, emphasizing security, robustness, and maintainability.

### Architecture

The application follows a classic three-tier architecture:

1.  **Presentation Layer (Frontend):** A lightweight UI built with vanilla **HTML5**, **CSS3**, and **JavaScript (ES6+)**. It is packaged as static content and served directly by the embedded Spring Boot web server. The UI is dynamic, using CSS Custom Properties to render the board based on the size provided by the backend.
2.  **Service Layer (Backend):** A stateless RESTful API built with **Java 8** and the **Spring Boot** framework. It contains all the core business logic for the game.
3.  **Data Layer (Persistence):** The game state is persisted using **Spring Data JPA** and an **H2 file-based database**. This ensures that game sessions are not lost when the application restarts. A custom JPA `AttributeConverter` is used to serialize the 2D game board array into a database-friendly string format.

### Key Design Principles

*   **Separation of Concerns:** Logic is cleanly separated into Controllers (API endpoints), Services (business logic), and Repositories (data access).
*   **DRY (Don't Repeat Yourself):** The core game move logic is highly efficient. `UP`, `DOWN`, and `RIGHT` moves are handled by transposing and reversing the board matrix, allowing a single `moveLeft` algorithm to process all four directions.
*   **Robust Error Handling:** A `@ControllerAdvice` global exception handler provides consistent, structured JSON error responses for all API failures.
*   **API Design:** The API follows RESTful conventions for creating and interacting with game resources.

---

## Installation and Running the Game

### Prerequisites

*   **Java Development Kit (JDK) 8** or later.
*   **Apache Maven** (The project includes a Maven wrapper, so a local installation is not strictly required).
*   A modern web browser.

### Running the Application

1.  **Clone the repository:**
    ```bash
    git clone <your-github-repository-url>
    cd game2048
    ```

2.  **Run the application using the Maven wrapper:**
    *   On macOS or Linux:
        ```bash
        ./mvnw spring-boot:run
        ```
    *   On Windows:
        ```bash
        mvnw.cmd spring-boot:run
        ```
    The application will start on the embedded Tomcat server.

3.  **Play the game:**
    Open your web browser and navigate to:
    [**http://localhost:8080**](http://localhost:8080)

4.  **(Optional) Access the H2 Database Console:**
    To inspect the game state directly in the database, navigate to:
    [**http://localhost:8080/h2-console**](http://localhost:8080/h2-console)
    *   **JDBC URL:** `jdbc:h2:file:./data/game2048db`
    *   **User Name:** `sa`
    *   **Password:** (leave empty)
