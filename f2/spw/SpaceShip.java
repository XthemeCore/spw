package f2.spw;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Graphics;

public class SpaceShip extends Sprite{

	int step = 1;
	private boolean alive = true;
	
	public SpaceShip(int x, int y, int width, int height) {
		super(x, y, width, height);
		
	}

	@Override
	public void draw(Graphics2D g) {
		if(isAlive())
			g.drawImage(sprites[0], x, y, width , height, null);
		else
			g.drawImage(sprites[2], x, y, width , height, null);		
	}

	public void move(int dx,int dy){
		x += (step * dx);
		y += (step * dy);
		if(x < 0)
			x = 0;
		if(x > 384 - width)
			x = 384 - width;
		if(y < 0)
			y = 0;
		if(y > 612 - height)
			y = 612 - height;
	}

	public boolean isAlive(){
		return alive;
	}

	public void setAlive(boolean alive){
		this.alive = alive;
	}

	public void setToOrigin(){
		x = 180;
		y = 550;
	}
}
