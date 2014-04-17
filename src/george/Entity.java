package george;

public class Entity extends Sprite {
    public Entity(Vec2 pos) {
        super(pos.x, pos.y);
    }

    public GameState onInteract(GameState g) {
        return g;
    }
}
