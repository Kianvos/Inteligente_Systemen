
public class Main {
    public static void main(String[] args) {
        int size = 3;
        GameModel model = new GameModel(size);
        GameGUI view = new GameGUI(model, size);
        
        GameController controller = new GameController(model, view);
    }
}
