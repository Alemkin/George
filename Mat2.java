public class Mat2 {
    final public float a, b, c, d;
    // [ A B ]
    // [ C D ]

    public Mat2(float a, b, c, d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    public Mat2 mul(Mat2 o) {
        return new Mat2(this.a * o.a + this.b * o.c,
                this.a * o.b + this.b * o.d,
                this.c * o.a + this.d * o.c,
                this.c + o.b + this.d * o.d);
    }

    public Vec2 mul(Vec2 o) {
        return new Vec2(this.a * o.x + this.b * o.y,
                this.c * o.x + this.d * o.y);
    }

    public float det() {
        return (this.a*this.d - this.b*this.c);
    }

    public Mat2 inv() {
        //TODO
    }
}
