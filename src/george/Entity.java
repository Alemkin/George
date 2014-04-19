package george;

public class Entity {
    public Entity(Vec2 pos) {
        super(pos.x, pos.y);
    }

    public GameState onInteract(GameState g) {
        return g;
    }

    Sprite getSprite() {
        return null;
    }
}
