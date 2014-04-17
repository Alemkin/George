package george;

public class Sprite {
    public float x, y;
    public float w, h;
    public String currentSprite;
    public int frameNum;

    public Sprite(float w, float h, float x, float y) {
        this.x = x;
        this.y = y;
        currentSprite = null;
        frameNum = 0;
    }

    //abstract public Vec2 getTextureCoords(float t);
}
