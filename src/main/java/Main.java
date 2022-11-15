
public class Main {
    public static void main(String[] args) {
        int size = 3;
        OthelloModel model = new OthelloModel();
        MenuGUI menu = new MenuGUI();
        GUI view = new GUI(size);
        
        GameController controller = new GameController(model, menu, view);
    }
}
