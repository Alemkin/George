package george;

import java.util.Vector;

public class GameState {
    final public Vector<NPC> npcs; //Other characters and whatever
    final public Vector<Tile> tiles;  //The terrain and stuff
    final public Vector<Entity> entities; //Objects that aren't terrain or people
    final public Player player; //You

    public float dt;
}
