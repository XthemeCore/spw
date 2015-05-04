package f2.spw;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JPanel;

public class GamePanel extends JPanel {
	
	private BufferedImage bi;	
	Graphics2D big;
	ArrayList<Sprite> sprites = new ArrayList<Sprite>();

	HealthBar hpbar = new HealthBar(15,30);
	BufferedImage background;
	private int bgOffsetY = 0;

	private int gpanelWidth;
	private int gpanelHeight;

	TextInput input;
	public GamePanel() {
		input = new TextInput("");
		add(input);
		try{
			InputStream stream = getClass().getResourceAsStream("/f2/spw/Graphics/background.png");
            background = ImageIO.read(stream);

        }
        catch (IOException e){
            e.printStackTrace();
        }
		gpanelWidth = 400;
		gpanelHeight = 600;
		bi = new BufferedImage(gpanelWidth, gpanelHeight, BufferedImage.TYPE_INT_ARGB);
		big = (Graphics2D) bi.getGraphics();
	}

	public void updateGameUI(GameReporter reporter){
		updateGameUI(reporter,0);
	}

	public void updateGameUI(GameReporter reporter,int state){
		big.clearRect(0, 0, gpanelWidth, gpanelHeight);
		
		big.setColor(Color.WHITE);	
		big.setFont(big.getFont().deriveFont(18F));

		drawBackground();
		
		switch (state){
			case 0:	drawMap(reporter);
				break;
			case 1:	drawGameTitle();
				break;
			case 2:	drawGameOver(reporter);
				break;
			case 3: drawStageUp(reporter);
				break;
			case 4: drawGameFinish(reporter);
				break;
			case 5: drawGamePause(reporter);
				break;
			case 6: drawScoreRecord(reporter);
				break;
		}

		repaint();
	}

	public void drawSprites(){
		for(Sprite s : sprites){
			s.draw(big);}
	}

	public void drawMap(GameReporter reporter){
		drawSprites();
		big.drawString(String.format("Score: %08d", reporter.getScore()), 240, 20);
		big.drawString(String.format("Stage %02d", reporter.getStage()), 10, 20);
		drawStatus(reporter);
	}

	public void drawStatus(GameReporter reporter){
		hpbar.updateOffset(reporter.getMaxHealth(),reporter.getHealth());		
		hpbar.draw(big);
	}

	public void drawGameTitle(){
		input.setBounds(0,0,1,1);
		big.setFont(big.getFont().deriveFont(48F)); 
		big.drawString("SPACEWAR", 45, 240);										
		big.setFont(big.getFont().deriveFont(18F)); 
		big.drawString("Press [ENTER] to Start", 45, 430);
		big.drawString("or Press [ESC] to Exit", 45, 450);
		big.setFont(big.getFont().deriveFont(18F));
		big.drawString("COMPLETE ALPHA", 45, 270);
		big.drawString("(C) XthemeCore (5610110364) , 2015", 45, 550);
	}

	public void drawGamePause(GameReporter reporter){
		drawSprites();
		drawBar();
		big.setFont(big.getFont().deriveFont(32F));
		big.drawString("PAUSE", 45, 240);			
		big.setFont(big.getFont().deriveFont(18F)); 
		drawScoreAchieve(reporter);
		big.drawString("Press [ENTER] to Resume", 45, 500);
		big.drawString("or Press [ESC] to Exit", 45, 520);
		big.setFont(big.getFont().deriveFont(18F));
	}

	public void drawGameFinish(GameReporter reporter){
		drawSprites();
		drawBar();
		big.setFont(big.getFont().deriveFont(32F)); 
		big.drawString("Good job!", 45, 240);										
		big.setFont(big.getFont().deriveFont(18F)); 
		big.drawString("Press [ENTER] to see Score", 45, 500);
		big.drawString("or Press [ESC] to Exit", 45, 520);
		big.setFont(big.getFont().deriveFont(18F));
		big.drawString("You win the SpaceWar.", 45, 280);
		big.drawString(String.format("Your score: %d", reporter.getScore()), 45, 310);
		
	}

	public void drawGameOver(GameReporter reporter){
		input.setBounds(530,470,160,20);
		drawSprites();
		drawBar();
		big.setFont(big.getFont().deriveFont(32F)); 
		big.drawString("Game Over", 45, 240);										
		big.setFont(big.getFont().deriveFont(20F));
		big.drawString("Enter Your Name:", 45, 390);
		big.setFont(big.getFont().deriveFont(18F));
		big.drawString("Press [ENTER] to see Score", 45, 500);
		big.drawString("or Press [ESC] to Exit", 45, 520);
		big.setFont(big.getFont().deriveFont(18F));
		big.drawString("You lost the SpaceWar!", 45, 280);
		big.drawString(String.format("Your score: %d", reporter.getScore()), 45, 310);
	}

	public void drawScoreRecord(GameReporter reporter){
		input.setBounds(0,0,1,1);
		drawSprites();
		drawBar();
		big.setFont(big.getFont().deriveFont(32F)); 
		big.drawString("Top 5 Score", 45, 240);										
		big.setFont(big.getFont().deriveFont(18F));
		big.drawString("Name   Score", 45, 280);
		int i = 0;
		Iterator<PlayerRecord> r_iter = reporter.getScoreRecord().iterator();
		while(r_iter.hasNext() && i < 5){
			PlayerRecord r = r_iter.next();
			big.drawString(String.format("%s %8d",
				r.getName(),
				r.score
				), 45, 310 + 31*i);
			i++;
		}
		big.setFont(big.getFont().deriveFont(18F));
		big.drawString("Press [ENTER] to Restart", 45, 500);
		big.drawString("or Press [ESC] to Exit", 45, 520);
	}

	public void drawStageUp(GameReporter reporter){
		drawSprites();
		drawBar();
		big.setFont(big.getFont().deriveFont(32F));
		big.drawString(String.format("Stage %02d", reporter.getStage()), 45, 240);			
		big.setFont(big.getFont().deriveFont(18F));
		drawScoreAchieve(reporter);
		big.drawString("Press [ENTER] to Start", 45, 500);
		big.drawString("or Press [ESC] to Exit", 45, 520);
		big.setFont(big.getFont().deriveFont(18F));
	}

	public void drawScoreAchieve(GameReporter reporter){
		big.drawString(String.format("Score per shot: %d",(long)(100 * Math.pow(2,reporter.getStage() - 1))), 45, 280);
		switch(reporter.getStage()){
			case 1:	big.drawString("Achieve score: 1,000", 45, 310);
					break;
			case 2:	big.drawString("Achieve score: 5,000", 45, 310);
					break;
			case 3:	big.drawString("Achieve score: 10,000", 45, 310);
					break;
			case 4:	big.drawString("Achieve score: 50,000", 45, 310);
					break;
			case 5:	big.drawString("Achieve score: 100,000", 45, 310);
					break;
			case 6:	big.drawString("Achieve score: 500,000", 45, 310);
					break;
			case 7:	big.drawString("Achieve score: 1,000,000", 45, 310);
					break;
			case 8:	big.drawString("Achieve score: 5,000,000", 45, 310);
					break;
			case 9:	big.drawString("Achieve score: 10,000,000", 45, 310);
					break;
			case 10:	big.drawString("Achieve score: 99,999,999", 45, 310);
					break;
		}
	}

	private void drawBar(){
		big.setColor(new Color(5, 0, 25, 196 ));
		big.fillRect(25, 0, 250, 650);
		big.setColor(new Color(255, 255, 255, 255));
	}

	public void drawBackground(){		
			big.drawImage(background, 0, bgOffsetY - 650, 400, 650, null);
			big.drawImage(background, 0, bgOffsetY, 400, 650, null);
			if(bgOffsetY < 650)			
				bgOffsetY++;			
			else
				bgOffsetY = 0;
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(bi, null, (Main.screenWidth - gpanelWidth)/2, (Main.screenHeight - gpanelHeight)/2);
	}
}
