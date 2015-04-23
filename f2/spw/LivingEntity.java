package f2.spw;

import java.awt.Color;
import java.awt.Graphics2D;

public abstract class LivingEntity extends Sprite {

	int step;
	protected boolean alive = true;

	public LivingEntity(int x, int y, int width, int height,int row,int column,String src) {
		super(x, y, width, height, row, column, src);	
	}

	public boolean isAlive(){
		return alive;
	}

	public void setAlive(boolean alive){
		this.alive = alive;
	}
}
