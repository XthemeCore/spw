package f2.spw;

import java.awt.Color;
import java.awt.Graphics2D;

public class SpaceShip extends Sprite{

	int step = 8;
	
	public SpaceShip(int x, int y, int width, int height) {
		super(x, y, width, height);
		
	}

	@Override
	public void draw(Graphics2D g) {
		g.drawImage(sprites[0], x, y, width , height, null);		
	}

	public void move(int direction){
		switch(direction){
			case 0:	x += step; 
					break;
			case 1: x -= step; 
					break;
			case 2: y += step; 
					break;
			case 3: y -= step; 
					break;
		}
		if(x < 0)
			x = 0;
		if(x > 384 - width)
			x = 384 - width;
		if(y < 0)
			y = 0;
		if(y > 612 - height)
			y = 612 - height;
	}

}
