package f2.spw;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.swing.JPanel;

public class GamePanel extends JPanel {
	
	private BufferedImage bi;	
	BufferedImage background;
	Graphics2D big;
	ArrayList<Sprite> sprites = new ArrayList<Sprite>();

	private int bgOffsetY = 0;

	public GamePanel() {
		try{
			InputStream stream = getClass().getResourceAsStream("/f2/spw/Graphics/background.png");
            background = ImageIO.read(stream);

        }
        catch (IOException e){
            e.printStackTrace();
        }
		bi = new BufferedImage(400, 650, BufferedImage.TYPE_INT_ARGB);
		big = (Graphics2D) bi.getGraphics();
		big.setBackground(Color.BLACK);
	}

	public void updateGameUI(GameReporter reporter){
		updateGameUI(reporter,1);
	}

	public void updateGameUI(GameReporter reporter,int state){
		big.clearRect(0, 0, 400, 650);
		
		big.setColor(Color.WHITE);	
		big.setFont(big.getFont().deriveFont(18F));

		updateGameBackground();
		
		switch (state){
			case 0:	drawGameTitle();
				break;
			case 1:	drawMap(reporter);
				break;
			case 2:	drawGameOver(reporter);
				break;
			case 3: drawStageUp(reporter);
				break;
			case 4: drawGameFinish(reporter);
				break;
		}
		
		repaint();
	}

	public void drawMap(GameReporter reporter){
		for(Sprite s : sprites){
			s.draw(big);}
		big.drawString(String.format("Score: %08d", reporter.getScore()), 240, 20);
		big.drawString(String.format("Stage %02d", reporter.getStage()), 10, 20);
	}

	public void drawGameTitle(){
		big.setFont(big.getFont().deriveFont(48F)); 
		big.drawString("SPACEWAR", 45, 240);										
		big.setFont(big.getFont().deriveFont(18F)); 
		big.drawString("Press [ENTER] to Start", 45, 430);
		big.drawString("or Press [ESC] to Exit", 45, 450);
		big.setFont(big.getFont().deriveFont(18F));
		big.drawString("ALPHA 0.1", 45, 270);
		big.drawString("(C) XthemeCore (5610110364) , 2015", 45, 550);
	}

	public void drawGameFinish(GameReporter reporter){
		drawBackgroundBar(0);
		big.setFont(big.getFont().deriveFont(32F)); 
		big.drawString("Good job!", 45, 240);										
		big.setFont(big.getFont().deriveFont(18F)); 
		big.drawString("Press [ENTER] to Restart", 45, 500);
		big.drawString("or Press [ESC] to Exit", 45, 520);
		big.setFont(big.getFont().deriveFont(18F));
		big.drawString("You win the SpaceWar.", 45, 280);
		big.drawString(String.format("Your score: %d", reporter.getScore()), 45, 310);
		
	}

	public void drawGameOver(GameReporter reporter){
		drawBackgroundBar(0);
		big.setFont(big.getFont().deriveFont(32F)); 
		big.drawString("Game Over", 45, 240);										
		big.setFont(big.getFont().deriveFont(18F)); 
		big.drawString("Press [ENTER] to Restart", 45, 500);
		big.drawString("or Press [ESC] to Exit", 45, 520);
		big.setFont(big.getFont().deriveFont(18F));
		big.drawString("You lost the SpaceWar!", 45, 280);
		big.drawString(String.format("Your score: %d", reporter.getScore()), 45, 310);
	}

	public void drawStageUp(GameReporter reporter){
		drawBackgroundBar(0);
		big.setFont(big.getFont().deriveFont(32F));
		big.drawString(String.format("Stage %02d", reporter.getStage()), 45, 240);			
		big.setFont(big.getFont().deriveFont(18F)); 
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
		big.drawString("Press [ENTER] to Start", 45, 500);
		big.drawString("or Press [ESC] to Exit", 45, 520);
		big.setFont(big.getFont().deriveFont(18F));
	}

	private void drawBackgroundBar(int style){
		for(Sprite s : sprites){
			s.draw(big);}
		switch(style){
			case 0:	big.setColor(new Color(5, 0, 25, 196 ));
					big.fillRect(25, 0, 250, 650);
					big.setColor(new Color(255, 255, 255, 255));
					break;
			case 1: big.setColor(new Color(255, 255, 255, 196 ));
					big.fillRect(25, 0, 250, 650);
					big.setColor(new Color(0, 0, 0, 206));
					break;
		}
	}

	public void updateGameBackground(){		
		big.drawImage(background, 0, bgOffsetY - 650, 400, 650, null);
		big.drawImage(background, 0, bgOffsetY, 400, 650, null);
		if(bgOffsetY < 650){			
			bgOffsetY++;

		}		else{
			bgOffsetY = 0;
		}
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(bi, null, 0, 0);
	}

}
