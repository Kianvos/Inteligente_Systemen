package Server;

import Model.Model;
import Model.Othello;
import Views.View;

import java.net.*;
import java.io.*;

public class ServerConnection implements Runnable {
    final private String playerName;
    final private String Hostname;
    final private int portNumber;
    final private String DEFAULT = "Searching for opponent...";
    private String opponentName;
    private boolean run;
    private View view;
    private Model model;
    private boolean opponentStart;

    public ServerConnection(View view, Model model, String playerName) {
        this.Hostname = "127.0.0.1"; // actual server: 145.33.225.170
        this.portNumber = 7789;
        this.playerName = playerName;
        this.run = true;
        this.view = view;
        this.model = model;
    }

    public boolean isRun() {
        return run;
    }

    public void disconnect() {
        this.run = false;
    }

    @Override
    public void run() {
        try (
                Socket socket = new Socket(this.Hostname, this.portNumber);
                PrintWriter out =
                        new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in =
                        new BufferedReader(
                                new InputStreamReader(socket.getInputStream()));
        ) {
            connectedMain(out, in);
        } catch (UnknownHostException e) {
            this.view.getGameView().setText("Server not found.");
            System.err.println("Don't know about host " + this.Hostname);
            //System.exit(1);
        } catch (IOException e) {
            this.view.getGameView().setText("Could not connect to server.");
            System.err.println("Couldn't get I/O for the connection to " +
                    this.Hostname);
            //System.exit(1);
        }
    }

    /**
     * The "main" when connected to a server for a tic-tac-toe match
     *
     * @param out For sending messages to the server
     * @param in  For receiving messages from the server
     * @throws IOException
     */
    private void connectedMain(PrintWriter out, BufferedReader in) throws IOException {
        in.readLine();
        in.readLine();  // remove first two lines the server returns upon connection
         out.println("login " + this.playerName);
//       out.println("login " + "kian");
        in.readLine(); // message: OK
        if (this.model instanceof Othello) {
            out.println("subscribe reversi");
        } else {
            out.println("subscribe tic-tac-toe");
        }
        this.view.getGameView().setText(DEFAULT);

        boolean firstMoveDone = false;
        boolean done = false;
        int AantalPotjes = 1;

        int countWin = 0;
        int countDraw = 0;
        int countLoss = 0;
        this.model.setCurrentPlayer(2);
        while (isRun()) {
            if (done){
                System.out.println("done.");
                done = false;
                if(this.model instanceof Othello){
                    out.println("subscribe reversi");
                }else{
                    out.println("subscribe tic-tac-toe");
                }
                if(AantalPotjes == 100){
                    System.out.println(AantalPotjes +" keer gespeeld");
                    disconnect();
                }
                AantalPotjes++;
                resetBoard("Nieuw spel: ");
            }
            if (in.ready()) {
                String input = in.readLine();
                System.out.println("input: " + input);
                if (input.contains("SVR GAME MATCH")) {
                    this.opponentName = input.substring(input.indexOf("OPPONENT: \"") + 11);
                    this.opponentName = this.opponentName.substring(0, this.opponentName.indexOf("\""));
                }
                if (input.contains("SVR GAME YOURTURN")) {
                    this.view.getGameView().setText("turn: " + this.playerName);
                    if (!firstMoveDone) {
                        this.opponentStart = false;
                        firstMoveDone = true;
                        this.model.setCurrentPlayer(1);
                        System.out.println(model.getStartPlayer());
                    }
                    this.view.getGameView().setText("turn: " + this.playerName);
                    if (this.opponentStart) {
                        out.println("move " + this.model.aiSet(1));
                    } else {
                        out.println("move " + this.model.aiSet(2));
                    }
                    this.view.getGameView().setText("turn: " + this.opponentName);
                }
                if (input.contains("SVR GAME MOVE")) {
                    if (!firstMoveDone) {
                        this.opponentStart = true;
                        firstMoveDone = true;
                        this.model.setCurrentPlayer(1);
                    }
                    this.updateBoard(input);
                }
                if (input.contains("SVR GAME WIN")) {
                    countWin++;
                    done = true;
                    String message = String.format("Last match winner: %s", this.playerName);
                    view.getGameView().setText(message);
//                    resetBoard(message);
                    firstMoveDone = false;
                }

                if (input.contains("SVR GAME DRAW")) {
                    countDraw++;
                    done = true;
                    String message = "Last match: Draw";
                    view.getGameView().setText(message);
//                    resetBoard("Last match: Draw");
                    firstMoveDone = false;
                }

                if (input.contains("SVR GAME LOSS")) {
                    countLoss++;
                    done = true;
                    String message = String.format("Last match winner: %s", this.opponentName);
                    view.getGameView().setText(message);
//                    resetBoard(message);
                    firstMoveDone = false;
                }
            }
        }
        System.out.println("Win: " + countWin);
        System.out.println("Draw: " + countDraw);
        System.out.println("Loss: " + countLoss);
        out.println("exit");
    }

    /**
     * Update the board after a move has been played
     *
     * @param input The message from the server
     */
    private void updateBoard(String input) { // String input
        int index = input.indexOf("MOVE: \"");
        String temp = input.substring(index + 7);
        int move = Integer.parseInt(temp.substring(0, temp.indexOf("\"")));
        this.model.userSet(move);
        this.view.getGameView().update(this.model);
    }

    /**
     * Reset the board to the beginning stage
     * to play a new game.
     */
    public void resetBoard(String message) {
        this.model.resetGame(false, false, 1);
        this.view.getGameView().update(this.model);
        this.view.getGameView().setText(String.format("<html>%s<br />%s</html>", message, DEFAULT));
    }
}