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

//        aiVersusAiButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                playerName = JOptionPane.showInputDialog(frame, "Wat is jullie gebruikersnaam?");
//                if (playerName != null && !playerName.equals("")) {
//                    frame.setVisible(false);
//                    frame.dispose();
//                }
//                System.out.println(playerName);
//            }
//        });
//        String[] buttons = {"Cancel", "AI", "Speler"};
//        playerVersusAiButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                int a = JOptionPane.showOptionDialog(frame, "Wie mag er beginnen? ", "Test", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, buttons, buttons[0]);
//
//            }
//        });
//        String[] buttonsPlayerPlayer = {"Cancel", "O", "X"};
//        playerVersusPlayerButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                int a = JOptionPane.showOptionDialog(frame, "Start X of O? ", "Test", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, buttonsPlayerPlayer, buttonsPlayerPlayer[0]);
//                System.out.println(a);
//
//            }
//        });

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
