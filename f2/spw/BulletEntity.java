package f2.spw;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;

public abstract class BulletEntity extends LivingEntity{
	public BulletEntity(int x, int y) {
		super(x, y, 6, 16,1,2,"bullet.png");
	}

	abstract public void proceed();
}