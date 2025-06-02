# Tic-Tac-Toe (Multiplayer over Sockets)

## Requirements

This application is a two-player Tic-Tac-Toe game that runs entirely from the command line. It uses Java socket programming to enable communication between two players over a network. One player acts as the server (host), and the other connects as a client. The game enforces turn-based play and includes all standard game rules.

## Features:

- Users can host or join a game via terminal.

- The game board is rendered in the CLI and updates in real time for both players.

- Players take turns entering their moves by choosing board positions (e.g., 1–9 or using a grid format).

- The game validates each move (e.g., prevents overwriting an occupied cell).

- The application checks for win conditions (rows, columns, diagonals) or a draw after each turn.

- Displays the game result (win/draw) and gracefully closes the connection afterward.

- Clean exit and error handling in case of disconnection or invalid input.

## Technical Highlights

- Java Sockets (TCP): Implements client-server communication using TCP sockets to exchange moves.

-Game Logic: Modular structure with clear separation between networking logic and game mechanics (e.g., board state, win conditions).

- Input Validation: Ensures robustness against invalid moves and unexpected inputs.

- CLI User Experience: Clean interface with numbered grid, prompts for turns, and feedback on each action.

## Learning Outcomes

- Understand the fundamentals of network programming with sockets in Java.

- Gain experience in designing turn-based multiplayer logic.

- Practice writing clean, modular code with proper separation of concerns.

- Build confidence working with the command line and real-time input/output handling.

- Develop better exception handling for networked applications.
