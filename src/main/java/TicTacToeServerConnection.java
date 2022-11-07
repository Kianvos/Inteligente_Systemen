import java.net.*;
import java.io.*;

public class TicTacToeServerConnection implements Runnable {
    final private String playerName;
    final private String Hostname;
    final private int portNumber;
    final private String DEFAULT = "Searching for opponent...";
    private String opponentName;
    private boolean run;
    private GUI view;
    private GameModel model;

    public TicTacToeServerConnection(GUI view, GameModel model, String playerName){
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
        this.run = !(this.run);
    }

    @Override
    public void run(){
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
            this.view.setText("Server not found.");
            System.err.println("Don't know about host " + this.Hostname);
            //System.exit(1);
        } catch (IOException e) {
            this.view.setText("Could not connect to server.");
            System.err.println("Couldn't get I/O for the connection to " +
                    this.Hostname);
            //System.exit(1);
        }
    }

    /**
     * The "main" when connected to a server for a tic-tac-toe match
     * @param out For sending messages to the server
     * @param in For receiving messages from the server
     * @throws IOException
     */
    private void connectedMain(PrintWriter out, BufferedReader in) throws IOException {
        in.readLine(); in.readLine();  // remove first two lines the server returns upon connection
        out.println("login " + this.playerName);
        in.readLine(); // message: OK
        //out.println("subscribe tic-tac-toe");
        this.view.setText(DEFAULT);
        while (isRun()) {
            if(in.ready()){
                String input = in.readLine();
                System.out.println("input: "+ input);
                if(input.contains("SVR GAME MATCH")) {
                    this.opponentName = input.substring(input.indexOf("OPPONENT: \"") + 11);
                    this.opponentName = this.opponentName.substring(0,this.opponentName.indexOf("\""));
                }
                if(input.contains("SVR GAME YOURTURN")){
                    this.view.setText("turn: "+ this.playerName);
                    out.println("move " + this.model.aiSet());
                }else{
                    this.view.setText("turn: "+ this.opponentName);
                }
                if(input.contains("SVR GAME MOVE")){
                    this.updateBoard(input);
                }
                if(input.contains("SVR GAME WIN")){
                    String message = String.format("Last match winner: %s", this.playerName);
                    resetBoard(message);
                }

                if(input.contains("SVR GAME DRAW")){
                    resetBoard("Last match: Draw");
                }

                if(input.contains("SVR GAME LOSS")){
                    String message = String.format("Last match winner: %s", this.opponentName);
                    resetBoard(message);
                }
            }
        }
        out.println("exit");
    }

    /**
     * Update the board after a move has been played
     * @param input The message from the server
     */
    private void updateBoard(String input){ // String input
        int index = input.indexOf("MOVE: \"");
        int move = Integer.parseInt(input.substring(index + 7, index + 8));
        this.model.userSet(move);
        this.view.update(this.model);
    }

    /**
     * Reset the board to the beginning stage
     * to play a new game.
     */
    public void resetBoard(String message) {
        this.model.resetGame();
        this.view.update(this.model);
        this.view.setText(String.format("<html>%s<br />%s</html>", message,DEFAULT));
    }
}