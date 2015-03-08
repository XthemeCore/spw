package f2.spw;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

public class GamePanel extends JPanel {
	
	private BufferedImage bi;	
	Graphics2D big;
	ArrayList<Sprite> sprites = new ArrayList<Sprite>();

	public GamePanel() {
		bi = new BufferedImage(400, 650, BufferedImage.TYPE_INT_ARGB);
		big = (Graphics2D) bi.getGraphics();
		big.setBackground(Color.BLACK);
	}

	public void updateGameUI(GameReporter reporter){
		updateGameUI(reporter,0);
	}

	public void updateGameUI(GameReporter reporter,int state){
		big.clearRect(0, 0, 400, 650);
		
		big.setColor(Color.WHITE);	
		big.setFont(big.getFont().deriveFont(16F));
		big.drawString(String.format("%08d", reporter.getScore()), 290, 20);

		switch (state){
			case 0:	for(Sprite s : sprites){
					s.draw(big);}
					break;
			case 1: int tempsize = big.getFont().getSize();
					big.setFont(big.getFont().deriveFont(20F)); 
					big.drawString("Game Over", 140, 240);										
					big.setFont(big.getFont().deriveFont((float)tempsize)); 
					big.drawString("Press Any Key except Arrow key to Restart", 45, 280);
					big.drawString("or Press ESC to Exit", 120, 300);
					break;
		}
		
		repaint();
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(bi, null, 0, 0);
	}

}
