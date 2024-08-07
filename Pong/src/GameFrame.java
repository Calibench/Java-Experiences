import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class GameFrame extends JFrame {

	GamePanel panel;
	
	GameFrame()
	{
		//The window in which the game will be ran in. This contains: title, non-resizable, background color, close button, make it visible, center to screen.
		panel = new GamePanel();
		this.add(panel);
		this.setTitle("Pong Game");
		this.setResizable(false);
		this.setBackground(Color.black);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		
	}
}
