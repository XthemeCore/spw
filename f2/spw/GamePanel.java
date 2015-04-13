package f2.spw;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.swing.JPanel;

public class GamePanel extends JPanel {
	
	private BufferedImage bi;	
	Graphics2D big;
	ArrayList<Sprite> sprites = new ArrayList<Sprite>();

	BufferedImage background;
	private int bgOffsetY = 0;

	private int gpanelWidth;
	private int gpanelHeight;

	public GamePanel() {
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
		big.setBackground(Color.BLACK);
	}

	public void updateGameUI(GameReporter reporter){
		big.clearRect(0, 0, gpanelWidth, gpanelHeight);
		updateBackground();
		big.setColor(Color.WHITE);		
		big.drawString(String.format("%08d", reporter.getScore()), 300, 20);
		for(Sprite s : sprites){
			s.draw(big);
		}
		
		repaint();
	}

	public void updateBackground(){		
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
		g2d.drawImage(bi, null, 0, 0);
	}

}
