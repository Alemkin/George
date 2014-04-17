package george;

public interface Subsystem {
    public void start();
    public GameState onFrame(GameState s, Event[] events);
    public void destroy();

    public boolean makeMenu();
    public Menu getMenu();
}
