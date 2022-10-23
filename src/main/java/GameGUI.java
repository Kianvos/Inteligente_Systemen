import javax.swing.*;
import java.awt.*;

public class GameGUI {
    private GUI parent;
    
    private JButton[] buttons;
    private JPanel panel;

    public GameGUI(GUI parent, int size) {
        this.parent = parent;

        this.panel = new JPanel(new GridLayout(size, size));
        this.buttons = new JButton[size * size];
        createButtons(size);
    }

    public void createButtons(int size) {
        for (int i = 0; i < size * size; i++) {
            buttons[i] = new JButton();
            panel.add(buttons[i]);
            buttons[i].setFocusable(false);
            buttons[i].setFont(new Font(buttons[i].getFont().toString(), Font.BOLD, 40));
            buttons[i].setBackground(new Color(220, 220, 220));
        }
    }

    public JButton[] getGameButtons() {
        return buttons;
    }

    public JPanel getPanel() {
        return panel;
    }

    public void update(GameModel model) {
        parent.setText("Boter kaas en eieren");

        char[] boardData = model.getBoardData();
        for (int i = 0; i < boardData.length; i++) {
            if (boardData[i] == 'X') {
                buttons[i].setForeground(new Color(164, 0, 0));
                buttons[i].setText("X");
            } else if (boardData[i] == 'O') {
                buttons[i].setForeground(new Color(17, 55, 190));
                buttons[i].setText("O");
            } else {
                buttons[i].setText(" ");
            }
        }

        if (model.isWinner()) {
            parent.setText(model.getWinner() + " heeft gewonnen!");
        } else if (model.isTie()) {
            parent.setText("Gelijkspel");
        }
    }
}
