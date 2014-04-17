package george;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;

public class TextureManager {

    private class ManifestEntry {
        final public String name;
        final public ImageBuffer b;
        final public int x, y, w, h;
        final public int idx;

        public ManifestEntry(ImageBuffer buf, String nm, int index, 
                int xx, int yy, 
                int ww, int hh) {
            name = nm;
            b = buf;
            x = xx;
            y = yy;
            w = ww;
            h = hh;
            idx = index;
        }

        public Rectangle getRect() {
            return new Rectangle(x, y, w, h);
        }
    }

    public TextureManager() {
        textLegend = new HashMap();
        textures = new Vector();
    }

    public void load(String manifest) {

        final static int [] imageUnits = {GL13.GL_TEXTURE0, GL13.GL_TEXTURE1, 
            GL13.GL_TEXTURE2, GL13.GL_TEXTURE3, GL13.GL_TEXTURE4,
            GL13.GL_TEXTURE5, GL13.GL_TEXTURE6, GL13.GL_TEXTURE7};

        int maxSize = GL11.glGetInteger(GL11.GL_MAX_TEXTURE_SIZE);
        BufferedReader manifestBuffer = new BufferedReader(
                new FileReader(manifest));
        ArrayList <ManifestEntry> boxFrames = 
            new ArrayList(); //Maybe convert to LinkedList later?
                    /* source image, text, position, width/height */

        Str ln = manifestBuffer.readLine();

        while(ln != null) {
            String [] vars = ln.split(",");
            try {
                BufferedImage img = ImageIO.read(vars[0]);

                int idx=0;
                for (int i=1;i<vars.length; i+=5) {
                    ManifestEntry e = new ManifestEntry(
                            img,
                            vars[i],
                            idx,
                            Integer.parseInt(vars[i+1]),
                            Integer.parseInt(vars[i+2]),
                            Integer.parseInt(vars[i+3]),
                            Integer.parseInt(vars[i+4])
                    );
                    idx++;
                    if(e.w > maxSize || e.h > maxSize) {
                        System.err.println("ERROR: Sprite too large to draw.");
                        throw new Exception("Sprite "+vars[i]+" too large.");
                        //TODO: Solve this more elegantly
                    }

                    int j = 0;

                    //Insertion sort.
                    while(j < boxFrames.size()) {
                        //Linear search for the best spot. Will replace with binary search later.
                        //float other = U.fmax(boxFrames.get(j).w,
                        //        bowFrames.get(j).h);
                        //float current = U.fmax(e.w, e.h);
                        float other = boxFrames.get(j).h;
                        float current = e.h;
                        //Sort solely by h for now
                        if(other > current) {
                            j++;
                        } else {
                            break;
                        }
                    }

                    boxFrames.add(j, newFrame);
                }

            } catch (IOException e) {
                //TODO: Something here.
            }

            //Get the next file and entries.
            ln = manifestBuffer.readLine();
        }


        //TODO: greedily pack into the texture image
        //While there's still frames to add..
        while(boxFrames.size() > 0) {
            Vector<Pair<Integer, Integer>> unpackedArea = new Vector();
            unpackedArea.append(new Pair(0, 0));
            //Describes the top edge of the unpacked space.

            BufferedImage currentTexture = new BufferedImage(maxSize, maxSize, 
                    BufferedImage.TYPE_4BYTE_ABGR);
            //And while not every one has been tried in this current texture..
            List <Pair<ManifestEntry, Vec2>> packedPos = new LinkedList();
            
            for(ManifestEntry e : boxFrames) {
                //For each sprite, find all the possible places for it
                //to fit
                List<Vec2> tlCorners = new LinkedList();

                for(Pair<Integer, Integer> cnr : unpackedArea) {
                    int maxY = cnr.snd();

                    for(Pair<Integer, Integer> o : unpackedArea) {
                        if(o.fst() < cnr.fst()) {
                            continue;
                        }
                        if(o.fst() <= cnr.fst() + e.w) {
                            if(o.snd() > maxY) {
                                maxY = o.snd();
                            } else {
                                break;
                            }
                        }
                    }

                    if(maxY + e.h < maxSize && 
                            cnr.fst() + e.h < maxSize) {
                        tlCorners.append(new Vec2(cnr.fst(), maxY));
                    }
                }
                    //All of this can be done in literally two lines
                    // of Haskell or Python >.>

                if(tlCorners.size() > 0) {
                    Pair<ManifestEntry, Vec2> bestPos = tlCorners.get(0);
                    //Now with that list find the best place to put it.
                    //..which I'll do later.

                    packedPos.append(bestPos);
                    int endY = bestPos.snd().y;

                    //Now to update the edge list
                    //Find the ones that the new packing box will overwrite
                    Vector<Pair<Integer, Integer>> toRemove = new Vector();
                    for(Pair<Integer, Integer> p : unpackedArea) {
                        if(p.fst() >= bestPos.snd().x && 
                                p.fst() < bestPos.snd().x+bestPos.fst().w) {
                            toRemove.add(p);
                            endY = p.snd();
                        }
                    }

                    //Add the starting and ending edges for the new box
                    int before = 0;
                    while(before < unpackedArea.size() && 
                            unpackedArea.get(before).fst() <= bestPos.snd().x) {
                        before++;
                    }
                    unpackedArea.add(before, new Pair(bestPos.snd().x,
                                bestPos.snd().y));

                    int after = before+1;
                    while(after < unpackedArea.size() &&
                            unpackedArea.get(after).fst() <= 
                            bestPos.snd().x + bestPos.fst().w) {
                        after++;
                    }
                    unpackedArea.add(after, new Pair(
                                bestPos.snd().x + bestPos.fst().w,
                                endY));

                    //Remove them
                    for(Pair<Integer, Integer> p : toRemove) {
                        unpackedArea.remove(p);
                    }
                }
            }

            WritableRaster canvas = currentTexture.getRaster();

            //Pack all the textures into the frame, transfer to GPU,
            //log in hash table, and remove from available sprites list.
            for(Pair<ManifestEntry, Vec2> s : packedPos) {
                canvas.setRect(s.snd().x, s.snd().y,
                        s.fst().buf.getData(s.fst().getRect())
                );
            }

            int texId = GL11.glGenTextures();
            GL13.glActiveTexture(imageUnits[count]);
            GL11.glBindTexture(GL31.GL_TEXTURE_2d, texId);
            GL11.glTexParameteri(texId, GL12.GL_TEXTURE_BASE_LEVEL, 0);
            GL11.glTexParameteri(texId, GL12.GL_MAX_LEVEL, 0);
            GL11.glTexImage2D(texId, 0, GL11.GL_RGBA8, maxSize, maxSize, 0, 
                    GL11.GL_RGBA, GL11.GL_UNSIGNED_INT_8_8_8_8, 
                    ByteBuffer.wrap(canvas.getPixels(0, 0, maxSize, maxSize)));
            //TODO: Check to make sure this is correct.

            for(Pair<ManifestEntry, Vec2> s : packedPos) {
                if(textureLegend.get(s.fst().nm) == null) {
                    textureLegend.put(s.fst().nm, new Vector());
                }

                textureLegend.get(s.fst().nm).add(new Pair(
                            count, new Rectangle(s.snd().x, s.snd().y, 
                                s.fst().w, s.fst().h)));

                boxFrames.remove(s.fst());
            }
            count++;
        }
    }

    Vector<Pair<Int, Rectangle>> get(String str) {
        return textureLegend.get(str);
    }


    private HashMap<String, Vector<Pair<Int, Vec2>> textureLegend;
    private Vector<BufferedImaged> textures;
    //Each sprite name with its corresponding texture and location within the texture.
}
