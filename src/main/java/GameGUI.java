import javax.swing.*;
import java.awt.*;

public class GameGUI {

    private GameModel model;
    private JFrame frame;
    private JButton quitButton;
    private JButton resetButton;
    private JButton[] buttons;
    private JLabel textfield;
    private JPanel top_panel;
    private JPanel bt_panel;


    public GameGUI(GameModel model, int size) {
        this.model = model;

        this.frame = new JFrame();

        this.quitButton = new JButton("Quit");
        this.resetButton = new JButton("Reset");

        this.textfield = new JLabel();
        settingsTextField();

        this.top_panel = new JPanel();
        buildTopMenu();

        this.bt_panel = new JPanel(new GridLayout(size, size));// Pannel for buttons

        this.buttons = new JButton[size * size];
        createButtons(size);

        update();
        frameSettings();

    }

    public void frameSettings() {
        this.frame = new JFrame("Tic Tac Toe");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(450, 600);
        this.frame.setLayout(new BorderLayout());
        frame.add(top_panel, BorderLayout.NORTH);
        frame.add(bt_panel);
        this.frame.setVisible(true);
    }

    public void buildTopMenu() {
        top_panel = settingsTopPanel(top_panel, 450);
        top_panel.add(this.textfield);

        JPanel rightTop = new JPanel();
        rightTop.setLayout(new GridLayout(2, 1));
        quitButton = settingsRightTopButtons(quitButton);
//        quitButton.addActionListener(this);
        resetButton = settingsRightTopButtons(resetButton);
        rightTop.add(quitButton);
        rightTop.add(resetButton);
        top_panel.add(rightTop);
    }

    public JPanel settingsTopPanel(JPanel jPanel, int width) {
        jPanel.setSize(width, 150);
        jPanel.setPreferredSize(new Dimension(width, 150));
        return jPanel;
    }

    public JButton settingsRightTopButtons(JButton button) {
        button.setFocusable(false);
        button.setFont(new Font(button.getFont().toString(), Font.BOLD, 20));
        button.setBackground(new Color(33, 27, 125));
        button.setSize(new Dimension(140, 70));
        button.setPreferredSize(new Dimension(140, 70));
        return button;
    }

    public void createButtons(int size) {
        for (int i = 0; i < size * size; i++) {
            buttons[i] = new JButton();
            bt_panel.add(buttons[i]);
            buttons[i].setFocusable(false);
            buttons[i].setFont(new Font(buttons[i].getFont().toString(), Font.BOLD, 40));
        }

    }

    public void settingsTextField() {
        textfield.setBackground(new Color(174, 170, 170));
        textfield.setForeground(new Color(0, 0, 0));
        textfield.setFont(new Font(textfield.getFont().toString(), Font.BOLD, 20));
        textfield.setHorizontalAlignment(JLabel.CENTER);
        textfield.setText("Boter kaas en eieren");
        textfield.setPreferredSize(new Dimension(300, 140));
        textfield.setOpaque(true);
    }

    public void resetGame(){
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setText("");
        }
        textfield.setText("Boter kaas en eieren");
    }

    public JButton[] getAllGameButtons() {
        return buttons;
    }

    public JButton getExitButton() {
        return quitButton;
    }

    public JButton getResetButton() {
        return resetButton;
    }

    public void update() {
        char[] boardData = model.getBoardData();
        for (int i = 0; i < boardData.length; i++) {
            if (boardData[i] == 'X') {
                buttons[i].setForeground(new Color(164, 0, 0));
                buttons[i].setText("X");
            } else if (boardData[i] == 'O') {
                buttons[i].setForeground(new Color(17, 55, 190));
                buttons[i].setText("O");
            }
        }
        if (model.isWinner()) {
            textfield.setText(model.getWinner() + " heeft gewonnen!");
        } else if (model.isTie()) {
            textfield.setText("Gelijkspel");
        }
    }
}
