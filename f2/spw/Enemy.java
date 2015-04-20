package f2.spw;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;

public class Enemy extends Sprite{
	public static final int Y_TO_DIE = 600;
	
	private int step = 12;
	private boolean alive = true;
	
	public Enemy(int x, int y) {
		super(x, y, 32, 32);
		
	}

	@Override
	public void draw(Graphics2D g) {
		if(isAlive())
			g.drawImage(sprites[1], x, y, width , height, null);
		else
			g.drawImage(sprites[2], x, y, width , height, null);	
	}

	public void proceed(){
		y += step;
		if(y > Y_TO_DIE){
			alive = false;
		}
	}
	
	public boolean isAlive(){
		return alive;
	}

	public void setAlive(boolean alive){
		this.alive = alive;
	}
}