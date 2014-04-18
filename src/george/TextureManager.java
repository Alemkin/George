package george;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;

import java.lang.Math;
import java.lang.Exception;

import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Vector;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;

public class TextureManager {

    private class ManifestEntry {
        final public String name;
        final public BufferedImage b;
        final public int x, y, w, h;
        final public int idx;

        public ManifestEntry(BufferedImage buf, String nm, int index, 
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
        textureLegend = new HashMap<String, Vector<Pair<Integer, Rectangle>>>();
        textures = new Vector<BufferedImage>();
    }

    //TODO: Refactor into smaller chunks, memoize results for later loading
    public void load(String manifest) throws Exception {

        int maxSize = GL11.glGetInteger(GL11.GL_MAX_TEXTURE_SIZE);
        int count = 0;
        BufferedReader manifestBuffer = new BufferedReader(
                new FileReader(manifest));
        List <ManifestEntry> boxFrames = 
            new LinkedList<ManifestEntry>(); //Maybe convert to LinkedList later?
                    /* source image, text, position, width/height */

        String ln = manifestBuffer.readLine();

        while(ln != null) {
            String [] vars = ln.split(",");
            try {
                BufferedImage img = ImageIO.read(new File(vars[0]));

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

                    boxFrames.add(j, e);
                }

            } catch (IOException e) {
                //TODO: Something here.
            }

            //Get the next file and entries.
            ln = manifestBuffer.readLine();
        }


        //While there's still frames to add..
        while(boxFrames.size() > 0) {
            Vector<Pair<Integer, Integer>> unpackedArea =
                new Vector<Pair<Integer, Integer>>();
            unpackedArea.add(new Pair<Integer, Integer>(0, 0));
            //Describes the top edge of the unpacked space.

            BufferedImage currentTexture = new BufferedImage(maxSize, maxSize, 
                    BufferedImage.TYPE_4BYTE_ABGR);
            //And while not every one has been tried in this current texture..
            List <Pair<ManifestEntry, Pair<Integer, Integer>>> packedPos = 
                new LinkedList<Pair<ManifestEntry, Pair<Integer, Integer>>>();
            
            for(ManifestEntry e : boxFrames) {
                //For each sprite, find all the possible places for it
                //to fit
                List<Pair<Integer, Integer>> tlCorners = 
                    new LinkedList<Pair<Integer, Integer>>();

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
                        tlCorners.add(new Pair<Integer, Integer>(cnr.fst(), maxY));
                    }
                }
                    //All of this can be done in literally two lines
                    // of Haskell or Python >.>

                if(tlCorners.size() > 0) {
                    Pair<Integer, Integer> bestPos = tlCorners.get(0);
                    //Now with that list find the best place to put it.
                    //I'll define that as the corner with the lowest x and y
                    //values, with lowest y a priority
                    for(Pair<Integer, Integer> newPos : tlCorners) {
                        if(newPos.snd() < bestPos.snd() ||
                                (newPos.snd() == best.snd() &&
                                 newPos.fst() < best.fst())) {
                            bestPos = newPos;
                        }
                    }

                    packedPos.add(new Pair<ManifestEntry,Pair<Integer,Integer>>(e, bestPos));
                    int endY = bestPos.y;

                    //Now to update the edge list
                    //Find the ones that the new packing box will overwrite
                    Vector<Pair<Integer, Integer>> toRemove = 
                        new Vector<Pair<Integer, Integer>>();
                    for(Pair<Integer, Integer> p : unpackedArea) {
                        if(p.fst() >= bestPos.x && 
                                p.fst() < bestPos.x+e.w) {
                            toRemove.add(p);
                            endY = p.snd();
                        }
                    }

                    //Add the starting and ending edges for the new box
                    int before = 0;
                    while(before < unpackedArea.size() && 
                            unpackedArea.get(before).fst() <= bestPos.x) {
                        before++;
                    }
                    unpackedArea.add(before, new Pair<Integer, Integer>(bestPos.x,
                                bestPos.y));

                    int after = before+1;
                    while(after < unpackedArea.size() &&
                            unpackedArea.get(after).fst() <= 
                            bestPos.x + e.w) {
                        after++;
                    }
                    unpackedArea.add(after, new Pair<Integer, Integer>(
                                bestPos.x + e.w,
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
            for(Pair<ManifestEntry, Pair<Integer, Integer>> s : packedPos) {
                canvas.setRect(s.snd().x, s.snd().y,
                        s.fst().b.getData(s.fst().getRect())
                );
            }

            byte [] ary = ((DataBufferByte) currentTexture.getData().
                    getDataBuffer()).getData();

            int texId = GL11.glGenTextures();
            GL13.glActiveTexture(imageUnits[count]);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
            GL11.glTexParameteri(texId, GL12.GL_TEXTURE_BASE_LEVEL, 0);
            GL11.glTexParameteri(texId, GL12.GL_TEXTURE_MAX_LEVEL, 0);
            GL11.glTexImage2D(texId, 0, GL11.GL_RGBA8, maxSize, maxSize, 0, 
                    GL11.GL_RGBA, GL12.GL_UNSIGNED_INT_8_8_8_8, 
                    ByteBuffer.wrap(ary));
            //TODO: Check to make sure this is correct.

            for(Pair<ManifestEntry, Pair<Integer, Integer>> s : packedPos) {
                if(textureLegend.get(s.fst().name) == null) {
                    textureLegend.put(s.fst().name, 
                            new Vector<Pair<Integer, Rectangle>>());
                }

                textureLegend.get(s.fst().name).add(new Pair<Integer, Rectangle>(
                            count, new Rectangle(s.snd().x, s.snd().y, 
                                s.fst().w, s.fst().h)));

                boxFrames.remove(s.fst());
            }
            count++;
        }
    }

    Vector<Pair<Integer, Rectangle>> get(String str) {
        return textureLegend.get(str);
    }


    private HashMap<String, Vector<Pair<Integer, Rectangle>>> textureLegend;
    private Vector<BufferedImage> textures;
    //Each sprite name with its corresponding texture and location within the texture.
    final static int [] imageUnits = {GL13.GL_TEXTURE0, GL13.GL_TEXTURE1, 
        GL13.GL_TEXTURE2, GL13.GL_TEXTURE3, GL13.GL_TEXTURE4,
        GL13.GL_TEXTURE5, GL13.GL_TEXTURE6, GL13.GL_TEXTURE7};
}
