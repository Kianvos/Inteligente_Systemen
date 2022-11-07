
public class Main {
    public static void main(String[] args) {
        int size = 3;
        GameModel model = new GameModel(size);
        MenuGUI menu = new MenuGUI();
        GUI view = new GUI(model, size);
        
        GameController controller = new GameController(model, menu, view);
    }
}
