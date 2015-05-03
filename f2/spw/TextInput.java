package f2.spw;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JTextField;

public class TextInput extends JTextField{

	public TextInput(String text) {
        super(text);
		setForeground(Color.WHITE);
        setBackground(new Color(0,0,0,120));
		setOpaque(true);
		setBorder(null);
        setFont(getFont().deriveFont(20F));
	}

    protected void paintComponent(Graphics g) {
        g.setColor(getBackground());
        g.fillRect(0,0, getWidth(),getHeight());
        super.paintComponent(g);
    }
}
