package Views;

import javax.swing.*;
import java.awt.*;

public class View {
    // private 
    private JFrame frame;

    // Contains all the main views used by the view class
    private JPanel views;
    private MenuView menuView;
    private GameView gameView;
    private CardLayout viewsLayout;

    public View(String title, int size) {
        this.menuView = new MenuView();
        this.gameView = new GameView(size);
        this.viewsLayout = new CardLayout();

        this.views = new JPanel(viewsLayout);
        views.add(menuView, "menu");
        views.add(gameView, "game");
        viewsLayout.show(views, "menu");

        setupFrame(title);
    }

    /**
     * Stelt de frame variabele in zo als het moet.
     */
    public void setupFrame(String title) {
        this.frame = new JFrame(title);
        this.frame.setSize(500, 650);

        this.frame.setLayout(new BorderLayout());
        // frame.add(top_panel, BorderLayout.NORTH);
        this.frame.add(views);

        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setVisible(true);
    }

    public void show(String panel) {
        viewsLayout.show(views, panel);
    }

    public MenuView getMenuView() {
        return menuView;
    }

    public GameView getGameView() {
        return gameView;
    }
}
