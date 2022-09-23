import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class gui implements ActionListener {
    private JButton[] button;
    private JLabel textfield;
    private JFrame frame;

    private boolean buttonPressed;
    private boolean resetGame;
    private int set;

    public gui(String title, int width, int height) {
        this.button = new JButton[9];


        this.textfield = new JLabel();
        this.textfield = settingsTextField(this.textfield);


        this.frame = new JFrame(title);
        this.frame.setVisible(true);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(width, height);
        this.frame.setLayout(new BorderLayout());

        this.buttonPressed = false;
        this.resetGame = false;
        this.set = 0;

        JPanel bt_panel = new JPanel();// Pannel for buttons
        bt_panel.setLayout(new GridLayout(3, 3));

        for (int i = 0; i < 9; i++) {
            button[i] = new JButton();
            bt_panel.add(button[i]);
            button[i].setFocusable(false);
            button[i].setFont(new Font(button[i].getFont().toString(), Font.BOLD, 40));
            button[i].addActionListener(this);
        }

        JPanel top_panel = new JPanel();
        top_panel = settingsTopPanel(top_panel, width);
        top_panel.add(this.textfield);

        JPanel rightTop = new JPanel();
        rightTop.setLayout(new GridLayout(2, 1));
        JButton quitButton = new JButton("Quit");
        quitButton = settingsRightTopButtons(quitButton);
        quitButton.addActionListener(this);
        JButton resetButton = new JButton("Reset");
        resetButton = settingsRightTopButtons(resetButton);
        rightTop.add(quitButton);
        rightTop.add(resetButton);
        top_panel.add(rightTop);
        this.frame.add(top_panel, BorderLayout.NORTH);

        this.frame.add(bt_panel);
    }

    public JPanel settingsTopPanel(JPanel jPanel, int width) {
        jPanel.setSize(width, 150);
        jPanel.setPreferredSize(new Dimension(width, 150));
        return jPanel;
    }

    public JLabel settingsTextField(JLabel textField) {
        textField.setBackground(new Color(174, 170, 170));
        textField.setForeground(new Color(0, 0, 0));
        textField.setFont(new Font(textField.getFont().toString(), Font.BOLD, 20));
        textField.setHorizontalAlignment(JLabel.CENTER);
        textField.setText("Boter kaas en eieren");
        textField.setPreferredSize(new Dimension(300, 140));
        textField.setOpaque(true);
        return textField;
    }

    public JButton settingsRightTopButtons(JButton button) {
        button.setFocusable(false);
        button.setFont(new Font(button.getFont().toString(), Font.BOLD, 20));
        button.setBackground(new Color(33, 27, 125));
        button.setSize(new Dimension(140, 70));
        button.setPreferredSize(new Dimension(140, 70));
        return button;
    }

    public void updateTextField(String text) {
        this.textfield.setText(text);
    }

    public void updateButton(int pos, char player) {
        this.button[pos].setText(String.valueOf(player));
    }

    public static void main(String[] args) {
        gui GUI = new gui("Tic Tac Toe", 450, 600);
        GUI.updateButton(1, 'X');
    }

    public int getSet() {
        return set;
    }

    public void setSet(int i) {
        this.set = i;
    }

    public boolean getButtonPressed() {
        return buttonPressed;
    }

    public void setButtonPressed(boolean pressed) {
        this.buttonPressed = pressed;
    }

    public void setResetGame(boolean reset) {
        this.resetGame = reset;
    }

    public boolean getResetGame() {
        return this.resetGame;
    }

    public void resetBoard() {
        for (JButton jButton : this.button) {
            jButton.setText("");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand());
        if (Objects.equals(e.getActionCommand(), "Quit")) {
            System.exit(0);
        } else if (Objects.equals(e.getActionCommand(), "Reset")) {
            this.resetGame = true;
        } else {
            for (int i = 0; i < 9; i++) {
                if (e.getSource() == button[i]) {
                    this.buttonPressed = true;
                    setSet(i);
                }
            }
        }
    }
}
