package george;

public class StartMenu implements Menu {
    public boolean hasOneSelected() {
        return false;
    }
    public GameState onFrame(GameState state) {
        return state;
    }

    public Menu getNewMenu() {
        return null;
    }
}
