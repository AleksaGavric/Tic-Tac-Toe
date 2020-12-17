# Tic Tac Toe
 Tic Tac Toe made using Java Graphics
 
 Contains a class called TTTGame which contains global variables (cellSize etc.), one main method which activates the
    TTTGame method, and helper methods which represent various actions (isDraw etc.). The main methods runs the class,
    and each helper method does a specific thing (either updates global variables or returns a value).

    The drawing was done by overriding the Paint component. Everything was put together in a single JFrame as 2 stacked
    JPanels with Box Layout, the upper one containing the grid and the lower one containing the buttons. The grid listens to mouse clicks,
    determining where the click was and if its a valid move. The New Game button also listens to clicks, clears the grid
    leaves statistics unchanged. Further descriptions can be found in the java file.

