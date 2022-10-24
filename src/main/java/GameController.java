import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameController {
    private GameModel model;
    private GUI view;

    public GameController(GameModel model, GUI view) {
        this.model = model;
        this.view = view;
        createListeners();
    }

    private void createListeners() {
        int size = model.getSize();

        for (int i = 0; i < size*size; i++) {
            createActionListener(i);
        }

        view.getLocalButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.show("game");

                model.resetGame();
                view.update(model);
            }
        });

        view.getServerButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: Server implementatie
            }
        });

        view.getMenuButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.show("menu");

                model.resetGame();
                view.update(model);
            }
        });

        view.getResetButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.resetGame();
                view.update(model);
                // view.updateGame();
            }
        });

        // view.getQuitButton().addActionListener(new ActionListener() {
        //     @Override
        //     public void actionPerformed(ActionEvent e) {
        //         model.quitGame();
        //     }
        // });
    }

    private void createActionListener(int idx) {
        JButton[] buttons = view.getGameButtons();
        buttons[idx].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!model.isWinner() && !model.isTie()){
                    model.sets(idx);
                    view.update(model);
                }
            }
        });
    }
}
