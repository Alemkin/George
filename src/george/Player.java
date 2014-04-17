package george;

public class Player extends Sprite {
    public Activity a; //What's the player currently doing?

    public Player(Vec2 spawnPoint)  {
        super(spawnPoint.x, spawnPoint.y);
    }
}
