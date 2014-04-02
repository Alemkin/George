package george;

//Physics engine.
public class Science implements Subsystem {
    public void start() {}
    public GameState onFrame(GameState s) {
        return s;
    }
    public void destroy() {}
}
