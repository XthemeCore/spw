package f2.spw;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;

public abstract class Sprite {
	int x;
	int y;
	int width;
	int height;
	int row;
	int column;
	BufferedImage image;
	BufferedImage[] sprites;
	
	public Sprite(int x, int y, int width, int height) {
		this(x,y,width,height,1,2,"sprite.png");
	}

	public Sprite(int x, int y, int width, int height,int row,int column,String src) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		sprites = new BufferedImage[row * column];
		try{
            File file = new File("f2/spw/Graphics/" + src);
            image = ImageIO.read(file);

        }
        catch (IOException e){
            e.printStackTrace();
        }
		int imageWidth  = image.getWidth()/column;
		int imageHeight = image.getHeight()/row;
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {
				sprites[(i * column) + j] = image.getSubimage(j * imageWidth, i * imageHeight, imageWidth, imageHeight);
			}
		}
	}

	abstract public void draw(Graphics2D g);
	
	public Double getRectangle() {
		return new Rectangle2D.Double(x, y, width, height);
	}
}
