import javax.swing.*;
import java.awt.*;

public class GUI {
    // private 
    private JFrame frame;
    
    private JLabel textfield;
    private JPanel top_panel;

    // Buttons
    private JButton localButton = new JButton("local");
    private JButton serverButton = new JButton("server");
    // private JButton quitButton = new JButton("quit");

    private JButton resetButton = new JButton("reset");
    private JButton menuButton = new JButton("menu");

    // Cards
    private CardLayout layout = new CardLayout();
    private JPanel rightTop = new JPanel();
    private JPanel rightTopGame = new JPanel();
    private JPanel rightTopMenu = new JPanel();

    // GameGUI
    private GameGUI game;
    private JPanel gamePanel;

    public GUI(GameModel model, int size) {
        this.frame = new JFrame();

        this.textfield = new JLabel();
        settingsTextField();

        this.top_panel = new JPanel();
        buildTopMenu();

        this.game = new GameGUI(this, size);
        this.game.update(model);

        this.gamePanel = game.getPanel();
        gamePanel.setVisible(false);

        frameSettings();   
    }

    public void frameSettings() {
        this.frame = new JFrame("Tic Tac Toe");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(500, 650);
        this.frame.setLayout(new BorderLayout());
        
        this.frame.add(top_panel, BorderLayout.NORTH);
        this.frame.add(gamePanel);
        
        this.frame.setVisible(true);
    }

    public void buildTopMenu() {
        top_panel = settingsTopPanel(top_panel, 500);
        top_panel.add(this.textfield);

        rightTop.setLayout(layout);
        rightTopMenu = settingsRightTopPanel(new JPanel());
        rightTopGame = settingsRightTopPanel(new JPanel());

        localButton = settingsRightTopButtons(localButton);
        serverButton = settingsRightTopButtons(serverButton);
        // quitButton = settingsRightTopButtons(quitButton);

        resetButton = settingsRightTopButtons(resetButton);
        menuButton = settingsRightTopButtons(menuButton);

        rightTopMenu.add(localButton);
        rightTopMenu.add(serverButton);
        // rightTopMenu.add(quitButton);

        rightTopGame.add(resetButton);
        rightTopGame.add(menuButton);
        
        rightTop.add(rightTopMenu, "menu");
        rightTop.add(rightTopGame, "game");

        layout.show(rightTop, "menu");
        top_panel.add(rightTop);
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

    public void show(String panel) {
        if (panel == "game") {
            gamePanel.setVisible(true);
        } else {
            gamePanel.setVisible(false);
        }
        
        layout.show(rightTop, panel);
    }

    public void update(GameModel model) {
        game.update(model);
    }

    public JButton getLocalButton() {
        return localButton;
    }

    public JButton getServerButton() {
        return serverButton;
    }

    public JButton getResetButton() {
        return resetButton;
    }

    public JButton getMenuButton() {
        return menuButton;
    }

    public JButton[] getGameButtons() {
        return game.getGameButtons();
    }

    public void setText(String s) {
        textfield.setText(s);
    }

    // public JButton getQuitButton() {
    //     return quitButton;
    // }
}
