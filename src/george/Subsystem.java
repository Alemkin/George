package george;

public interface Subsystem {
    public void start();
    public GameState onFrame(GameState s);
    public void destroy();
    public boolean makeMenu();
    public Menu getMenu();
}
