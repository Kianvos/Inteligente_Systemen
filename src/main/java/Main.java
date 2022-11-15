
public class Main {
    public static void main(String[] args) {
        int size = 8;
        OthelloModel model = new OthelloModel();
        MenuGUI menu = new MenuGUI();
        GUI view = new GUI(size);
        
        GameController controller = new GameController(model, menu, view);
    }
}
