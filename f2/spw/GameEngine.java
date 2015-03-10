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
	
	private Timer timer;
	private Timer timer_player;
	private Timer timer_bullet;
	
	private long score = 0;
	private double difficulty = 0.1;

	//Keys for Left, Right, Up, Down, Spacebar.
	private boolean keys[] = {false,false,false,false,false};
	
	public GameEngine(GamePanel gp, SpaceShip v) {
		this.gp = gp;
		this.v = v;		
		
		gp.sprites.add(v);
		
		timer = new Timer(50, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				process();
			}
		});
		timer_player = new Timer(10, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				process_player();	
			}
		});
		timer_bullet = new Timer(150, new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				generateBullet();	
			}
		});
		timer.setRepeats(true);
		timer_player.setRepeats(true);
		timer_bullet.setRepeats(true);
	}
	
	public void start(){
		timer.start();
		timer_player.start();
	}
	
	private void generateEnemy(){
		Enemy e = new Enemy((int)(Math.random()*390), 0);
		gp.sprites.add(e);
		enemies.add(e);
	}

	private void generateBullet(){
		Bullet b = new Bullet(v.x + (v.width/2) - 2, v.y);
		gp.sprites.add(b);
		bullets.add(b);
	}

	private void process_player(){
		if(keys[0])
			v.move(-1,0);
		else if(keys[1])
			v.move(1,0);

		if(keys[2])
			v.move(0,-1);
		else if(keys[3])
			v.move(0,1);

		if(keys[4]){
			timer_bullet.start();
		}else{			
			timer_bullet.stop();
		}
	}
	
	private void process(){
		if(Math.random() < difficulty){
			generateEnemy();
		}
		
		Iterator<Enemy> e_iter = enemies.iterator();
		while(e_iter.hasNext()){
			Enemy e = e_iter.next();
			e.proceed();
			
			if(e.y >= e.Y_TO_DIE){
				e_iter.remove();
				gp.sprites.remove(e);
			}
		}

		Iterator<Bullet> b_iter = bullets.iterator();
		while(b_iter.hasNext()){
			Bullet b = b_iter.next();
			b.proceed();
			
			if(!b.isAlive()){
				b_iter.remove();
				gp.sprites.remove(b);
			}
		}
		
		gp.updateGameUI(this);
		
		Rectangle2D.Double vr = v.getRectangle();
		Rectangle2D.Double er;
		Rectangle2D.Double br;
		for(Enemy e : enemies){
			er = e.getRectangle();
			if(er.intersects(vr) && e.isAlive()){				
				die();
				return;
			}
			for(Bullet b : bullets){
				br = b.getRectangle();
				if(br.intersects(er) && e.isAlive()){
						e.setAlive(false);
						b.setAlive(false);
						score+= 100;
				}
			}
		}
	}

	private void clear(){		
		Iterator<Enemy> e_iter = enemies.iterator();
		while(e_iter.hasNext()){
			Enemy e = e_iter.next();
			e.proceed();
			
			if(e.isAlive() || e.y < e.Y_TO_DIE){
				e_iter.remove();
				gp.sprites.remove(e);
			}
		}
		Iterator<Bullet> b_iter = bullets.iterator();
		while(b_iter.hasNext()){
			Bullet b = b_iter.next();
			b.proceed();
			
			if(b.isAlive()){
				b_iter.remove();
				gp.sprites.remove(b);
			}
		}
		v.setAlive(true);
		score = 0;		
		v.x = 180;
		v.y = 550;

		gp.updateGameUI(this);

		timer.start();
		timer_player.start();
	}
	
	public void die(){
		v.setAlive(false);
		difficulty = 0.1;

		timer.stop();
		timer_player.stop();
		timer_bullet.stop();

		gp.updateGameUI(this,1);
	}
	
	void onPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_A:
			keys[0] = true;
			break;
		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_D:
			keys[1] = true;
			break;
		case KeyEvent.VK_UP:
		case KeyEvent.VK_W:
			keys[2] = true;
			break;
		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_S:
			keys[3] = true;
			break;
		case KeyEvent.VK_SPACE:
		case KeyEvent.VK_ENTER:
			keys[4] = true;
			break;
		case KeyEvent.VK_C:
			difficulty += 0.1;
			break;
		case KeyEvent.VK_ESCAPE:
			System.exit(0);
			break;
		}
	}

	void onStop(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_ESCAPE:
			System.exit(0);
			break;
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_UP:
		case KeyEvent.VK_DOWN:
			break;
		default:
			clear();
			break;
		}
	}

	void onReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_A:
			keys[0] = false;
			break;
		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_D:
			keys[1] = false;
			break;
		case KeyEvent.VK_UP:
		case KeyEvent.VK_W:
			keys[2] = false;
			break;
		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_S:
			keys[3] = false;
			break;
		case KeyEvent.VK_SPACE:
		case KeyEvent.VK_ENTER:
			keys[4] = false;
			break;
		}
	}

	public long getScore(){
		return score;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if(timer.isRunning())
			onPressed(e);
		else{				
			onStop(e);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		onReleased(e);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		//do nothing		
	}
}
