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
	private Audio shot;
	private Audio explode;
	private Audio start;
	private Audio sample;
	
	private Timer timer;
	private Timer timer_player;
	private Timer timer_bullet;
	
	private long score = 0;
	private int stage;
	private double difficulty;
	public static int maxstage = 10;

	//Keys for Left, Right, Up, Down, Spacebar.
	private boolean keys[] = {false,false,false,false,false};
	private boolean events[] = {true,false,false};
	
	public GameEngine(GamePanel gp, SpaceShip v) {
		this.gp = gp;
		this.v = v;
		sample = new Audio("sample.mid");
		shot = new Audio("gunshot.mid");
		explode = new Audio("explode.mid");
		start = new Audio("start.mid");

		gp.sprites.add(v);
		
		timer = new Timer(50, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				process();
				stageUp();
				win();
				isTitle();
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
				shot.start();
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
		Enemy e = new Enemy((int)(Math.random()*366), 0);
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

	private void process_compare(){
		if(score >= 99999999){
			events[2] = true;
		}
		else {
			switch(stage){
				case 1:	calStage(1,1000);
				case 2:	calStage(2,5000);
				case 3:	calStage(3,10000);
				case 4:	calStage(4,50000);
				case 5:	calStage(5,100000);
				case 6:	calStage(6,500000);
				case 7:	calStage(7,1000000);
				case 8:	calStage(8,5000000);
				case 9:	calStage(9,10000000);
			}
		}
	}
	
	private void process(){
		if(Math.random() < difficulty){
			generateEnemy();
		}

		process_compare();
		
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
						explode.start();
						e.setAlive(false);
						b.setAlive(false);
						score+= 100 * Math.pow(2,stage - 1);
				}
			}
		}
	}

	private void stageUp(){		
		if(events[1]){
			if(stage > 0)
				sample.pause();
				start.start();
			if(stage < maxstage)
				setStage(++stage);

			Iterator<Enemy> e_iter = enemies.iterator();
			while(e_iter.hasNext()){
				Enemy e = e_iter.next();
			
				if(e.isAlive()){
					e.setAlive(false);
				}
			}

			timer.stop();
			timer_player.stop();
			timer_bullet.stop();
		
			gp.updateGameUI(this,3);
		}
	}

	private void startScreen(){
		events[0] = false;
		gp.updateGameUI(this);
		resetGame();
		timer.start();
		timer_player.start();
	}

	private void continueScreen(){
		sample.play();
		events[1] = false;
		gp.updateGameUI(this);
		timer.start();
		timer_player.start();
		timer_bullet.start();
	}

	private void clearScreen(){
		if(events[2])
			events[2] = false;
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
		v.setToOrigin();
		events[0] = true;

		gp.updateGameUI(this);

		timer.start();
		timer_player.start();
	}

	private void resetGame(){
		setStage(0);
		score = 0;
		events[1] = true;
	}

	private void isTitle(){
		if(events[0]){
			if(sample.isPlaying())
				sample.play();
			else
				sample.start();
			gp.updateGameUI(this,0);

			timer.stop();
			timer_player.stop();
		}
	}
	
	private void win(){
		if(events[2]){
			Iterator<Enemy> e_iter = enemies.iterator();
			while(e_iter.hasNext()){
				Enemy e = e_iter.next();
			
				if(e.isAlive()){
					e.setAlive(false);
				}
			}

			timer.stop();
			timer_player.stop();
			timer_bullet.stop();

			gp.updateGameUI(this,4);
		}
	}

	private void die(){
		v.setAlive(false);
		setStage(1);
		
		timer.stop();
		timer_player.stop();
		timer_bullet.stop();

		gp.updateGameUI(this,2);
	}

	private void setStage(int stage){
		this.stage = stage;
		difficulty = 0.1 * stage;
	}

	private void calStage(int stage,long stagescore){
		if(score >= stagescore){
			events[1] = true;
		}
	}
	
	private void onPressed(KeyEvent e) {
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
		case KeyEvent.VK_SHIFT:
			keys[4] = true;
			break;
		case KeyEvent.VK_C:
			events[1] = true;
			break;
		case KeyEvent.VK_ESCAPE:
			System.exit(0);
			break;
		}
	}

	private void onReleased(KeyEvent e) {
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
		case KeyEvent.VK_SHIFT:
			keys[4] = false;
			break;
		}
	}

	public long getScore(){
		return score;
	}

	public int getStage(){
		return stage;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if(timer.isRunning())
			onPressed(e);
		else if(e.getKeyCode() == KeyEvent.VK_ENTER){
			if(events[0]){				
				startScreen();
			}
			else if(events[1]){				
				
				continueScreen();
			}
			else if(events[2]){				
				clearScreen();
			}
			else{
				clearScreen();
			}
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
