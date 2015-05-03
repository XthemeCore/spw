package f2.spw;

import java.awt.Color;
import java.awt.Graphics2D;

public class Player extends LivingEntity{

	int step = 8;
	private String name = "Alex";

	protected long score = 0;
	protected int stage;
	
	public Player(int x, int y, int width, int height) {
		super(x, y, width, height, 1, 4,"sprite.png");
		maxHealth = 250;
		health = maxHealth;
	}

	@Override
	public void draw(Graphics2D g) {
		if(isAlive())
			g.drawImage(sprites[0], x, y, width , height, null);
		else
			g.drawImage(sprites[2], x, y, width , height, null);		
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
		if(x > 400 - width)
			x = 400 - width;
		if(y < 0)
			y = 0;
		if(y > 600 - height)
			y = 600 - height;
	}

	public void setToOrigin(){
		x = 180;
		y = 550;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}
}