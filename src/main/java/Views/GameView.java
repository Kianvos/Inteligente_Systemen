package Views;

import Model.Model;
import Model.Othello;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;


public class GameView extends JPanel {
    // for tictactoe instead of 3 the size will be 9
    // size stands for the entire board size instead of the size of one row
    private int size;
    private JPanel buttonPanel;
    private JButton[] buttons;

    // Just making the GameView nicer to look at
    private JLabel textfield;
    private JLabel textScoreField;
    private JPanel top;

    private CardLayout topRightLayout;
    private JPanel topRight;

    private JButton resetButton;
    private JButton menuButton;
    private JButton disconnectButton;

    private String playerOnePiece;

    private String playerTwoPiece;

    private Color playerOneColor;
    private Color playerTwoColor;
    private Color boardBgColor;

    public GameView(int size) {
        // first construct JPanel before constructing GameView
        super(new BorderLayout());


        this.size = size;
        this.buttons = new JButton[size];

        this.resetButton = new JButton("Reset");
        this.menuButton = new JButton("Menu");
        this.disconnectButton = new JButton("Disconnect");

        setupGameView();
    }

    public void setBoardSize(int size) {
        this.size = size;
        this.buttons = new JButton[this.size];
        remove(this.buttonPanel);
        setupButtons();
    }


    public void setupGameView() {
        textfield = setupTextfield();
        textScoreField = setupTextfield();
        setupTopPanel();
        setupButtons();
    }

    /**
     * Sets up textfield
     */
    public JLabel setupTextfield() {
        JLabel text = new JLabel();
        text.setFont(new Font(text.getFont().toString(), Font.BOLD, 18));
        text.setForeground(new Color(0,0,0));
        text.setBackground(new Color(105, 105, 105));
        text.setHorizontalAlignment(JLabel.CENTER);
        text.setPreferredSize(new Dimension(300, 75));
        text.setOpaque(true);
        return text;
    }

    public void setupTopPanel() {
        this.top = new JPanel(new GridBagLayout());
        top.setSize(500, 150);
        top.setPreferredSize(new Dimension(500, 150));

        JPanel textTop = setupTextPanel();

        textTop.setPreferredSize(new Dimension(300, 140));
        topRight = setupTopRight();
        topRight.setPreferredSize(new Dimension(160, 140));


        top.add(textTop);
        top.add(topRight);
        add(top, BorderLayout.NORTH);
    }

    public JPanel setupTextPanel() {
        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.add(textfield);
        textPanel.add(textScoreField);
        textPanel.setOpaque(true);
        return textPanel;
    }

    public JPanel setupTopRight() {
        topRightLayout = new CardLayout();
        topRight = new JPanel(topRightLayout);
        styleRightTopButton(resetButton);
        styleRightTopButton(menuButton);
        styleRightTopButton(disconnectButton);

        JPanel topRightGame = new JPanel(new GridLayout(2, 1));
        topRightGame.add(resetButton);
        topRightGame.add(menuButton);

        JPanel topRightOnline = new JPanel(new GridLayout(1, 1));
        topRightOnline.add(disconnectButton);

        topRight.add(topRightGame, "local");
        topRight.add(topRightOnline, "online");

        // Default but has to be overwritten by thisView.show("online");
        topRightLayout.show(topRight, "local");

        return topRight;
    }

    /**
     * Hier worden de knoppen ingesteld waar je op moet drukken om een zet te doen.
     */
    public void setupButtons() {
        this.buttonPanel = new JPanel(new GridLayout((int) Math.sqrt(size), (int) Math.sqrt(size)));
        this.buttonPanel.setSize(new Dimension(500, 500));
        this.buttonPanel.setPreferredSize(new Dimension(500, 500));

        for (int i = 0; i < size; i++) {
            buttons[i] = new JButton();
            styleButton(buttons[i]);

            this.buttonPanel.add(buttons[i]);
        }

        add(this.buttonPanel);
    }

    /**
     * Hier worden alle knoppen geupdate op basis van wat er op het vakje staat (x, o)
     *
     * @param model model die beheert over alle game logica
     */
    public void update(Model model) {
        int[] board = model.getBoardData();
        final int PLAYER_ONE = 1;
        final int PLAYER_TWO = 2;
        final int SUGGESTED = 3;
        for (int i = 0; i < size; i++) {

            if (board[i] == PLAYER_ONE) {
                buttons[i].setForeground(playerOneColor);
                buttons[i].setText(playerOnePiece);
                continue;
            }
            if (board[i] == PLAYER_TWO) {
                buttons[i].setForeground(playerTwoColor);
                buttons[i].setText(playerTwoPiece);
                continue;
            }
            if (board[i] == SUGGESTED) {
                buttons[i].setForeground(new Color(150, 150, 150));
                buttons[i].setText("-");
                continue;
            }

            buttons[i].setForeground(new Color(220, 220, 220));
            buttons[i].setText("");
        }


        if (model.isWinner()) {
            textfield.setText(model.getStringWinner() + " heeft gewonnen!");
        } else if (model.isTie()) {
            textfield.setText("Gelijkspel");
        }

        if (model instanceof Othello){
            int scorePlayerOne = Othello.countScore(board, PLAYER_ONE);
            int scorePlayerTwo = Othello.countScore(board, PLAYER_TWO);
            textScoreField.setText("Zwart " + scorePlayerOne + " - " + scorePlayerTwo + " Wit");
        }
    }

    public void show(String panel) {
        topRightLayout.show(topRight, panel);
    }

    /**
     * Styles the button given
     *
     * @param button button
     */
    public void styleButton(JButton button) {
        button.setFocusable(false);
        button.setFont(new Font(button.getFont().toString(), Font.BOLD, 40));
        button.setBackground(boardBgColor);
        button.setOpaque(true);
        button.setBorder(new LineBorder(new Color(0, 26, 9)));
    }

    /**
     * Styles the button given for the topRight panel
     *
     * @param button button
     */
    public void styleRightTopButton(JButton button) {
        button.setFocusable(false);
        button.setFont(new Font(button.getFont().toString(), Font.BOLD, 20));
        button.setBackground(new Color(112, 128, 144));
        button.setPreferredSize(new Dimension(140, 70));
    }

    public void setText(String s) {
        this.textfield.setText(s);
    }

    public void setTextScoreField(JLabel textScoreField) {
        this.textScoreField = textScoreField;
    }

    public JButton[] getButtons() {
        return buttons;
    }

    public JButton getResetButton() {
        return resetButton;
    }

    public JButton getMenuButton() {
        return menuButton;
    }

    public JButton getDisconnectButton() {
        return disconnectButton;
    }

    public String getPlayerOnePiece() {
        return playerOnePiece;
    }

    public void setPlayerOnePiece(String playerOnePiece) {
        this.playerOnePiece = playerOnePiece;
    }

    public String getPlayerTwoPiece() {
        return playerTwoPiece;
    }

    public void setPlayerTwoPiece(String playerTwoPiece) {
        this.playerTwoPiece = playerTwoPiece;
    }

    public Color getBoardBgColor() {
        return boardBgColor;
    }

    public void setBoardBgColor(Color boardBgColor) {
        this.boardBgColor = boardBgColor;
    }

    public Color getPlayerOneColor() {
        return playerOneColor;
    }

    public void setPlayerOneColor(Color playerOneColor) {
        this.playerOneColor = playerOneColor;
    }

    public Color getPlayerTwoColor() {
        return playerTwoColor;
    }

    public void setPlayerTwoColor(Color playerTwoColor) {
        this.playerTwoColor = playerTwoColor;
    }
}
