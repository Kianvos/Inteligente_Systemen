package Helper;


public class MainHelper {
    public static void main(String[] args) {
        RandomAI model = new RandomAI();
        ServerConnectionHelper serverConnection = new ServerConnectionHelper(model, "randomAI");
        serverConnection.run();

    }
}
