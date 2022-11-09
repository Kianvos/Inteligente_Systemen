import javax.swing.*;
import java.awt.*;

public class GUI {
    // private 
    private JFrame frame;
    
    private JLabel textfield;
    private JButton disconnectButton = new JButton("Disconnect");
    private JButton resetButton = new JButton("Reset");
    private JButton menuButton = new JButton("Menu");

    // Cards
    private CardLayout layout = new CardLayout();
    private JPanel rightTop = new JPanel();

    // Game
    private JButton[] buttons;
    private JPanel gamePanel;


    public GUI(int size) {
        this.frame = new JFrame();
        this.textfield = new JLabel();
        settingsTextField();

        buildTopMenu();

        this.buttons = new JButton[size * size];
        this.gamePanel = new JPanel(new GridLayout(size, size));
        buildGameButtons(size);

        frameSettings();
    }

    /**
     * Stelt de frame variabele in zo als het moet.
     */
    public void frameSettings() {
        this.frame = new JFrame("Tic Tac Toe");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(500, 650);
        this.frame.setLayout(new BorderLayout());
        
        this.frame.add(gamePanel);
        
        gamePanel.setVisible(false);
        this.frame.setVisible(false);
    }

    /**
     * Boven het spel is er een menu. Hier worden de settings hiervoor gezet.
     */
    public void buildTopMenu() {
        JPanel top_panel = new JPanel();
        JPanel rightTopGame;
        JPanel rightTopMenu;
        JPanel rightTopOnlineGame;
        top_panel = settingsTopPanel(top_panel, 500);
        top_panel.add(this.textfield);

        rightTop.setLayout(layout);
        rightTopMenu = settingsRightTopPanel(new JPanel());
        rightTopGame = settingsRightTopPanel(new JPanel());
        rightTopOnlineGame = settingsRightTopPanel(new JPanel());

        disconnectButton = settingsRightTopButtons(disconnectButton);

        resetButton = settingsRightTopButtons(resetButton);
        menuButton = settingsRightTopButtons(menuButton);

        rightTopGame.add(resetButton);
        rightTopGame.add(menuButton);

        rightTopOnlineGame.add(disconnectButton);

        rightTop.add(rightTopMenu, "menu");
        rightTop.add(rightTopGame, "game");
        rightTop.add(rightTopOnlineGame,"online");

        layout.show(rightTop, "menu");
        top_panel.add(rightTop);
        frame.add(top_panel, BorderLayout.NORTH);
    }

    public JButton settingsRightTopButtons(JButton button) {
        button.setFocusable(false);
        button.setFont(new Font(button.getFont().toString(), Font.BOLD, 20));
        button.setBackground(new Color(112, 128, 144));
        button.setSize(new Dimension(140, 70));
        button.setPreferredSize(new Dimension(140, 70));
        return button;
    }

    public JPanel settingsTopPanel(JPanel jPanel, int width) {
        jPanel.setSize(width, 150);
        jPanel.setPreferredSize(new Dimension(width, 150));
        return jPanel;
    }

    public JPanel settingsRightTopPanel(JPanel jpanel) {
        jpanel.setLayout(new GridLayout(2, 1));
        return jpanel;
    }

    public void settingsTextField() {
        textfield.setBackground(new Color(105, 105, 105));
        textfield.setForeground(new Color(0, 0, 0));
        textfield.setFont(new Font(textfield.getFont().toString(), Font.BOLD, 20));
        textfield.setHorizontalAlignment(JLabel.CENTER);
        textfield.setText("Boter kaas en eieren");
        textfield.setPreferredSize(new Dimension(300, 140));
        textfield.setOpaque(true);
    }

    /**
     * Hier worden de knoppen ingesteld waar je op moet drukken om een zet te doen.
     * @param size is de lengte van het bord.
     */
    public void buildGameButtons(int size) {
        for (int i = 0; i < size * size; i++) {
            buttons[i] = new JButton();
            gamePanel.add(buttons[i]);
            buttons[i].setFocusable(false);
            buttons[i].setFont(new Font(buttons[i].getFont().toString(), Font.BOLD, 40));
            buttons[i].setBackground(new Color(220, 220, 220));
        }
    }

    public void show(String panel) {
        if (panel.equals("game") || panel.equals("online")) {
            gamePanel.setVisible(true);
            frame.setVisible(true);
        } else {
            gamePanel.setVisible(false);
            frame.setVisible(false);
        }
        
        layout.show(rightTop, panel);
    }

    /**
     * @param visible geeft aan of het frame zichtbaar moet zijn
     */
    public void setVisible(boolean visible){
        frame.setVisible(visible);
    }

    public void update(GameModel model) {
        //textfield.setText("Boter kaas en eieren");

        char[] boardData = model.getBoardData();
        for (int i = 0; i < boardData.length; i++) {
            if (boardData[i] == 'X') {
                // buttons[i].setBackground(new Color(169, 169, 169));
                buttons[i].setForeground(new Color(164, 0, 0));
                buttons[i].setText("X");
            } else if (boardData[i] == 'O') {
                // buttons[i].setBackground(new Color(169, 169, 169));
                buttons[i].setForeground(new Color(17, 55, 190));
                buttons[i].setText("O");
            } else {
//                buttons[i].setBackground(new Color(220, 220, 220));
                buttons[i].setForeground(new Color(220, 220, 220));
                buttons[i].setText(" ");
            }
        }

        if (model.isWinner()) {
            textfield.setText(model.getWinner() + " heeft gewonnen!");
        } else if (model.isTie()) {
            textfield.setText("Gelijkspel");
        }
        // game.update(model);
    }

    public JButton getDisconnectButton() { return disconnectButton; }

    public JButton getResetButton() {
        return resetButton;
    }

    public JButton getMenuButton() {
        return menuButton;
    }

    public JButton[] getGameButtons() {
        return buttons;
    }

    public void setText(String s) {
        textfield.setText(s);
    }

}
