import javax.swing.*;
import java.awt.*;

public class gui {
    public JButton[][] button;
    private JLabel textfield;
    private JFrame frame;

    public gui(String title, int width, int height) {
        this.button = new JButton[3][3];

        JPanel t_panel = new JPanel();
        t_panel.setLayout(new GridLayout(1, 1));
        t_panel.setSize(width, 150);

        this.textfield =  new JLabel();
        this.textfield = settingsTextField(this.textfield, width);

        this.frame = new JFrame(title);
        this.frame.setVisible(true);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(width, height);
        this.frame.setLayout(new BorderLayout());



        JPanel bt_panel = new JPanel();// Pannel for buttons

        bt_panel.setLayout(new GridLayout(3, 3));

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                button[i][j] = new JButton();
                bt_panel.add(button[i][j]);
                button[i][j].setFocusable(false);
                button[i][j].setFont(new Font(button[i][j].getFont().toString(), Font.BOLD, 50));
            }
        }
        t_panel.add(this.textfield);
        this.frame.add(t_panel, BorderLayout.NORTH);

        this.frame.add(bt_panel);
    }

    public JLabel settingsTextField(JLabel textField, int width){
        textField.setBackground(new Color(174, 170, 170));
        textField.setForeground(new Color(0, 0, 0));
        textField.setFont(new Font(textField.getFont().toString(), Font.BOLD, 26));
        textField.setHorizontalAlignment(JLabel.CENTER);
        textField.setText("Boter kaas en eieren");
        textField.setOpaque(true);
        textField.setBounds(0,0,width, 150);
        textField.setMinimumSize(new Dimension(width, 150));
        textField.setPreferredSize(new Dimension(width, 150));
        textField.setMaximumSize(new Dimension(width, 150));
        return textField;
    }

    public void updateTextField(String text){
        this.textfield.setText(text);
    }

    public void updateButton(int row, int column, char player) {
        this.button[row][column].setText(String.valueOf(player));
    }

    public static void main(String[] args) {
        gui GUI = new gui("Tic Tac Toe", 450, 600);
        GUI.updateButton(1, 1, 'X');
    }
}
