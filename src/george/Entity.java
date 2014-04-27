package george;

public class Entity {
    final public float x, y;
    public Entity(Vec2 pos) {
        x = pos.x;
        y = pos.y;
    }

    public GameState onInteract(GameState g) {
        return g;
    }

    Sprite getSprite() {
        return null;
    }
}
