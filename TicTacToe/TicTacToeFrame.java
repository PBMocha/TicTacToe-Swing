package TicTacToe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class TicTacToeFrame extends JFrame  {

    private static final int WIDTH = 1000;
    private static final int HEIGHT = 1000;

    private static final int CENTER_WIDTH = 600;
    private static final int CENTER_HEIGHT = 600;

    private static final int WEST_WIDTH = 200;
    private static final int WEST_HEIGHT = 1000;

    private static final int EAST_WIDTH = 200;
    private static final int EAST_HEIGHT = 1000;

    private JPanel gameBoard;
    private JPanel menuPanel;
    private JPanel eastPanel;

    private JButton[] placementLoc = new JButton[9];
    private static String[][] gameGrid = new String[3][3];
    private static int filledLoc = 0;

    //Need to do
    private JButton reset;
    private JButton startButton;
    private String playerSymbol;
    private String botSymbol;

    private JLabel turnLabel;

    private boolean playerTurn;
    private boolean winnerExists = false;
    private String symbolWinner;

    public TicTacToeFrame(int closeOperation) {

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setMaximumSize(new Dimension(WIDTH, HEIGHT));
        setMinimumSize(new Dimension(500, 500));

        setDefaultCloseOperation(closeOperation);
        setVisible(true);
        setResizable(true);
        setLayout(new BorderLayout());
        centerComponents();
        eastComponents();
        westComponents();
        pack();
    }

    //Where the game board resides
    private void centerComponents() {

        int rowSize = gameGrid.length;
        int colSize = gameGrid[0].length;

        for (int row = 0; row < rowSize; row++)
            for (int col = 0; col < colSize; col++)
                if (gameGrid[row][col] == null)
                    gameGrid[row][col] = "";


        gameBoard = new JPanel(new GridLayout(3,3, 10, 10));
        gameBoard.setBackground(Color.BLACK);

        //Creates the game grid in buttons.
        for (int i = 0; i < placementLoc.length; i++) {
            placementLoc[i] = new JButton();
            placementLoc[i].setBackground(Color.WHITE);
            placementLoc[i].setActionCommand(String.valueOf(i));
            placementLoc[i].addActionListener(new PlacementListener());
            placementLoc[i].setFont(new Font("Courier New", Font.BOLD, 50));
            placementLoc[i].setEnabled(false);
            placementLoc[i].setSize(200, 200);
            gameBoard.add(placementLoc[i]);
        }

        gameBoard.setPreferredSize(new Dimension(CENTER_WIDTH, CENTER_HEIGHT));


        add(gameBoard, BorderLayout.CENTER);
    }

    //Game information; Which player's turn
    private void eastComponents() {

        eastPanel = new JPanel();
        eastPanel.setPreferredSize(new Dimension(EAST_WIDTH, EAST_HEIGHT));

        turnLabel = new JLabel("Welcome To TicTacToe!");
        eastPanel.add(turnLabel);
        add(eastPanel, BorderLayout.EAST);

    }

    private void westComponents() {

        menuPanel = new JPanel();
        menuPanel.setPreferredSize(new Dimension(WEST_WIDTH, WEST_HEIGHT));
        startButton = new JButton("Begin");
        startButton.addActionListener((ActionEvent e) -> {
                JButton thisButton = (JButton)e.getSource();
                thisButton.setEnabled(false);
                for (Component component : gameBoard.getComponents())
                    component.setEnabled(true);

                //If player selects team X, bot goes first
                if (!playerTurn) {
                    botPlaceSymbol();
                }
        });

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener((ActionEvent e) -> System.exit(0));

        ButtonGroup teamSelection = new ButtonGroup();
        JRadioButton selectTeamX = new JRadioButton("Team X");
        JRadioButton selectTeamO = new JRadioButton("Team O");
        selectTeamO.setSelected(true);
        selectTeamO.doClick();

        selectTeamO.addActionListener((ActionEvent e) -> {
            System.out.println("Selected team O");
            playerSymbol = "O";
            botSymbol = "X";
            playerTurn = true;

        });

        selectTeamX.addActionListener((ActionEvent e) -> {
            System.out.println("Selected team X");
            playerSymbol = "X";
            botSymbol = "O";
            playerTurn = false;

        });

        teamSelection.add(selectTeamO);
        teamSelection.add(selectTeamX);

        menuPanel.add(startButton);
        menuPanel.add(exitButton);
        menuPanel.add(selectTeamO);
        menuPanel.add(selectTeamX);
        add(menuPanel, BorderLayout.WEST);
    }

    private void botPlaceSymbol() {
        Random randGen = new Random();
        boolean isLocationExist = false;

        //Check if there is at least one enabled button on the grid.
        for (JButton location : placementLoc) {
            if (location.isEnabled()) {
                isLocationExist = true;
                break;
            }
        }

        //Place bot symbol in any random enabled location.
        while(isLocationExist) {
            int botPlaceLoc = randGen.nextInt(9);

            if (placementLoc[botPlaceLoc].isEnabled()) {
                //Click a button
                placementLoc[botPlaceLoc].doClick();
                break;
            }
        }
    }

    //Event Listeners

    //When a button is clicked, set button to the corresponding symbol of the player.
    //Check if each player's placement wins the game.
    private class PlacementListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            JButton buttonSource = (JButton) e.getSource();

            if (playerTurn) {

                placeAtLocation(e, playerSymbol);

                //Sets bot's turn
                playerTurn = false;
                buttonSource.setBackground(Color.BLUE);
                turnLabel.setText("Cross Player's Turn");
                botPlaceSymbol();

            } else {

                placeAtLocation(e, botSymbol);
                buttonSource.setBackground(Color.RED);
                turnLabel.setText("Circle Player's Turn");
                playerTurn = true;

            }

            //Todo

            filledLoc++;

            if (winnerExists) {

                turnLabel.setText(symbolWinner + " is the Winner!!");

            } else if (filledLoc == placementLoc.length) {
                turnLabel.setText("No more possible locations.Game is a tie.");

            }

        }

        //Testing different method of placing locations using action command
        //instead of relying on parameters on the creating of the listener.
        private void placeAtLocation(ActionEvent e, String symbol) {
            System.out.println("Button at loc " + e.getActionCommand() +
                    "| Placed an " + symbol);
            if (e.getSource() instanceof JButton) {
                ((JButton) e.getSource()).setText(symbol);
                ((JButton) e.getSource()).setEnabled(false);

                int locationInteger = Integer.parseInt(e.getActionCommand());

                switch (locationInteger) {

                    case 0:
                        gameGrid[0][0] = symbol;
                        break;
                    case 1:
                        gameGrid[0][1] = symbol;
                        break;
                    case 2:
                        gameGrid[0][2] = symbol;
                        break;
                    case 3:
                        gameGrid[1][0] = symbol;
                        break;
                    case 4:
                        gameGrid[1][1] = symbol;
                        break;
                    case 5:
                        gameGrid[1][2] = symbol;
                        break;
                    case 6:
                        gameGrid[2][0] = symbol;
                        break;
                    case 7:
                        gameGrid[2][1] = symbol;
                        break;
                    case 8:
                        gameGrid[2][2] = symbol;
                        break;
                }
            }

            if (isWinner(symbol)) {
                System.out.println(symbol + " is the winner!!");

                for (JButton button : placementLoc)
                    button.setEnabled(false);

                winnerExists = true;
                symbolWinner = symbol;
            }
        }

    }

    //static only temporary for testing. When completed, remove the static
    private static boolean isWinner(String symbol) {

        int rowsize = gameGrid.length;
        int colsize = gameGrid[0].length;

        for (int row = 0; row < rowsize; row++) {
            for (int col = 0; col < colsize; col++)
                if (gameGrid[row][col] == null)
                    gameGrid[row][col] = "";
        }

        //Check all rows for win condition
        for (int row = 0; row < rowsize; row++) {
            for (int col = 0; col < colsize; col++) {
                if (!gameGrid[row][col].equals(symbol))
                    break;
                if (col == colsize - 1)
                    return true;
            }
        }

        //Check all columns
        for (int col = 0; col < colsize; col++) {
            for (int row = 0; row < rowsize; row++) {
                if (!gameGrid[row][col].equals(symbol))
                    break;
                if (row == rowsize - 1)
                    return true;
            }
        }

        //Check all diagonals
        for (int dia = 0; dia < gameGrid.length; dia++) {
            if (!gameGrid[dia][dia].equals(symbol))
                break;
            if (dia == gameGrid.length-1)
                return true;
        }

        for (int row = 2, col = 0; row > -1; row--, col++) {
            if(!gameGrid[row][col].equals(symbol))
                break;
            if (row == 0)
                return true;
        }

        return false;
    }

    public static void main(String[] args) {

        new TicTacToeFrame(JFrame.EXIT_ON_CLOSE);
    }

}
