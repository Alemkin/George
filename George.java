import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
//TODO: Make this into a package.

public class George {
    private Subsystem[] subsystems;
    private GameState currentGameState;

    public George() {
        subsystems = new Subsystem[] {new Angels(), new Laws(),
            new Painter(), new Science()};
        currentGameState = loadGameState();
    }

    public void start() {
        for(Subsystem s : subsystems) {
            s.start();
        }

        while(!Display.isCloseRequested()) {

            pollInput(); //TODO: Figure out how to deal with this elegantly.
            GameState g = currentGameState;

            for(Subsystem s : subsystems) {
                g = s.onFrame(g);
            }

            currentGameState = g;

            Display.update();
        }

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
    }

    public static void debug(String s) {
        System.err.println(s);
    }

    static final DEBUG = true;
}
