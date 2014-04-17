package george;

public interface Menu {
    public boolean hasOneSelected();
    public GameState onFrame(GameState state);
    public Menu getNewMenu();
}
