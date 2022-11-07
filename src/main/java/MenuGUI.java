import javax.swing.*;
import java.awt.*;

public class MenuGUI {
    private JFrame frame;

    private String[] options = {"player", "ai"};
    private JComboBox[] dropdowns = {new JComboBox(options), new JComboBox(options)};

    private JButton singleplayerButton = new JButton("Singleplayer");
    private JButton mutliplayerButton = new JButton("Multiplayer");

    public MenuGUI() {
        this.frame = new JFrame();
        frame.setSize(300, 400);
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel optionPanel = new JPanel(new FlowLayout());

        optionPanel.add(new JLabel("p1:"));
        optionPanel.add(dropdowns[0]);
        optionPanel.add(new JLabel("p2:"));
        optionPanel.add(dropdowns[1]);

        buttonPanel.add(optionPanel);
        buttonPanel.add(singleplayerButton);
        buttonPanel.add(mutliplayerButton);

        frame.add(buttonPanel);
        frame.setVisible(true);
    }

    /**
     * @param visible geeft aan of het frame zichtbaar moet zijn
     */
    public void setVisible(boolean visible){
        frame.setVisible(visible);
    }

    public JFrame getFrame(){
        return frame;
    }

    public JComboBox[] getDropdowns() {
        return dropdowns;
    }

    public JButton getSingleplayerButton() {
        return singleplayerButton;
    }

    public JButton getMultiplayerButton() {
        return mutliplayerButton;
    }
}
