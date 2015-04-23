package f2.spw;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;

public class Bullet extends LivingEntity{
	public static final int Y_TO_DIE = 0;
	
	private int step = 16;

	public Bullet(int x, int y) {
		super(x, y, 6, 16,1,2,"bullet.png");
	}

	@Override
	public void draw(Graphics2D g) {
		g.drawImage(sprites[0], x, y, width , height, null);	
	}

	public void proceed(){
		y -= step;
		if(y < Y_TO_DIE){
			alive = false;
		}
	}
}