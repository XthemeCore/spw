package f2.spw;

import java.awt.BorderLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Color;
import javax.swing.JFrame;

public class Main {
	static int screenWidth;
	static int screenHeight;

	public static void main(String[] args){
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();

		screenWidth = ge.getMaximumWindowBounds().width;
		screenHeight = ge.getMaximumWindowBounds().height;

		JFrame frame = new JFrame("Space War");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(screenWidth, screenHeight);
		frame.getContentPane().setLayout(new BorderLayout());
		frame.setUndecorated(true); 
		frame.setResizable(false);

		if(gd.isFullScreenSupported())
			gd.setFullScreenWindow(frame);
		
		Player v = new Player(180, 550, 32, 32);
		GamePanel gp = new GamePanel();
		GameEngine engine = new GameEngine(gp, v);
		frame.addKeyListener(engine);
		frame.getContentPane().setBackground(Color.BLACK);
		frame.getContentPane().add(gp, BorderLayout.CENTER);
		frame.setVisible(true);
		
		engine.start();
	}
}
