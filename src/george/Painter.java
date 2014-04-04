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
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.glu.GLU;

public class Painter implements Subsystem {
    public void start() {
        try {
            Display.setDisplayMode(new DisplayMode(800, 600));
            Display.create();
            //TODO: Set up for OpenGL 3.0 use.
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(0);
        }
        //TODO: Set up basic shaders, load sprite textures, etc.
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

        if(GL20.glGetShader(shaderID, GL20.GL_COMPILE_STATUS) ==
                GL11.GL_FALSE) {
            System.err.println("Could not compiler shader.");
            System.exit(-1);
        }

        this.exitOnGLError("loadShader");

        return shaderID;
    }

    public GameState onFrame(GameState s) {
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

    private TextureManager texs(Config.getManifestLocation());
}
