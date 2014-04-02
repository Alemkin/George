package george;

import java.lang.System;
import org.lwjgl.opengl.Display;

public class George {
    private Subsystem[] subsystems;
    private GameState currentGameState;

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

        for(Subsystem s : subsystems) {
            s.start();
        }
    }

    public void loop() {
        long lastTime = System.currentTimeMillis();

        while(!Display.isCloseRequested()) {
            long currentTime = System.currentTimeMillis();
            currentGameState.dt = (currentTime - lastTime)/1000.0f;

            Display.sync(60);

            pollInput(); //TODO: Figure out how to deal with this elegantly.
            //Probably move into Laws?
            GameState g = currentGameState;

            for(Subsystem s : subsystems) {
                g = s.onFrame(g);
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

    private void pollInput() {
        while(Mouse.next()) {
            if(George.DEBUG) {
                George.debug(Mouse.getEventX()+","+Mouse.getEventY()+": "+
                    Mouse.getEventButton()+" "+
                    (Mouse.getEventButtonState()?"true":"false"));
            }
            //TODO: Convert this raw output into 
            //mouseMove, mouseClick, mouseDown, mouseUp events.
        }

        while(Keyboard.next()) {
        }
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
