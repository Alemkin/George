package george;

public class StartMenu implements Menu {
    public boolean hasOneSelected() {
        return false;
    }
    public GameState onFrame(GameState state, Event [] events) {
        //TODO Draw a start menu and check for events
        return state;
    }

    public Menu getNewMenu() {
        return null;
    }
}
