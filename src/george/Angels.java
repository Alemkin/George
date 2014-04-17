package george;

//AI class
public class Angels implements Subsystem {
    public void start() {
    }

    public GameState onFrame(GameState s, Event[] events) {
        return s;
    }

    public void destroy() {
    }

    public boolean makeMenu() {
        return false;
    }

    public Menu getMenu() {
        return null;
    }
}
