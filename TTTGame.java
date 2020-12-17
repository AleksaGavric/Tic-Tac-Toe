import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TTTGame extends JFrame {

    //grid
    private static int cellSize = 100;
    private static int layoutWidth = 300;
    private static int layoutHeight = 400;
    private static int gridWidth = 8;
    //stats
    private static int P1wins = 0;
    private static int P2wins = 0;
    private static int draws = 0;
    //labels
    private JLabel stat1;
    private JLabel stat2;
    private JLabel stat3;
    private JLabel message;
    //other
    private results currentSt;
    private action currentPl;
    private action[][] board;
    private Font smallerFont;

    //possible results
    private enum results {
        play, P1win, P2win, draw
    }

    //possible actions
    private enum action {
        P1turn, P2turn, notPlayed
    }

    //restarts game
    private void restartGame() {
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 3; ++col) {
                board[row][col] = action.notPlayed;
            }
        }

        currentSt = results.play;
        currentPl = action.P1turn;
    }

    //returns if the move is valid
    private boolean validMove(int row, int col) {
        boolean validMove = (row >= 0 && row < 3) && (col >= 0 && col < 3);
        boolean notPlayedYet = (board[row][col] == action.notPlayed);

        return validMove && notPlayedYet;
    }

    //changes turn
    private void turnPlay(int activeRow, int activeCol) {
        if (currentSt == results.play) {
            if (validMove(activeRow,activeCol) == true) {
                updateState(currentPl, activeRow, activeCol);
                switchTurn();
            }
        }
    }

    //switches turns
    private void switchTurn() {
        if (currentPl == action.P1turn) {
            currentPl = action.P2turn;
        } else {
            currentPl = action.P1turn;
        }
    }

    //updates the state of the game, updates stats
    private void updateState(action spot, int activeRow, int activeCol) {
        board[activeRow][activeCol] = currentPl;

        if (isVictory(spot, activeRow, activeCol) == true) {
            if (spot == action.P1turn) {
                currentSt = results.P1win;
                P1wins++;
            } else {
                currentSt = results.P2win;
                P2wins++;
            }

        } else if (isDraw() == true) {
            currentSt = results.draw;
            draws++;
        }
    }

    //returns true if its a draw
    private boolean isDraw() {
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 3; ++col) {
                if (board[row][col] == action.notPlayed) {
                    return false;
                }
            }
        }

        return true;
    }

    //returns true if there is a victory condition
    private boolean isVictory(action theMark, int activeRow, int activeCol) {
        boolean rowWon = (board[activeRow][0] == theMark && board[activeRow][1] == theMark && board[activeRow][2] == theMark);
        boolean colWon = (board[0][activeCol] == theMark && board[1][activeCol] == theMark && board[2][activeCol] == theMark);
        boolean diagonalWon = (board[0][0] == theMark && board[1][1] == theMark && board[2][2] == theMark) || (board[0][2] == theMark
                        && board[1][1] == theMark && board[2][0] == theMark);

        return rowWon || colWon || diagonalWon;
    }

    //draws a JPanel with the grid
    private class drawGrid extends JPanel {

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D)g;

            for (int col = 1; col < 3; ++col) {
                g.fillRoundRect(cellSize * col - (gridWidth / 2), 0,
                        gridWidth, layoutHeight-1, gridWidth, gridWidth);
            }

            for (int row = 1; row < 3; ++row) {
                g.fillRoundRect(0, cellSize * row - gridWidth / 2,
                        layoutWidth -1, gridWidth, gridWidth, gridWidth);
            }

            for (int row = 0; row < 3; ++row) {
                for (int col = 0; col < 3; ++col) {
                    int x1 = col * cellSize + 15;
                    int y1 = row * cellSize + 15;

                    if (board[row][col] == action.P1turn) {
                        int x2 = (col + 1) * cellSize - 15;
                        int y2 = (row + 1) * cellSize - 15;
                        g2d.drawLine(x1, y1, x2, y2);
                        g2d.drawLine(x2, y1, x1, y2);

                    } else if (board[row][col] == action.P2turn) {
                        g2d.drawOval(x1, y1, 70, 70);

                    }
                }
            }

            stat1.setText("P1 wins: " + P1wins);
            stat2.setText("P2 wins: " + P2wins);
            stat3.setText("Draws: " + draws);

            if (currentSt == results.play) {
                if (currentPl == action.P1turn) {
                    message.setText("P1's turn");
                } else {
                    message.setText("P2's turn");
                }

            } else if (currentSt == results.draw) {
                message.setText("Draw!");

            } else if (currentSt == results.P1win) {
                message.setText("P1 Wins!");

            } else if (currentSt == results.P2win) {
                message.setText("P2 Wins!");
            }
        }
    }

    //draws a JFrame, adds buttons, puts everything together
    private TTTGame() {
        JFrame game = new JFrame();
        BoxLayout boxLayout = new BoxLayout(game.getContentPane(), BoxLayout.Y_AXIS);

        game.setSize(310, 430);
        game.setLayout(boxLayout);

        smallerFont = new Font("Serif", Font.PLAIN, 11);

        Dimension size1 = new Dimension(300, 100); //for JPanel
        Dimension size2 = new Dimension(88, 100); //for buttons

        JButton b1 = new JButton("New Game");
        b1.setFont(smallerFont);
        b1.setPreferredSize(size2);

        JButton b2 = new JButton();
        b2.setFont(smallerFont);
        b2.setPreferredSize(size2);
        b2.setAlignmentY(JButton.CENTER_ALIGNMENT);

        JButton b3 = new JButton();
        b3.setFont(smallerFont);
        b3.setPreferredSize(size2);

        JPanel lower = new JPanel();
        JPanel insideButton = new JPanel();
        insideButton.setOpaque(false);

        lower.setMaximumSize(size1);
        lower.add(b1);
        lower.add(b2);
        lower.add(b3);

        JPanel TTT = new drawGrid();
        TTT.setPreferredSize(new Dimension(300, 400));

        TTT.addMouseListener(new MouseAdapter() {
            //gets x, y of mouse clicks on grid, paints X or O there
            @Override
            public void mouseClicked(MouseEvent e) {
                int positionX = e.getX();
                int positionY = e.getY();

                int rowSelected = positionY / cellSize;
                int colSelected = positionX / cellSize;

                turnPlay(rowSelected, colSelected);
                TTT.repaint();
            }
        });

        b1.addMouseListener(new MouseAdapter() {
            //gets mouse click on Start Game button, restarts game
            @Override
            public void mouseClicked(MouseEvent e) {
                restartGame();

                TTT.repaint();
            }
        });

        //whose turn it is
        message = new JLabel("");
        message.setFont(smallerFont);
        message.setAlignmentX(JButton.CENTER_ALIGNMENT);

        game.setTitle("Tic-Tac-Toe by Aleksa!");
        game.add(TTT);
        game.add(lower);
        game.setVisible(true);
        game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        b2.add(message);

        //stats to display in the third button
        stat1 = new JLabel();
        stat1.setFont(smallerFont);
        stat2 = new JLabel();
        stat2.setFont(smallerFont);
        stat3 = new JLabel();
        stat3.setFont(smallerFont);

        insideButton.add(stat1);
        insideButton.add(stat2);
        insideButton.add(stat3);
        insideButton.setAlignmentX(JButton.CENTER_ALIGNMENT);

        b3.add(insideButton);

        board = new action[3][3];
        restartGame();
    }

    //runs TTTGame
    public static void main(String[] args) {
        new TTTGame();
    }

}