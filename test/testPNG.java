import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.nio.file.Files;

import java.util.Scanner;
import java.io.File;
import java.lang.Exception;
import java.lang.System;

public class testPNG {
    final static char [] lut="0123456789abcdef".toCharArray();

    static String toHex(int a) {
        char[] out=new char[8];
        out[7]=lut[a&0x0f];
        out[6]=lut[(a>>4)&0x0f];
        out[5]=lut[(a>>8)&0x0f];
        out[4]=lut[(a>>12)&0x0f];
        out[3]=lut[(a>>16)&0x0f];
        out[2]=lut[(a>>20)&0x0f];
        out[1]=lut[(a>>24)&0x0f];
        out[0]=lut[(a>>28)&0x0f];

        return new String(out);
    }

    public static void main(String [] args) {
        try {
            BufferedImage img = ImageIO.read(new File("test.png"));
            System.out.println("h: "+img.getHeight()+", w: "+img.getWidth());
            Scanner i = new Scanner(System.in);

            int x = 0, y = 0;
            while(x!= -1 && y != -1) {
                x = i.nextInt();
                y = i.nextInt();

                if(x < 0 || x > img.getWidth() || y < 0 || y > img.getHeight())
                    continue;

                int val = img.getRGB(x, y);
                // ARGB
                System.out.println("rgba:"+ testPNG.toHex(val));
            }
        } catch (Exception e) {
            System.err.println("Exception occurred: "+e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
