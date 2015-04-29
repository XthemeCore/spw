package f2.spw;

import java.awt.Color;
import java.awt.Graphics2D;

public abstract class LivingEntity extends Sprite {

	int maxHealth = 1;
	int health;

	protected boolean alive = true;

	public LivingEntity(int x, int y, int width, int height,int row,int column,String src) {
		super(x, y, width, height, row, column, src);
		health = maxHealth;
	}

	public boolean isAlive(){
		updateAlive();
		return alive;
	}

	public void updateAlive(){
		if(health > 0)
			setAlive(true);
		else
			setAlive(false);
	}

	public void setAlive(boolean alive){
		this.alive = alive;
		if(alive && health <= 0)
			health = maxHealth;
		else if(!alive)
			health = 0;
	}
}