package f2.spw;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Graphics;

public class SpaceShip extends Sprite{

	int step = 1;
	
	public SpaceShip(int x, int y, int width, int height) {
		super(x, y, width, height);
		
	}

	@Override
	public void draw(Graphics2D g) {
		g.drawImage(sprites[0], x, y, width , height, null);		
	}

	public void move(int dx,int dy){
		x += (step * dx);
		y += (step * dy);
		if(x < 0)
			x = 0;
		if(x > 400 - width)
			x = 400 - width;
		if(y < 0)
			y = 0;
		if(y > 650 - height)
			y = 650 - height;
	}

}
