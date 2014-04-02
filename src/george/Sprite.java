package george;

public class Sprite {
    public float x, y;
    final public float w, h;

    public Sprint(float w, float h, float x, float y) {
        this.w = w;
        this.h = h;
        this.x = x;
        this.y = y;
    }

    abstract public Vec2 getTextureCoords(float t);
}
