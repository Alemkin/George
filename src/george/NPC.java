package george;

public class NPC extends Entity {
    public NPC(Vec2 pos) {
        super(pos);
    }

    public GameState onInteract(GameState s) {
        return s;
    }
}
