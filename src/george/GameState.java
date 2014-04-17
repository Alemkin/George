package george;

import java.util.Vector;

public class GameState {
    final public Vector<NPC> npcs; //Other characters and whatever
    final public Vector<Tile> tiles;  //The terrain and stuff
    final public Vector<Entity> entities; //Objects that aren't terrain or people
    final public Player player; //You

    public float dt;
    public float t;

    public GameState() {
        npcs = new Vector<NPC>();
        tiles = new Vector<Tile>();
        entities = new Vector<Entity>();
        player = new Player(0, 0);

        dt = 0.0f;
        t = 0.0f;
    }
}
