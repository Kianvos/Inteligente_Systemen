import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
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

    /**
     * Zet de instellingen voor de knoppen die op de menu view te zien zijn.
     */
    private void createMenuListeners() {
        menu.getMultiplayerButton().addActionListener(new ActionListener() {
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

        menu.getSingleplayerButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox[] dropdowns = menu.getDropdowns();

                String p1 = (String) dropdowns[0].getSelectedItem();
                String p2 = (String) dropdowns[1].getSelectedItem();

                if (p1.equals("ai") && p2.equals("ai")) {
                    System.out.println("Cannot play ai vs ai");
                    return;
                }

                System.out.println(p1);
                System.out.println(p2);

                Boolean playerVsAi = p1.equals("ai") || p2.equals("ai");

                menu.setVisible(false);
                view.show("game");
                model.resetGame(playerVsAi, p1.equals("ai"), 'X');
                view.update(model);
            }

        });
    }

    /**
     * Zet de instellingen voor de knoppen die op de view te zien zijn tijdens het tic-tac-toe spel.
     */
    private void createListeners() {
        int size = model.getSize();

        for (int i = 0; i < size * size; i++) {
            createActionListener(i);
        }

        view.getDisconnectButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ticTacToeServerConnection != null) {
                    ticTacToeServerConnection.disconnect();
                    model.resetGame(false, false, 'X');
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
                menu.setVisible(true);
                view.setText("Boter kaas en eieren");
                model.resetGame(false,false, 'X');
                view.update(model);
            }
        });

        view.getResetButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (model.getAgainstAi()){
                    model.resetGame(model.getAgainstAi(), Math.random() < 0.5, model.getStartPlayer());
                    view.setText(model.getCurrentPlayer() + " is aan de beurt");
                }else{
                    Random r = new Random();
                    char c = r.nextBoolean() ? 'X' : 'O';
                    model.resetGame(model.getAgainstAi(), Math.random() < 0.5, c);
                    view.setText(c + " is aan de beurt");
                }
                view.update(model);
            }
        });

        // view.getQuitButton().addActionListener(new ActionListener() {
        //     @Override
        //     public void actionPerformed(ActionEvent e) {
        //         model.quitGame();
        //     }
        // });
    }

    /**
     * Zet de instellingen voor de knoppen van het bord zelf. Dus waar je een zet wil doen.
     */
    private void createActionListener(int idx) {
        JButton[] buttons = view.getGameButtons();
        buttons[idx].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!model.isWinner() && !model.isTie() && !model.isOnline()) {
                    model.sets(idx);
                    view.setText(model.getCurrentPlayer() + " is aan de beurt");
                    view.update(model);
                }
            }
        });
    }
}
