package george;

import java.lang.System;
import org.lwjgl.opengl.Display;

public class George {
    private Subsystem[] subsystems;
    private GameState currentGameState;
    private Menu currentMenu;

    public George() {
        subsystems = new Subsystem[] {new Angels(), new Laws(),
            new Science(), new Painter()};
    }

    public GameState loadGameState() {
        //TODO
        //
        return null;
    }

    public void start() {
        currentGameState = loadGameState();
        currentMenu = new StartMenu();

        for(Subsystem s : subsystems) {
            s.start();
        }
    }

    public void loop() {
        long lastTime = System.currentTimeMillis();

        while(!Display.isCloseRequested()) {
            if(currentMenu != null) {
                currentMenu.onFrame(currentGameState);
                if(currentMenu.hasOneSelected()) {
                    currentMenu = currentMenu.getNewMenu();
                }
                continue;
            }

            long currentTime = System.currentTimeMillis();
            currentGameState.dt = (currentTime - lastTime)/1000.0f;

            Display.sync(60);

            GameState g = currentGameState;

            for(Subsystem s : subsystems) {
                try{
                    g = s.onFrame(g);
                    if(s.makeMenu()) {
                        currentMenu = s.getMenu();
                        break;
                    }
                } catch (Exception e) {
                    System.err.println("Exception occurred: "+e.getMessage());
                    System.err.println("Continuing regardless.");
                    e.printStackTrace();
                }
            }

            currentGameState = g;
            lastTime = currentTime;

            Display.update();
        }
    }


    public void destroy() {

        for(Subsystem s : subsystems) {
            s.destroy();
        }

        Display.destroy();
    }

    public static void main(String[] args) {
        George king = new George();
        king.start();
        king.loop();
        king.destroy();
    }

    public static void debug(String s) {
        System.err.println(s);
    }

    static final boolean DEBUG = true;
}
