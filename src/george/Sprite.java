package george;

public class Sprite {
    public float x, y;
    public float w, h;

    public Sprite(float w, float h, float x, float y) {
        this.w = w;
        this.h = h;
        this.x = x;
        this.y = y;
    }

    abstract public Vec2 getTextureCoords(float t);
}
