import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Pong extends JPanel {

	// instance variables
	int x = 0;
	int y = 0;
	int xa = 1;
	int ya = 1;
	private JFrame frame;
	private static final int WIDTH = 300;
	private static final int HEIGHT = 300;
	private boolean gameStarted = false;

	public Pong() {
		// create a JFrame to display the game
		frame = new JFrame("Pong");
		frame.setSize(WIDTH, HEIGHT);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(this);

		// add a KeyListener to the frame to handle key events
		frame.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}

			@Override
			public void keyReleased(KeyEvent e) {}

			@Override
			public void keyPressed(KeyEvent e) {
				startGame(e);
				movePaddle(e);
			}
		});
	}

	private void startGame(KeyEvent e) {
		// start the game if the "Enter" key is pressed
		if (!gameStarted && e.getKeyCode() == KeyEvent.VK_ENTER) {
			gameStarted = true;
		}
	}

	private void movePaddle(KeyEvent e) {
		// only move the paddle if the game has started
		if (gameStarted) {
			// get the key code of the pressed key
			int key = e.getKeyCode();

			// move the paddle up if the "W" key is pressed
			if (key == KeyEvent.VK_W) {
				y--;
			}

			// move the paddle down if the "S" key is pressed
			if (key == KeyEvent.VK_S) {
				y++;
			}
		}
	}

	public void move() {
		// only move the ball if the game has started
		if (gameStarted) {
			// check if the ball has reached the left or right edge of the screen
			if (x + xa < 0 || x + xa > getWidth() - 30) {
				xa = -xa;
			}

			// check if the ball has reached the top or bottom edge of the screen
			if (y + ya < 0 || y + ya > getHeight() - 30) {
				ya = -ya;
			}

			// check if the ball has reached the paddle
			if (x + xa > 280 && y + ya > y && y + ya < y + 60) {
				xa = -xa;
			}

			// move the ball
			x = x + xa;
			y = y + ya;
		}
	}

	public void paint(Graphics g) {
		super.paint(g);
		setBackground(Color.BLACK);

		// draw the main menu
		if (!gameStarted) {
			g.setColor(Color.WHITE);
			g.drawString("PONG", 120, 100);
			g.drawString("Press Enter to start the game", 60, 120);
		}

		// draw the ball
		g.setColor(Color.WHITE);
		g.fillOval(x, y, 30, 30);

		// draw the paddle
		g.fillRect(280, y, 20, 60);
	}
	public static void main(String[] args) {
		Pong pong = new Pong();
		// run the game loop
		while (true) {
			pong.move();
			pong.repaint();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
