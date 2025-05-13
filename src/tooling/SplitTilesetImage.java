package tooling;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SplitTilesetImage {

    public static void main(String[] args) throws SlickException, IOException {

        File inputFile = new File("data/maps/tiles.png");
        BufferedImage image = ImageIO.read(inputFile);
        int height = image.getHeight();
        int chunkSize = 5760;

        if (height > chunkSize * 2) {
            throw new RuntimeException("Cannot split image in two chunks since the image height is bigger than two chunks together.");
        }

        BufferedImage image1 = image.getSubimage(0, 0, image.getWidth(), chunkSize);
        BufferedImage image2 = image.getSubimage(0, chunkSize, image.getWidth(), image.getHeight() - chunkSize);

        ImageIO.write(image1, "png", new File("data/tileset00000.png"));
        ImageIO.write(image2, "png", new File("data/tileset00001.png"));

    }

}
