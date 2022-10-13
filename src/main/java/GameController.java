import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameController {
    private GameModel model;
    private GameGUI view;

    public GameController(GameModel model, GameGUI view) {
        this.model = model;
        this.view = view;
        createListeners();
    }

    private void createListeners() {
        int size = model.getSize();
        for (int i = 0; i < size*size; i++) {
            createActionListener(i);
        }

        view.getExitButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.quitGame();
            }
        });

        view.getResetButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.resetGame();
                view.resetGame();
            }
        });
    }

    private void createActionListener(int idx) {
        JButton[] buttons = view.getAllGameButtons();
        buttons[idx].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!model.isWinner() && !model.isTie()){
                    model.sets(idx);
                    view.update();
                }
            }
        });
    }
}
