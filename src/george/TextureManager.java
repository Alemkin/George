package george;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import javax.imageio.ImageIO;
import java.util.List;

import org.lwjgl.opengl.GL11;

public class TextureManager {

    public TextureManger(String manifest) {
        textLegend = new HashMap();

        int maxSize = GL11.glGetInteger(GL11.GL_MAX_TEXTURE_SIZE);
        BufferedReader manifestBuffer = new BufferedReader(
                new FileReader(manifest));
        BufferedImage currentTexture = new BufferedImage(maxSize, maxSize, 
                BufferedImage.TYPE_4BYTE_ABGR);
        ArrayList <Pair<BufferedImage, Pair<Vec2, Vec2>> boxFrames = 
            new ArrayList(); //Maybe convert to LinkedList later?
                    /* source image, position, width/height */

        Str ln = manifestBuffer.readLine();

        while(ln != null) {
            String [] vars = ln.split(",");
            try {
                BufferedImage img = ImageIO.read(vars[0]);

                for (int i=1;i<vars.length; i+=4) {
                    Pair newFrame = new Pair(img, 
                            new Pair(
                                new Vec2(Integer.parseInt(vars[i]),
                                    Integer.parseInt(vars[i+1])), 
                                new Vec2(Integer.parseInt(vars[i+2]),
                                    Integer.parseInt(vars[i+3]))));
                    int j = 0;

                    while(j < boxFrames.size() ) {           //Linear search for the best spot. Will replace with binary search later.
                        float other = U.fmax(boxFrames.get(j).snd().snd().x,
                                bowFrames.get(j).snd().snd().y);
                        float current = U.fmax(newFrame.snd().snd().x,
                                newFrame.snd().snd().y);
                        if(other > current) {
                            j++;
                        } else {
                            break;
                        }
                    }

                    boxFrame.add(j, newFrame);
                }

            } catch (IOException e) {
                //TODO: Something here.
            }
        }

        Vector<Pair<Integer, Integer>> topEdge = new Vector();
        //Describes the top edge of the available space, approximately.
        topEdge.add(new Pair(0, 0));
        //TODO: greedily pack into the texture image
        //While there's still frames to add..
        while(boxFrame.size() > 0) {
            //And while not every one has been tried in this current texture..
            ArrayList <Pair<BufferedImage, Pair<Vec2, Vec2>> copy = 
                new ArrayList(boxFrame);

            for(Pair<BufferedImage, Pair<Vec2, Vec2>> frame : copy) {
                //Find a good spot for it
                //And by good spot I mean the first space it fits
                //Try to the right of the current spot, then below it
                for(Pair<Integer, Integer> p : topEdge) {
                }
            }
        }

        ln = manifestBuffer.readLine();

    }


    private HashMap<String, Vector<Pair<Int, Vec2>> textureLegend;
    //Each sprite name with its corresponding texture and location within the texture.
}
