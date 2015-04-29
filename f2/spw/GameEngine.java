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
	private ArrayList<BulletPlayer> bulletsPlayer = new ArrayList<BulletPlayer>();
	private ArrayList<BulletEnemy> bulletsEnemy = new ArrayList<BulletEnemy>();
	private Player v;	
	
	private Audio bgm;
	private Audio shotSE;
	private Audio explodeSE;

	private Timer timer;
	Counter c;
	private int maxTime = 50;
	
	private long score = 0;
	private double difficulty;
	private int stage;
	public static int maxstage = 10;

	private boolean[] keys = {false,false,false,false,false};
	private boolean events[] = {true,false,false,false};
	
	public GameEngine(GamePanel gp, Player v) {
		this.gp = gp;
		this.v = v;
		bgm = new Audio("sample.mid");
		shotSE = new Audio("gunshot.mid");
		explodeSE = new Audio("explode.mid");
		
		gp.sprites.add(v);
		
		c = new Counter();
		timer = new Timer(maxTime, new ActionListener() {
			
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
		Enemy e = new Enemy((int)(Math.random()*360), 0);
		gp.sprites.add(e);
		enemies.add(e);
	}

	private void generateBulletPlayer(){
		BulletPlayer bp = new BulletPlayer(v.x + (v.width/2) - 2, v.y);
		gp.sprites.add(bp);
		bulletsPlayer.add(bp);
	}

	private void generateBulletEnemy(LivingEntity n){
		BulletEnemy be = new BulletEnemy(n.x + (n.width/2) - 2, n.y + 32);
		gp.sprites.add(be);
		bulletsEnemy.add(be);
	}

	private void keyCompare(){
		if(keys[0])
			v.move(1);
		else if(keys[1])
			v.move(0);
		if(keys[2])
			v.move(3);
		else if(keys[3])
			v.move(2);
		if(keys[4])
			if(c.getCount()%4 == 0){
				shotSE.start();
				generateBulletPlayer();
			}
	}
	
	private void process(){
		if(c.getCount() < maxTime)
			c.count();
		else
			c.reset();
		
		if(Math.random() < difficulty){
			generateEnemy();
		}

		scoreProcess();
		keyCompare();
		eventsProcess();
		
		Iterator<Enemy> e_iter = enemies.iterator();
		Iterator<BulletPlayer> bp_iter = bulletsPlayer.iterator();
		while(e_iter.hasNext()){
			Enemy e = e_iter.next();
			e.proceed();
			e.scan(v);
			if(e.isDetect())
				if(c.getCount()%9 == 0){
					shotSE.start();
					generateBulletEnemy(e);
				}
			
			if(!e.isAlive()){
				e_iter.remove();
				gp.sprites.remove(e);
			}
		}
		while(bp_iter.hasNext()){
			BulletPlayer bp = bp_iter.next();
			bp.proceed();
		
			if(!bp.isAlive()){
				bp_iter.remove();
				gp.sprites.remove(bp);
			}
		}		
		Iterator<BulletEnemy>  be_iter = bulletsEnemy.iterator();
		while(be_iter.hasNext()){
			BulletEnemy be = be_iter.next();
			be.proceed();
		
			if(!be.isAlive()){
				be_iter.remove();
				gp.sprites.remove(be);
			}
		}
		
		
		Rectangle2D.Double vr = v.getRectangle();
		Rectangle2D.Double er;
		Rectangle2D.Double bpr;
		Rectangle2D.Double ber;
		for(Enemy e : enemies){
			er = e.getRectangle();			
			if(er.intersects(vr) && e.isAlive()){
				if(v.isAlive()){
					v.health -= 5;
					e.setAlive(false);
				}
				else
					die();
				return;
			}
			else{
				for(BulletPlayer bp : bulletsPlayer){
					bpr = bp.getRectangle();
					e.scan(bp);
					if(bpr.intersects(er) && bp.isAlive() && e.isAlive()){
							explodeSE.start();
							e.setAlive(false);
							bp.setAlive(false);
							score+= 100 * Math.pow(2,stage - 1);
					}
				}
			}
		}
		for(BulletEnemy be : bulletsEnemy){
			ber = be.getRectangle();
			if(ber.intersects(vr) && be.isAlive()){
				if(v.isAlive()){
					v.health -= 1;
					be.setAlive(false);
				}
				else
					die();
				return;
			}
		}
	}

	private void scoreProcess(){
		if(score >= 99999999){
			events[2] = true;
		}
		else {
			switch(stage){
				case 1:	scoreCompare(1,1000);
				case 2:	scoreCompare(2,5000);
				case 3:	scoreCompare(3,10000);
				case 4:	scoreCompare(4,50000);
				case 5:	scoreCompare(5,100000);
				case 6: scoreCompare(6,500000);
				case 7:	scoreCompare(7,1000000);
				case 8:	scoreCompare(8,5000000);
				case 9:	scoreCompare(9,10000000);
			}
		}
	}

	public void eventsProcess(){
		if(events[0])
			toTitle();
		else if(events[1])
			stageUp();
		else if(events[2])
			win();
		else if(events[3])
			pauseGame();
		else{		
			gp.updateGameUI(this);
			if(!bgm.isPlaying())
				bgm.start();
		}
	}

	private void stageUp(){
			if(stage > 0)
				bgm.pause();
			if(stage < maxstage)
				setStage(++stage);

			Iterator<Enemy> e_iter = enemies.iterator();
			while(e_iter.hasNext()){
				Enemy e = e_iter.next();
			
				if(e.isAlive()){
					e.setAlive(false);
				}
			}
			Iterator<BulletEnemy> be_iter = bulletsEnemy.iterator();
			while(be_iter.hasNext()){
				BulletEnemy be = be_iter.next();
			
				if(be.isAlive()){
					be.setAlive(false);
				}
			}

			timer.stop();
		
			gp.updateGameUI(this,3);
	}

	private void pauseGame(){
			bgm.pause();
			timer.stop();		
			gp.updateGameUI(this,5);
	}

	private void startGame(){
		events[0] = false;
		gp.updateGameUI(this);
		resetGame();
		bgm.play();
		timer.start();
	}

	private void continueGame(){
		bgm.play();
		events[1] = false;
		events[3] = false;
		gp.updateGameUI(this);
		timer.start();
	}

	private void clearGame(){
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
		Iterator<BulletPlayer> bp_iter = bulletsPlayer.iterator();
		while(bp_iter.hasNext()){
			BulletPlayer bp = bp_iter.next();
			bp.proceed();
			
			if(bp.isAlive()){
				bp_iter.remove();
				gp.sprites.remove(bp);
			}
		}
		Iterator<BulletEnemy> be_iter = bulletsEnemy.iterator();
		while(be_iter.hasNext()){
			BulletEnemy be = be_iter.next();
			be.proceed();
			
			if(be.isAlive()){
				be_iter.remove();
				gp.sprites.remove(be);
			}
		}
		v.setAlive(true);
		v.setToOrigin();
		events[0] = true;

		gp.updateGameUI(this);

		timer.start();
	}

	private void resetGame(){
		setStage(0);
		score = 0;
		events[1] = true;
	}

	public void toTitle(){
		bgm.start();		
		gp.updateGameUI(this,1);
		timer.stop();
	}

	private void win(){
			Iterator<Enemy> e_iter = enemies.iterator();
			while(e_iter.hasNext()){
				Enemy e = e_iter.next();
			
				if(e.isAlive()){
					e.setAlive(false);
				}
			}

			timer.stop();
			gp.updateGameUI(this,4);
	}
	
	public void die(){
		v.setAlive(false);
		setStage(1);
		timer.stop();

		gp.updateGameUI(this,2);

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
		case KeyEvent.VK_ESCAPE:
			events[3] = true;
			break;
		case KeyEvent.VK_D:
			difficulty += 0.1;
			break;
		}
	}

	public long getScore(){
		return score;
	}

	public int getStage(){
		return stage;
	}

	public int getMaxHealth(){
		return v.maxHealth;
	}

	public int getHealth(){
		return v.health;
	}

	private void scoreCompare(int stage,long stagescore){
		if(score >= stagescore){
			events[1] = true;
		}
	}

	private void setStage(int stage){
		this.stage = stage;
		difficulty = 0.1 * stage;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if(timer.isRunning())
			controlVehicle(e);
		else if(e.getKeyCode() == KeyEvent.VK_ENTER){
			if(events[0]){				
				startGame();
			}
			else if(events[1] || events[3]){						
				continueGame();
			}
			else if(events[2]){				
				clearGame();
			}
			else
				clearGame();
		}
		else if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
			System.exit(0);
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
