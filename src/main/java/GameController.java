import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameController {
    private GameModel model;
    private GUI view;
    private TicTacToeServerConnection ticTacToeServerConnection;

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
                model.toggleIsOnline();
                ExecutorService executor = Executors.newSingleThreadExecutor();
                ticTacToeServerConnection = new TicTacToeServerConnection(view, model);
                view.show("online");
                executor.submit(ticTacToeServerConnection);
                executor.shutdown();
            }
        });

        view.getDisconnectButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(ticTacToeServerConnection != null){
                    ticTacToeServerConnection.disconnect();
                    model.resetGame();
                    view.update(model);
                    view.setText("Boter kaas en eieren");
                    view.show("menu");
                    model.toggleIsOnline();
                }
            }
        });

        view.getMenuButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.show("menu");
                view.setText("Boter kaas en eieren");
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
                if (!model.isWinner() && !model.isTie() && !model.isOnline()){
                    model.sets(idx);
                    view.update(model);
                }
            }
        });
    }
}
