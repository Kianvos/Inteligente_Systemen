package Helper;

import Model.Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class ServerConnectionHelper implements Runnable {
    final private String playerName;
    final private String Hostname;
    final private int portNumber;
    private boolean run;
    private boolean slept;
    private RandomAI model;

    public ServerConnectionHelper(RandomAI model, String playerName) {
        this.Hostname = "127.0.0.1"; // actual server: 145.33.225.170
        this.portNumber = 7789;
        this.playerName = playerName;
        this.run = true;
        this.slept = false;
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
            System.err.println("Don't know about host " + this.Hostname);
            //System.exit(1);
        } catch (IOException | ClassNotFoundException e) {
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
    private void connectedMain(PrintWriter out, BufferedReader in) throws IOException, ClassNotFoundException {
        in.readLine();
        in.readLine();  // remove first two lines the server returns upon connection
        out.println("login " + this.playerName);
        in.readLine(); // message: OK

        boolean recentAI = false;
        boolean done = false;
        boolean firstMoveDone = false;
        boolean aiMoved = false;

        int AantalPotjes = 1;

        while (isRun()) {
            if (done) {
                aiMoved = false;

                System.out.println("done.");
                done = false;
                out.println("subscribe reversi");

                if (AantalPotjes == 25) {
                    System.out.println(AantalPotjes + " keer gespeeld. AFGELOPEN");
                    disconnect();
                }
                AantalPotjes++;
            } else {
                out.println("subscribe reversi");
            }
            if (in.ready()) {
                String input = in.readLine();
//                System.out.println("input: " + input);
                if (input.contains("SVR GAME YOURTURN")) {
                    if (model.isFile() && !aiMoved) {
                        int ai = new ArrayListFile("./data/").ArrayListRead("game_" + AantalPotjes).get(0);
                        int aiPlayer = firstMoveDone ? 2 : 1;
                        if (aiPlayer!=ai) {
//                        if (firstMoveDone ? ai == 2 : ai == 1) {
                            out.println("move " + 1);
                            this.slept = true;
                            continue;
                        }
                    }
                    aiMoved = true;

                    if (!firstMoveDone) {
                        firstMoveDone = true;
                        model.startGameSettings(1, 2);
                    }

                    int move = model.AImove();
                    if (!model.isFile()){
                        model.addMove(move);

                    }
                    out.println("move " + move);
                    recentAI = true;
                }
                if (input.contains("SVR GAME MOVE")) {
                    if (!firstMoveDone) {
                        firstMoveDone = true;
                        model.startGameSettings(2, 1);
                    }
                    if (recentAI) {
                        recentAI = false;
                    } else {
                        this.updateBoard(input);
                    }
                }
                if (input.contains("SVR GAME WIN")) {
                    firstMoveDone = false;
                    done = true;
                    resetGame(AantalPotjes);
                }

                if (input.contains("SVR GAME DRAW")) {
                    firstMoveDone = false;
                    done = true;
                    resetGame(AantalPotjes);
                }

                if (input.contains("SVR GAME LOSS")) {
                    if (!this.slept) { done = true; }
                    this.slept = false;

                    firstMoveDone = false;
                    resetGame(AantalPotjes);
                }
            }
        }
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
        this.model.move(move, model.getOpponent());
    }

    private void resetGame(int aantalPotjes) throws IOException {
        model.setCount(aantalPotjes);
        if (!model.isFile()){
            model.writeFile();
        }
        model.resetGame();
    }
}