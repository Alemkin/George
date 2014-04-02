package george;

public class Vec2 {
    final public  float x;
    final public float y;

    public Vec2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vec2 add(Vec2 o) {
        return new Vec2(o.x+this.x , o.y+this.y);
    }

    public Vec2 scale(float s) {
        return new Vec2(s*this.x, s*this.y);
    }

    public float dot(Vec2 o) {
        return (this.x * o.x) + (this.y * o.y);
    }
}
