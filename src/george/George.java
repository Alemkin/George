package george;

import java.lang.System;
import java.util.Vector;

import org.lwjgl.opengl.Display;

import org.lwjgl.input.Mouse;
import org.lwjgl.input.Keyboard;

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
        Vector<Event> events = new Vector<Event>();

        while(!Display.isCloseRequested()) {

            events.clear();
            while(Keyboard.next()) {
                //TODO: poll
            }

            while(Mouse.next()) {
                //TODO: poll
            }
            
            if(currentMenu != null) {
                currentGameState = currentMenu.onFrame(currentGameState,
                        events.toArray(new Event[0]));
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
                    g = s.onFrame(g, events.toArray(new Event[0]));
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
