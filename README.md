# FindTheCheese
FindTheCheese is a web-based maze game that is built using Spring Boot and written in Java. As a player, your objective is to navigate the maze as a mouse and `FindTheCheese` that is located in an area of the maze, while avoiding being caught by the cats. Collect 5 of the cheese to win the game!

<p align="center">
  <img src="https://github.com/icextreme/findthecheese/blob/main/images/gameplay.png" height="40%" width="40%"/>
</p>


## Getting Started
### Prerequisites
In order to build this application, you will need:
* IntelliJ IDEA
* Java Version 8 (JDK 1.8)
* A web browser

### Building
After cloning or downloading the project, you will need to open it in IntelliJ IDEA. After opening the project, Gradle will automatically be imported and the build will begin. 

## Usage
### Running the program
To run the application, from the navigation bar, go to `FindTheCheese/src/main/java/com.icextreme.findthecheese` and find `Application`. Right click it and press `Run 'Application.main()'`. The local web server will now start.

To play the game, open a web browser of your choice, and go to `http://localhost:8080`.

### Starting a new game
Click the `New Game` button on the top of the web page to start a new game.

### Playing the game
Use the arrow keys on your keyboard to move your character (the mouse). At first, the unexplored areas of the maze are not visible to you, so you need to explore the areas by moving around the maze. After moving around the maze, the maze is visible to you.

### Ending the game
The game ends when you collect all 5 of the cheese, or when your character gets caught by a cat. You can also start another new game by pressing the button.

### Stopping the program
To stop the application, open up IntelliJ IDEA press `CTRL + F2` or press the stop button on the run configuration toolbar. The web server will shut down as well.

## Acknowledgements
Dr. Brian Fraser for providing the Gradle build scripts and initial interface.
