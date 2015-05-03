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
	private ArrayList<BulletEntity> bullets = new ArrayList<BulletEntity>();
	private Player v;	
	
	private Audio bgm;
	private Audio shotSE;
	private Audio explodeSE;
	private Audio gameoverME;
	
	private double difficulty;

	private ScoreRecord record;

	private Timer timer;
	Counter c;
	private int maxTime = 50;
	
	public static int maxstage = 10;

	private boolean[] keys = {false,false,false,false,false};
	private boolean events[] = {true,false,false,false,false};
	
	public GameEngine(GamePanel gp, Player v) {
		this.gp = gp;
		this.v = v;
		bgm = new Audio("sample.mid");
		shotSE = new Audio("gunshot.mid");
		explodeSE = new Audio("explode.mid");
		gameoverME = new Audio("gameover.mid");
		
		record = new ScoreRecord();

		gp.sprites.add(v);
		gp.input.addKeyListener(this);
		
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
		bullets.add(bp);
	}

	private void generateBulletEnemy(LivingEntity n){
		BulletEnemy be = new BulletEnemy(n.x + (n.width/2) - 2, n.y + 32);
		gp.sprites.add(be);
		bullets.add(be);
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
		while(e_iter.hasNext()){
			Enemy e = e_iter.next();
			e.proceed();
			e.scan(v);
			if(e.isDetect())
				if(c.getCount()%12 == 0){
					shotSE.start();
					generateBulletEnemy(e);
				}
			
			if(!e.isAlive()){
				e_iter.remove();
				gp.sprites.remove(e);
			}
		}
		Iterator<BulletEntity> b_iter = bullets.iterator();
		while(b_iter.hasNext()){
			
			BulletEntity b  = b_iter.next();
			b.proceed();
			if(!b.isAlive()){
				b_iter.remove();
				gp.sprites.remove(b);
			}
		}
		
		
		Rectangle2D.Double vr = v.getRectangle();
		Rectangle2D.Double er;
		Rectangle2D.Double br;
		for(Enemy e : enemies){
			er = e.getRectangle();			
			if(er.intersects(vr) && e.isAlive()){
				if(v.isAlive()){
					v.health -= 10;
					e.setAlive(false);
				}
				else
					die();
				return;
			}
			else{
				for(BulletEntity b : bullets){
					br = b.getRectangle();
					if(b instanceof BulletPlayer){
						e.scan(b);
						if(br.intersects(er) && b.isAlive() && e.isAlive()){
							explodeSE.start();
							e.setAlive(false);
							b.setAlive(false);
							v.score+= 100 * Math.pow(2,v.stage - 1); 
						}
					}else if(b instanceof BulletEnemy){
						if(br.intersects(vr) && b.isAlive()){
							if(v.isAlive()){
								v.health -= 1;
								b.setAlive(false);
							}
							else
								die();
							return;
						}
					}
				}
			}
		}
	}
	

	private void scoreProcess(){
		if(v.score >= 99999999){
			events[2] = true;
		}
		else {
			switch(v.stage){
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
			if(v.stage > 0)
				bgm.pause();
			if(v.stage < maxstage)
				setStage(++v.stage);

			Iterator<Enemy> e_iter = enemies.iterator();
			while(e_iter.hasNext()){
				Enemy e = e_iter.next();
			
				if(e.isAlive()){
					e.setAlive(false);
				}
			}
			Iterator<BulletEntity> b_iter = bullets.iterator();
			while(b_iter.hasNext()){
				BulletEntity b = b_iter.next();
			
				if(b.isAlive()){
					b.setAlive(false);
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
		else if(events[4])
			events[4] = false;
		Iterator<Enemy> e_iter = enemies.iterator();
		while(e_iter.hasNext()){
			Enemy e = e_iter.next();			
			if(e.isAlive() || e.y < e.Y_TO_DIE){
				e_iter.remove();
				gp.sprites.remove(e);
			}
		}
		Iterator<BulletEntity> b_iter = bullets.iterator();
		while(b_iter.hasNext()){
			BulletEntity b = b_iter.next();			
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
	}

	private void resetGame(){
		setStage(0);
		v.score = 0;
		events[1] = true;
	}

	public void toTitle(){
		bgm.start();		
		gp.updateGameUI(this,1);
		timer.stop();
	}

	public void toRecord(){	
		events[4] = true;
		gp.updateGameUI(this,6);
	}

	private void win(){
			Iterator<Enemy> e_iter = enemies.iterator();
			while(e_iter.hasNext()){
				Enemy e = e_iter.next();			
				if(e.isAlive()){
					e.setAlive(false);
				}
			}
			Iterator<BulletEntity> b_iter = bullets.iterator();
			while(b_iter.hasNext()){
				BulletEntity b = b_iter.next();			
				if(b.isAlive()){
					b.setAlive(false);
				}
			}

			timer.stop();
			gp.updateGameUI(this,4);
	}
	
	public void die(){
		bgm.pause();
		gameoverME.start();
		v.setAlive(false);
		setStage(1);
		timer.stop();

		gp.input.setText(v.getName());
		gp.input.requestFocusInWindow();
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
		return v.score;
	}

	public int getStage(){
		return v.stage;
	}

	public int getMaxHealth(){
		return v.maxHealth;
	}

	public int getHealth(){
		return v.health;
	}

	public ArrayList<PlayerRecord> getScoreRecord(){
		return record.getRecord();
	}

	private void scoreCompare(int stage,long stagescore){
		if(v.score >= stagescore){
			events[1] = true;
		}
	}

	private void setStage(int stage){
		v.stage = stage;
		difficulty = 0.1 * v.stage;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if(timer.isRunning()){
			gp.input.setText("");
			controlVehicle(e);
		}
		else if(e.getKeyCode() == KeyEvent.VK_ENTER){
			if(events[0]){				
				startGame();
			}
			else if(events[1] || events[3]){						
				continueGame();
			}
			else if(events[2]){				
				toRecord();
			}
			else if(events[4]){				
				clearGame();
			}
			else{
				System.out.println(gp.input.getText());
				v.setName(gp.input.getText());
				record.addRecord(v);
				toRecord();
			}
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
