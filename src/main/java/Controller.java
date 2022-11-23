import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Controller {
    private Model model;
    private View view;
    private ServerConnection serverConnection;

    private final int EMPTY = 0;

    private final int PLAYER_ONE = 1;
    private final int PLAYER_TWO = 2;

    private final int SUGGESTED = 3;

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;

        createMenuListeners();
        createGameListeners();
    }

    /**
     * Zet de instellingen voor de knoppen die op de menu view te zien zijn.
     */
    private void createMenuListeners() {
        view.getMenuView().getMultiplayerButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // String playerName = JOptionPane.showInputDialog(menu.getFrame(),"Wat is jullie gebruikersnaam?", "Groep3A");
                String playerName = "ITV2A3";

                // if (playerName != null && !playerName.equals("")) {
                    // menu.setVisible(false);

                String game = view.getMenuView().getSelected()[0];

                if (game.equals("TicTacToe")) {
                    //View karakters voor boter kaas en eieren
                    view.getGameView().setPlayerOnePiece("X");
                    view.getGameView().setPlayerTwoPiece("O");

                    //Achtergrond van het bord wit maken
                    view.getGameView().setBoardBgColor(new Color(220, 220, 220));

                    //Kleur van de steentjes rood en blauw maken
                    view.getGameView().setPlayerOneColor(new Color(164, 0, 0));
                    view.getGameView().setPlayerTwoColor(new Color(17, 55, 190));

                    model = new TicTacToe();
                    view.getGameView().setBoardSize(3 * 3);
                    model.toggleIsOnline();
                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    serverConnection = new ServerConnection(view, model, playerName);

                    view.show("game");
                    view.getGameView().show("online");

                    executor.submit(serverConnection);
                    executor.shutdown();
                }

                if (game.equals("Othello")) {
                    //View karakters voor Othello
                    view.getGameView().setPlayerOnePiece("O");
                    view.getGameView().setPlayerTwoPiece("O");

                    //Achtergrond van het bord groen maken
                    view.getGameView().setBoardBgColor(new Color(0, 102, 34));

                    //Kleur van de steentjes zwart en wit maken
                    view.getGameView().setPlayerOneColor(new Color(255, 255, 255));
                    view.getGameView().setPlayerTwoColor(new Color(0, 0, 0));


                    System.out.println("Othello still w.i.p.");
                    model = new Othello();
                    view.getGameView().setBoardSize(8 * 8);
                }

            }
        });

        view.getMenuView().getSingleplayerButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] selected = view.getMenuView().getSelected();

                String game = selected[0];
                String p1 = selected[1];
                String p2 = selected[2];

                if (game.equals("TicTacToe")) {

                    //View karakters voor Othello
                    view.getGameView().setPlayerOnePiece("X");
                    view.getGameView().setPlayerTwoPiece("O");

                    //Achtergrond van het bord wit maken
                    view.getGameView().setBoardBgColor(new Color(220, 220, 220));

                    //Kleur van de steentjes rood en blauw maken
                    view.getGameView().setPlayerOneColor(new Color(164, 0, 0));
                    view.getGameView().setPlayerTwoColor(new Color(17, 55, 190));

                    model = new TicTacToe();
                    view.getGameView().setBoardSize(3 * 3);
                    for (int i = 0; i < 3*3; i++) {
                        createActionListener(i);
                    }
                }

                if (game.equals("Othello")) {
                    System.out.println("Othello still w.i.p.");

                    //View karakters voor Othello
                    view.getGameView().setPlayerOnePiece("O");
                    view.getGameView().setPlayerTwoPiece("O");

                    //Achtergrond van het bord groen maken
                    view.getGameView().setBoardBgColor(new Color(0, 102, 34));

                    //Kleur van de steentjes zwart en wit maken
                    view.getGameView().setPlayerOneColor(new Color(255, 255, 255));
                    view.getGameView().setPlayerTwoColor(new Color(0, 0, 0));

                    model = new Othello();
                    view.getGameView().setBoardSize(8 * 8);
                    for (int i = 0; i < 8*8; i++) {
                        createActionListener(i);
                    }
                }

                if (p1.equals("AI") && p2.equals("AI")) {
                    System.out.println("Cannot play ai vs ai");
                    return;
                }

                System.out.println(p1);
                System.out.println(p2);

                Boolean playerVsAi = p1.equals("AI") || p2.equals("AI");

                view.show("game");
                view.getGameView().show("local");

                model.resetGame(playerVsAi, p1.equals("ai"), PLAYER_ONE);
                view.getGameView().update(model);
            }

        });
    }

    /**
     * Zet de instellingen voor de knoppen die op de view te zien zijn tijdens het tic-tac-toe spel.
     */
    private void createGameListeners() {
        int size = model.getSize();
        for (int i = 0; i < size*size; i++) {
            createActionListener(i);
        }

        view.getGameView().getDisconnectButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (serverConnection != null) {
                    serverConnection.disconnect();
                    model.resetGame(false, false, PLAYER_ONE);
                    view.getGameView().update(model);
                    view.getGameView().setText("Boter kaas en eieren");
                    
                    // view.getGameView().setVisible(false);
                    // menu.setVisible(true);

                    view.show("menu");

                    model.toggleIsOnline();
                }
            }
        });

        view.getGameView().getMenuButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.show("menu");
                // menu.setVisible(true);
                view.getGameView().setText("Boter kaas en eieren");
                model.resetGame(false,false, PLAYER_ONE);
                view.getGameView().update(model);
            }
        });

        view.getGameView().getResetButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (model.getAgainstAi()){
                    model.resetGame(model.getAgainstAi(), Math.random() < 0.5, model.getStartPlayer());
                    view.getGameView().setText(model.getCurrentPlayerChar() + " is aan de beurt");
                } else {
                    Random r = new Random();
                    int c = r.nextBoolean() ? PLAYER_ONE : PLAYER_TWO;
                    model.resetGame(model.getAgainstAi(), Math.random() < 0.5, c);
                    view.getGameView().setText(c + " is aan de beurt");
                }

                view.getGameView().update(model);
            }
        });
    }

    /**
     * Zet de instellingen voor de knoppen van het bord zelf. Dus waar je een zet wil doen.
     */
    private void createActionListener(int idx) {
        JButton[] buttons = view.getGameView().getButtons();
        buttons[idx].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!model.isWinner() && !model.isTie() && !model.isOnline()) {
                    model.sets(idx);
                    view.getGameView().setText(model.getCurrentPlayerChar() + " is aan de beurt");
                    view.getGameView().update(model);
                }
            }
        });
    }
}
