import Controller.Controller;
import Model.Othello;
import Views.View;

public class Main {
    public static void main(String[] args) {
        int size = 8;
        Othello model = new Othello();
        View view = new View("Framework ITV2A3", size * size);
        Controller controller = new Controller(model, view);
    }
}
