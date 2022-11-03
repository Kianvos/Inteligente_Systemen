import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameController {
    private GameModel model;
    private MenuGUI menu;
    private GUI view;
    private TicTacToeServerConnection ticTacToeServerConnection;

    public GameController(GameModel model, MenuGUI menu, GUI view) {
        this.model = model;
        this.menu = menu;
        this.view = view;
        createMenuListeners();
        createListeners();
    }

    private void createMenuListeners() {
        menu.getAiVersusAiButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String playerName = JOptionPane.showInputDialog(menu.getFrame(),"Wat is jullie gebruikersnaam?", "Groep3A");
                if (playerName != null && !playerName.equals("")) {
                    menu.setVisible(false);
                    model.toggleIsOnline();
                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    ticTacToeServerConnection = new TicTacToeServerConnection(view, model, playerName);
                    view.show("online");
                    executor.submit(ticTacToeServerConnection);
                    executor.shutdown();
                }
                System.out.println(playerName);
            }
        });
        String[] buttonsPlayerAi = {"Cancel", "AI", "Speler"};
        menu.getPlayerVersusAiButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int a = JOptionPane.showOptionDialog(menu.getFrame(), "Wie mag er beginnen? ", "Test", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, buttonsPlayerAi, buttonsPlayerAi[0]);

            }
        });
        String[] buttonsPlayerPlayer = {"Cancel", "O", "X"};
        menu.getPlayerVersusPlayerButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int a = JOptionPane.showOptionDialog(menu.getFrame(), "Start X of O? ", "Test", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, buttonsPlayerPlayer, buttonsPlayerPlayer[0]);
                char playerStart;
                if (a == 1){
                    playerStart = 'O';
                }else if (a == 2){
                    playerStart = 'X';
                }

                if (a != 0){
                    view.show("game");

                    model.resetGame();
                    view.update(model);
                }
            }
        });
    }

    private void createListeners() {
        int size = model.getSize();

        for (int i = 0; i < size * size; i++) {
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
                ticTacToeServerConnection = new TicTacToeServerConnection(view, model, "groep3ai");
                view.show("online");
                executor.submit(ticTacToeServerConnection);
                executor.shutdown();
            }
        });

        view.getDisconnectButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ticTacToeServerConnection != null) {
                    ticTacToeServerConnection.forfeit();
                    model.resetGame();
                    view.update(model);
                    view.setText("Boter kaas en eieren");
                    view.setVisible(false);
                    menu.setVisible(true);
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
                if (!model.isWinner() && !model.isTie() && !model.isOnline()) {
                    model.sets(idx);
                    view.update(model);
                }
            }
        });
    }
}
