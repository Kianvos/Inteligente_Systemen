
public class Main {
    public static void main(String[] args) {
        // TODO: nu lengte van het hele bord inplaats van een row of rank
        int size = 9;
        Model model = new Model(size);
        View view = new View("Framework Groep 3A", "Boter Kaas en Eieren", size);
        
        new Controller(model, view);
    }
}
