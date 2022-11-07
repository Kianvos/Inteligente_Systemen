import java.net.*;
import java.io.*;
import java.util.Arrays;
import java.util.Random;

public class TicTacToeServerConnection implements Runnable {
    final private String playerName;
    private String opponentName;
    final private String Hostname;
    final private int portNumber;
    private boolean run;
    private boolean forfeit;
    private GUI view;
    private GameModel model;

    public TicTacToeServerConnection(GUI view, GameModel model, String playerName){
        this.Hostname = "127.0.0.1"; // actual server: 145.33.225.170
        this.portNumber = 7789;
        this.playerName = playerName;
        this.run = true;
        this.forfeit = false;
        this.view = view;
        this.model = model;
    }

    public boolean isRun() {
        return run;
    }

    public void disconnect() {
        this.run = !(this.run);
    }

    public void forfeit() { this.forfeit = !(this.forfeit); }

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
        in.readLine(); in.readLine(); // remove first two lines the server returns upon connection
        out.println("login " + this.playerName);
        System.out.println(in.readLine()); // message: OK
        out.println("subscribe tic-tac-toe");
        System.out.println(in.readLine()); // message: OK
        this.view.setText("Searching for opponent...");
        while (isRun()) {
            if(this.forfeit){
                System.out.println("forfeit the match :(");
                if(in.ready()){
                    out.println("forfeit");
                }
                out.println("exit");
                this.disconnect();
            }
            if(in.ready()){
                String input = in.readLine();
                System.out.println("input: "+ input);
                if(input.contains("SVR GAME MATCH")) {
                    this.opponentName = input.substring(input.indexOf("OPPONENT: \"") + 11);
                    this.opponentName = this.opponentName.substring(0,this.opponentName.length()-2);
                }
                if(input.contains("SVR GAME YOURTURN")){
                    this.view.setText("turn: "+ this.playerName);
                    System.out.println("my turn :)");
                    System.out.println(this.model.getBoardData());
                    int move = this.model.aiSet();
                    System.out.println(move);
                    out.println("move " + move);
                }else{
                    this.view.setText("turn: "+ this.opponentName);
                }
                if(input.contains("SVR GAME MOVE")){
                    this.updateBoard(input);
                }
                if(input.contains("SVR GAME WIN")){
                    this.view.setText(this.playerName + " wins.");
                    System.out.println("I win! :)");
                    out.println("exit");
                    this.disconnect();
                }

                if(input.contains("SVR GAME DRAW")){
                    this.view.setText("Draw.");
                    System.out.println("It's a draw! :o");
                    out.println("exit");
                    this.disconnect();
                }

                if(input.contains("SVR GAME LOSS")){
                    this.view.setText(this.opponentName + " wins.");
                    System.out.println("I lose! :(");
                    out.println("exit");
                    this.disconnect();
                }
            }
        }
    }

    /**
     * Update the board after a move has been played
     * @param input The message from the server
     */
    private void updateBoard(String input){ // String input
        System.out.println("Game move:");
        int index = input.indexOf("MOVE: \"");
        int move = Integer.parseInt(input.substring(index + 7, index + 8));
        System.out.println("move in remember: " + move);
        this.model.userSet(move);
        this.view.update(this.model);
    }
}