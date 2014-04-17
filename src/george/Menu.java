package george;

public interface Menu {
    public boolean hasOneSelected();
    public GameState onFrame(GameState state, Event [] events);
    public Menu getNewMenu();
}
