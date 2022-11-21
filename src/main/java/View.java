
import javax.swing.*;
import java.awt.*;

public class View {
    // private 
    private JFrame frame;

    // Contains all the main views used by the view class
    private JPanel views;
    private MenuView menuView;
    private GameView gameView;
    private CardLayout viewsLayout;

    public View(String title, int size) {
        this.menuView = new MenuView();
        this.gameView = new GameView(size);
        this.viewsLayout = new CardLayout();

        this.views = new JPanel(viewsLayout);
        views.add(menuView, "menu");
        views.add(gameView, "game");
        viewsLayout.show(views, "menu");

        setupFrame(title);
    }

    /**
     * Stelt de frame variabele in zo als het moet.
     */
    public void setupFrame(String title) {
        this.frame = new JFrame(title);
        this.frame.setSize(500, 650);

        this.frame.setLayout(new BorderLayout());
        // frame.add(top_panel, BorderLayout.NORTH);
        this.frame.add(views);

        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setVisible(true);
    }

    public void show(String panel) {
        // TODO: Geen idee waar dit voor is
        // if (panel.equals("game") || panel.equals("online")) {
        //     gamePanel.setVisible(true);
        //     frame.setVisible(true);
        // } else {
        //     gamePanel.setVisible(false);
        //     frame.setVisible(false);
        // }
        
        viewsLayout.show(views, panel);
    }

    public MenuView getMenuView() {
        return menuView;
    }

    public GameView getGameView() {
        return gameView;
    }
}

class MenuView extends JPanel {
    private JComboBox<String> games;
    private JComboBox<String> p1;
    private JComboBox<String> p2;

    private JButton singleplayerButton;
    private JButton multiplayerButton;

    public MenuView() {
        // first construct JPanel before constructing MenuView
        super(new GridLayout(3, 1));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] gameTypes = { "tictactoe", "othello" };
        this.games = new JComboBox<String>(gameTypes);

        String[] playerTypes = { "player", "ai" };
        this.p1 = new JComboBox<String>(playerTypes);
        this.p2 = new JComboBox<String>(playerTypes);

        this.singleplayerButton = new JButton("singleplayer");
        this.multiplayerButton = new JButton("multiplayer");

        setupMenuView();
    }

    /**
     * setups the whole menu
     */
    public void setupMenuView() {
        setupDropdowns();
        setupButtons();
    }

    /**
     * Sets all dropdowns up ands adds them to the menu
     */
    public void setupDropdowns() {
        JPanel dropdowPanel = new JPanel(new GridLayout(1, 3));

        JPanel left = new JPanel();
        left.add(new JLabel("p1 :"));
        left.add(p1);

        JPanel middle = new JPanel();
        middle.add(games);

        JPanel right = new JPanel();
        right.add(p2);
        right.add(new JLabel(": p2"));

        dropdowPanel.add(left);
        dropdowPanel.add(middle);
        dropdowPanel.add(right);

        add(dropdowPanel);
    }

    /**
     * Sets all buttons up ands adds them to the menu
     */
    public void setupButtons() {
        styleButton(singleplayerButton);
        styleButton(multiplayerButton);

        add(singleplayerButton);
        add(multiplayerButton);
    }

    /**
     * Styles the button given
     * @param button button
     */
    public void styleButton(JButton button) {
        button.setFocusable(false);
        button.setBackground(new Color(220, 220, 220));
    }

    /**
     * Get all currently selected dropdown options
     * @return [games.selected, p1.selected, p2.selected]
     */
    public String[] getSelected() {
        String[] selected = new String[3];

        selected[0] = (String) games.getSelectedItem();
        selected[1] = (String) p1.getSelectedItem();
        selected[2] = (String) p2.getSelectedItem();

        return selected;
    }

    public JButton getSingleplayerButton() {
        return singleplayerButton;
    }

    public JButton getMultiplayerButton() {
        return multiplayerButton;
    }
}

class GameView extends JPanel {
    // for tictactoe instead of 3 the size will be 9
    // size stands for the entire board size instead of the size of one row  
    private int size;
    private JPanel buttonPanel;
    private JButton[] buttons;

    // Just making the GameView nicer to look at
    private JLabel textfield;
    private JPanel top;

    private CardLayout topRightLayout;
    private JPanel topRight;

    private JButton resetButton;
    private JButton menuButton;
    private JButton disconnectButton;

    public GameView(int size) {
        // first construct JPanel before constructing GameView
        super(new BorderLayout());

        this.size = size;
        this.buttons = new JButton[size];

        this.resetButton = new JButton("reset");
        this.menuButton = new JButton("menu");
        this.disconnectButton = new JButton("disconnect");

        setupGameView();
    }

    public void setBoardSize(int size) {
        this.size = size;
        this.buttons = new JButton[this.size];
        remove(this.buttonPanel);
        setupButtons();
    }


    public void setupGameView() {
        setupTextfield();
        setupTopPanel();
        setupButtons();
    }

    /**
     * Sets up textfield
     */
    public void setupTextfield() {
        this.textfield = new JLabel();
        textfield.setBackground(new Color(105, 105, 105));
        textfield.setForeground(new Color(0, 0, 0));
        textfield.setFont(new Font(textfield.getFont().toString(), Font.BOLD, 20));
        textfield.setHorizontalAlignment(JLabel.CENTER);
        // textfield.setText();
        textfield.setPreferredSize(new Dimension(300, 140));
        textfield.setOpaque(true);
    }

    public void setupTopPanel() {
        this.top = new JPanel();
        top.setSize(500, 150);
        top.setPreferredSize(new Dimension(500, 150));

        top.add(textfield);
        setupTopRight();

        add(top, BorderLayout.NORTH);
    }

    public void setupTopRight() {
        this.topRightLayout = new CardLayout();
        this.topRight = new JPanel(topRightLayout);

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

        top.add(topRight);
    }

    /**
     * Hier worden de knoppen ingesteld waar je op moet drukken om een zet te doen.
     * @param size is de lengte van het bord.
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
     * @param model model die beheert over alle game logica
     */
    public void update(Model model) {
        char[] board = model.getBoardData();

        for (int i = 0; i < size; i++) {
            if (board[i] == 'X') {
                buttons[i].setForeground(new Color(164, 0, 0));
                buttons[i].setBackground(Color.WHITE);
                buttons[i].setText("");
                continue;
            }
            if (board[i] == 'O') {
                buttons[i].setForeground(new Color(17, 55, 190));
                buttons[i].setBackground(Color.BLACK);
                buttons[i].setText("");
                continue;
            }
            if (board[i] == '-'){
                buttons[i].setForeground(new Color(150, 150, 150));
                buttons[i].setText("-");
                continue;
            }

            buttons[i].setForeground(new Color(220, 220, 220));
            buttons[i].setText("");
        }

        if (model.isWinner()) {
            textfield.setText(model.getWinner() + " heeft gewonnen!");
         } else if (model.isTie()) {
            textfield.setText("Gelijkspel");
        }
    }

    public void show(String panel) {
        topRightLayout.show(topRight, panel);
    }

    /**
     * Styles the button given
     * @param button button
     */
    public void styleButton(JButton button) {
        button.setFocusable(false);
        button.setFont(new Font(button.getFont().toString(), Font.BOLD, 40));
        button.setBackground(new Color(220, 220, 220));
    }

    /**
     * Styles the button given for the topRight panel
     * @param button button
     */
    public void styleRightTopButton(JButton button) {
        button.setFocusable(false);
        button.setFont(new Font(button.getFont().toString(), Font.BOLD, 20));
        button.setBackground(new Color(112, 128, 144));
        button.setSize(new Dimension(140, 70));
        button.setPreferredSize(new Dimension(140, 70));
    }

    public void setText(String s) {
        this.textfield.setText(s);
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
}