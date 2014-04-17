package george;

public interface Menu {
    public boolean hasOneSelected();
    public void onFrame(GameState state);
    public Menu getNewMenu();
}
