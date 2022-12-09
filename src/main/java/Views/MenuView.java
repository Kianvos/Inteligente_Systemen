package Views;

import javax.swing.*;
import java.awt.*;

public class MenuView extends JPanel {
    private JComboBox<String> games;
    private JComboBox<String> p1;
    private JComboBox<String> p2;

    private JButton singleplayerButton;
    private JButton multiplayerButton;

    public MenuView() {
        // first construct JPanel before constructing MenuView
        super(new GridLayout(3, 1));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] gameTypes = {"TicTacToe", "Othello"};
        this.games = new JComboBox<String>(gameTypes);

        String[] playerTypes = {"Player", "AI"};
        this.p1 = new JComboBox<String>(playerTypes);
        this.p2 = new JComboBox<String>(playerTypes);

        this.singleplayerButton = new JButton("Singleplayer");
        this.multiplayerButton = new JButton("Multiplayer");

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
     *
     * @param button button
     */
    public void styleButton(JButton button) {
        button.setFocusable(false);
        button.setBackground(new Color(220, 220, 220));
    }

    /**
     * Get all currently selected dropdown options
     *
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

