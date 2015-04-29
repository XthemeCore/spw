package f2.spw;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;

public class HealthBar {
	int x;
	int y;
	int width = 13;
	int height = 214;
	int offsetY = 0;
	BufferedImage image;
	BufferedImage background;
	
	public HealthBar(int x, int y) {
		this.x = x;
		this.y = y;
		try{
			InputStream stream = getClass().getResourceAsStream("/f2/spw/Graphics/healthbar.png");
            image = ImageIO.read(stream);
			InputStream streambackground = getClass().getResourceAsStream("/f2/spw/Graphics/healthbarback.png");
			background = ImageIO.read(streambackground);
        }
        catch (IOException e){
            e.printStackTrace(); 
        }
	}

	public void updateOffset(int maxHealth,int health){
		offsetY = height - (height*health/maxHealth);
	}

	public void draw(Graphics2D g){
		g.drawImage(background, x, y, x + width, y + height, 0, 0, width, height, null);
		g.drawImage(image, x, y, x + width, y + height - offsetY, 0, 0, width, height - offsetY, null);
	}
}