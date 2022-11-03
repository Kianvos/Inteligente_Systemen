import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuGUI {
    private JFrame frame;

    private JButton aiVersusAiButton = new JButton("AI VS AI");
    private JButton playerVersusAiButton = new JButton("Speler VS AI");
    private JButton playerVersusPlayerButton = new JButton("Player VS PLAYER");

    private String playerName;

    public MenuGUI() {
        this.frame = new JFrame();
        frame.setSize(300, 400);
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        buttonPanel.add(aiVersusAiButton);
        buttonPanel.add(playerVersusAiButton);
        buttonPanel.add(playerVersusPlayerButton);

        frame.add(buttonPanel);
        frame.setVisible(true);
    }

    public void setVisible(boolean visible){
        frame.setVisible(visible);
    }

    public JFrame getFrame(){
        return frame;
    }

    public JButton getAiVersusAiButton(){
        return aiVersusAiButton;
    }

    public JButton getPlayerVersusAiButton(){
        return playerVersusAiButton;
    }

    public JButton getPlayerVersusPlayerButton(){
        return playerVersusPlayerButton;
    }

    public static void main(String[] args) {
        MenuGUI gui = new MenuGUI();
    }
}
