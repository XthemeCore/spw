package f2.spw;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.Timer;


public class GameEngine implements KeyListener, GameReporter{
	GamePanel gp;
		
	private ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	private ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	private SpaceShip v;	
	
	private Audio bgm;

	private Timer timer;
	
	private long score = 0;
	private double difficulty = 0.1;

	private boolean[] keys = {false,false,false,false,false}; 
	
	public GameEngine(GamePanel gp, SpaceShip v) {
		this.gp = gp;
		this.v = v;	
		bgm = new Audio("sample.mid");
		bgm.play(true);
		
		gp.sprites.add(v);
		
		timer = new Timer(50, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				process();
			}
		});
		timer.setRepeats(true);
		
	}
	
	public void start(){
		timer.start();
	}
	
	private void generateEnemy(){
		Enemy e = new Enemy((int)(Math.random()*390), 30);
		gp.sprites.add(e);
		enemies.add(e);
	}

	private void generateBullet(){
		Bullet b = new Bullet(v.x + (v.width/2) - 2, v.y);
		gp.sprites.add(b);
		bullets.add(b);
	}

	private void checkMove(){
		if(keys[0])
			v.move(1);
		else if(keys[1])
			v.move(0);
		if(keys[2])
			v.move(3);
		else if(keys[3])
			v.move(2);
		if(keys[4])
			generateBullet();
	}
	
	private void process(){
		checkMove();
		if(Math.random() < difficulty){
			generateEnemy();
		}
		
		Iterator<Enemy> e_iter = enemies.iterator();
		Iterator<Bullet> b_iter = bullets.iterator();
		while(e_iter.hasNext()){
			Enemy e = e_iter.next();
			e.proceed();
			
			if(!e.isAlive()){
				e_iter.remove();
				gp.sprites.remove(e);
			}
		}
		while(b_iter.hasNext()){
			Bullet b = b_iter.next();
			b.proceed();
		
			if(!b.isAlive()){
				b_iter.remove();
				gp.sprites.remove(b);
			}
		}
		
		gp.updateGameUI(this);
		bgm.updatePlay();
		
		Rectangle2D.Double vr = v.getRectangle();
		Rectangle2D.Double er;
		Rectangle2D.Double br;
		for(Enemy e : enemies){
			er = e.getRectangle();
			if(er.intersects(vr) && e.isAlive()){
				die();
				return;
			}
			else{
				for(Bullet b : bullets){
					br = b.getRectangle();
					if(br.intersects(er) && b.isAlive() && e.isAlive()){
							gp.sprites.remove(b);
							gp.sprites.remove(e);
							e.setAlive(false);
							b.setAlive(false);
							score+= 100;
					}
				}
			}
		}
	}
	
	public void die(){
		timer.stop();

	}
	
	void controlVehicle(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			keys[0] = true;
			break;
		case KeyEvent.VK_RIGHT:
			keys[1] = true;
			break;
		case KeyEvent.VK_UP:
			keys[2] = true;
			break;
		case KeyEvent.VK_DOWN:
			keys[3] = true;
			break;
		case KeyEvent.VK_SPACE:
			keys[4] = true;
			break;
		case KeyEvent.VK_D:
			difficulty += 0.1;
			break;
		}
	}

	public long getScore(){
		return score;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		controlVehicle(e);
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			keys[0] = false;
			break;
		case KeyEvent.VK_RIGHT:
			keys[1] = false;
			break;
		case KeyEvent.VK_UP:
			keys[2] = false;
			break;
		case KeyEvent.VK_DOWN:
			keys[3] = false;
			break;
		case KeyEvent.VK_SPACE:
			keys[4] = false;
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		//do nothing		
	}
}
