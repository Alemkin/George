package george;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.input.Mouse;
import org.lwjgl.input.Keyboard;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL21;

import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.util.glu.GLU;

public class Painter implements Subsystem {
    public Painter() {
        texs = new TextureManager();
    }

    public void start() {
        try {
            Display.setDisplayMode(new DisplayMode(800, 600));
            Display.create(new PixelFormat(),
                    new ContextAttribs(2, 1));
            George.debug("OpenGL version: "+GL11.glGetString(GL11.GL_VERSION));
            texs.load("assets/sprites.manifest");
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        //TODO: Set up basic shaders, etc.
    }

    private int loadShader(String filename, int type) {
        StringBuilder source = new StringBuilder();
        int shaderID = 0;

        try {
            BufferedReader r = new BufferedReader(new FileReader(filename));
            String l;
            while((l = r.readLine()) != null) {
                source.append(l).append("\n");
            }
            r.close();
        } catch (IOException e) {
            System.err.println("Could not read shader file "+filename);
            e.printStackTrace();
            System.exit(-1);
        }

        shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, source);
        GL20.glCompileShader(shaderID);

        if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) ==
                GL11.GL_FALSE) {
            System.err.println("Could not compile shader:"+filename);
            this.exitOnGLError("loadShader");
        }


        return shaderID;
    }

    public GameState onFrame(GameState s, Event[] events) {
        //Three different rendering jobs:
        //TODO: Background
        //TODO: Sprites
        //TODO: Text/UI
        return s;
    }

    private void exitOnGLError(String s) {
        int err = GL11.glGetError();
        if(err != GL11.GL_NO_ERROR) {
            String eStr = GLU.gluErrorString(err);
            System.err.println("FATAL ERROR: "+s+": "+eStr);
            if(Display.isCreated()) Display.destroy();
            System.exit(-1);
        }
    }


    public void destroy() {
    }

    public boolean makeMenu() {
        return false;
    }

    public Menu getMenu() {
        return null;
    }

    private TextureManager texs;
}
