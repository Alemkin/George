package george;

public class Vec2i {
    final public  int x;
    final public int y;

    public Vec2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vec2i add(Vec2i o) {
        return new Vec2i(o.x+this.x , o.y+this.y);
    }

    public Vec2i scale(int s) {
        return new Vec2i(s*this.x, s*this.y);
    }

    public int dot(Vec2i o) {
        return (this.x * o.x) + (this.y * o.y);
    }
}
