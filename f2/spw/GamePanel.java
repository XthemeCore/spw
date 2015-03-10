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
		updateGameUI(reporter,0);
	}

	public void updateGameUI(GameReporter reporter,int state){
		big.clearRect(0, 0, 400, 650);
		
		big.setColor(Color.WHITE);	
		big.setFont(big.getFont().deriveFont(16F));

		updateGameBackground();
		
		switch (state){
			case 0:	big.drawString(String.format("%08d", reporter.getScore()), 290, 20);
					for(Sprite s : sprites){
					s.draw(big);}
					break;
			case 1:	sprites.get(0).draw(big);
					int tempsize = big.getFont().getSize();
					big.setFont(big.getFont().deriveFont(20F)); 
					big.drawString("Game Over", 135, 240);										
					big.setFont(big.getFont().deriveFont((float)tempsize)); 
					big.drawString("Press Any Key except Arrow key to Restart", 45, 280);
					big.drawString("or Press ESC to Exit", 120, 300);
					big.setFont(big.getFont().deriveFont(18F));
					big.drawString(String.format("Your Score: %d", reporter.getScore()), 120, 360);
					break;
		}
		
		repaint();
	}

	public void updateGameBackground(){		
		big.drawImage(background, 0, bgOffsetY - 650, 400, 650, null);
		big.drawImage(background, 0, bgOffsetY, 400, 650, null);
		if(bgOffsetY < 650){			
			bgOffsetY++;
		}
		else{
			bgOffsetY = 0;
		}
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(bi, null, 0, 0);
	}

}
