package george;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.nio.file.Files;
import java.util.List;

public class TextureManager {
    public TextureManger(String manifest) {
        String [] lns = new String(Files.readAllBytes(manifest)).split("\n");
        textLegend = new HashMap();
    }

    private HashMap<String, Vector<Pair<Int, Vec2>> textureLegend;
    //Each sprite name with its corresponding texture and location within the texture.
}
