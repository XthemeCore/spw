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
	private SpaceShip v;	
	
	private Timer timer;
	private Timer timer_player;
	
	private long score = 0;
	private double difficulty = 0.1;

	//Keys for Left, Right, Up, Down.
	private boolean keys[] = {false,false,false,false};
	
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
		timer.setRepeats(true);
		timer_player.setRepeats(true);
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

	private void process_player(){
		if(keys[0])
			v.move(-1,0);
		else if(keys[1])
			v.move(1,0);

		if(keys[2])
			v.move(0,-1);
		else if(keys[3])
			v.move(0,1);
	}
	
	private void process(){
		if(Math.random() < difficulty){
			generateEnemy();
		}
		
		Iterator<Enemy> e_iter = enemies.iterator();
		while(e_iter.hasNext()){
			Enemy e = e_iter.next();
			e.proceed();
			
			if(!e.isAlive()){
				e_iter.remove();
				gp.sprites.remove(e);
				score += 100;
			}
		}
		
		gp.updateGameUI(this);
		
		Rectangle2D.Double vr = v.getRectangle();
		Rectangle2D.Double er;
		for(Enemy e : enemies){
			er = e.getRectangle();
			if(er.intersects(vr)){
				die();
				return;
			}
		}
	}

	private void clear(){		
		Iterator<Enemy> e_iter = enemies.iterator();
		while(e_iter.hasNext()){
			Enemy e = e_iter.next();
			e.proceed();
			
			if(e.isAlive()){
				e_iter.remove();
				gp.sprites.remove(e);
			}
		}
		score = 0;		
		v.x = 180;
		v.y = 550;

		gp.updateGameUI(this);

		timer.start();
		timer_player.start();
	}
	
	public void die(){
		timer.stop();
		timer_player.stop();

		gp.updateGameUI(this,1);
	}
	
	void onPressed(KeyEvent e) {
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
		case KeyEvent.VK_D:
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
