package f2.spw;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;

public class Enemy extends LivingEntity{
	public static final int Y_TO_DIE = 600;
	
	private int step = 12;
	private int scanY = 75;
	private int scanX = 75;

	private boolean detected = false;
	
	public Enemy(int x, int y) {
		super(x, y, 32, 32, 1, 4, "sprite.png");		
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

	public void scan(LivingEntity v){
		if(v instanceof BulletPlayer){
			if(y - v.y >= 0 && y - v.y <= scanY){
					if(x < v.x && x > step/4 && v.x - x <= scanX)
						x-= step/4;
					else if(x < v.x && v.x - x <= scanX)
						x+= step/4;
					else if(x > v.x && x < 366 - step/4 && x - v.x <= scanX)
						x+=step/4;
					else if(x > v.x && x - v.x <= scanX)
						x-=step/4;
					else if(x == v.x) 
						if(Math.random() < 0.5 && x < 366 - step/4)
							x+= step/4;
						else if(x > 0)
							x-= step/4;
						else 
							x+= step/4;
			}
		}
		else if(v instanceof Player){
			setDetect(false);
			if(x < v.x && x > step/2 && v.x - x <= scanX){
				if(Math.random() < 0.2)
					x-= step/8;
				else
					setDetect(true);
			}
			else if(x > v.x && x < 366 - step/2 && x - v.x <= scanX){
				if(Math.random() < 0.2)
					x+= step/8;
				else
					setDetect(true);
			}
			else if(x == v.x)
				setDetect(true);
		}
	}

	public void setDetect(boolean detected){
		this.detected = detected;
	}

	public boolean isDetect(){
		return detected;
	}
}