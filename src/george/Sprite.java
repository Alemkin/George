package george;

public class Sprite {
    public float x, y;
    public String currentSprite;
    public int frameNum;

    public Sprite(float x, float y) {
        this.x = x;
        this.y = y;
        currentSprite = "";
        frameNum = 0;
    }

    //abstract public Vec2 getTextureCoords(float t);
}
