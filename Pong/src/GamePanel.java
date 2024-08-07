import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable{

	static final int GAME_WIDTH = 1000;
	static final int GAME_HEIGHT = (int)(GAME_WIDTH * (0.555));
	static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH, GAME_HEIGHT);
	static final int BALL_DIAMETER = 20;
	static final int PADDLE_WIDTH = 25;
	static final int PADDLE_HEIGHT = 100;
	Thread gameThread;
	Image image;
	Graphics graphics;
	Random random;
	Paddle paddle1;
	Paddle paddle2;
	Ball ball;
	Score score;
	
	GamePanel()
	{
		//Sets things to be drawn in the game
		newPaddles();
		newBall();
		score = new Score(GAME_WIDTH, GAME_HEIGHT);
		this.setFocusable(true);
		this.addKeyListener(new AL());
		this.setPreferredSize(SCREEN_SIZE);
		
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	public void newBall()
	{
		random = new Random();
		ball = new Ball((GAME_WIDTH/2) - (BALL_DIAMETER/2), random.nextInt(GAME_HEIGHT-BALL_DIAMETER), BALL_DIAMETER, BALL_DIAMETER);
	}
	
	public void newPaddles()
	{
		//Creates the new paddles
		paddle1 = new Paddle(0,(GAME_HEIGHT/2)-(PADDLE_HEIGHT/2),PADDLE_WIDTH,PADDLE_HEIGHT,1);
		paddle2 = new Paddle(GAME_WIDTH-PADDLE_WIDTH,(GAME_HEIGHT/2)-(PADDLE_HEIGHT/2),PADDLE_WIDTH,PADDLE_HEIGHT,2);
	}
	
	public void paint(Graphics g)
	{
		//Draws the things inside the window
		image = createImage(getWidth(), getHeight());
		graphics = image.getGraphics();
		draw(graphics);
		g.drawImage(image,0,0,this);
	}
	
	public void draw(Graphics g)
	{
		//Draws both the paddles
		paddle1.draw(g);
		paddle2.draw(g);
		ball.draw(g);
		score.draw(g);		
	}
	
	public void move()
	{
		//Makes the paddles and ball move smoothly as its updated by framerate
		paddle1.move();
		paddle2.move();
		ball.move();
	}
	
	public void checkCollision()
	{
		//Bounce ball of the top and bottom & window edges
		if(ball.y <= 0)
		{
			ball.setYDirection(-ball.yVelocity);
		}
		if(ball.y >= GAME_HEIGHT-BALL_DIAMETER)
		{
			ball.setYDirection(-ball.yVelocity);
		}
		
		//bounces ball off paddles
		if(ball.intersects(paddle1))
		{
			ball.xVelocity = Math.abs(ball.xVelocity);
			//optional for more difficulty, gives more speed after it hits the paddle
			ball.xVelocity++;
			if(ball.yVelocity > 0)
			{
				ball.yVelocity++; //optional more difficult
			}
			else
			{
				ball.yVelocity--;
			}
			ball.setXDirection(ball.xVelocity);
			ball.setYDirection(ball.yVelocity);
		}
		
		if(ball.intersects(paddle2))
		{
			ball.xVelocity = Math.abs(ball.xVelocity);
			//optional for more difficulty, gives more speed after it hits the paddle
			ball.xVelocity++;
			if(ball.yVelocity > 0)
			{
				ball.yVelocity++; //optional more difficult
			}
			else
			{
				ball.yVelocity--;
			}
			ball.setXDirection(-ball.xVelocity);
			ball.setYDirection(ball.yVelocity);
		}
		
		//Stops the left paddle from moving off the screen
		if(paddle1.y <= 0)
		{
			paddle1.y = 0;
		}
		if(paddle1.y >= (GAME_HEIGHT - PADDLE_HEIGHT))
		{
			paddle1.y = (GAME_HEIGHT - PADDLE_HEIGHT);
		}
		
		//Stops the right paddle from moving off the screen
		if(paddle2.y <= 0)
		{
			paddle2.y = 0;
		}
		if(paddle2.y >= (GAME_HEIGHT - PADDLE_HEIGHT))
		{
			paddle2.y = (GAME_HEIGHT - PADDLE_HEIGHT);
		}
		
		//give player a point and create a new paddle & ball
		if(ball.x <= 0)
		{
			score.player2++;
			newPaddles();
			newBall();
		}
		if(ball.x >= GAME_WIDTH-BALL_DIAMETER)
		{
			score.player1++;
			newPaddles();
			newBall();
		}
		
	}
	
	public void run()
	{
		//Loops the game
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		while(true)
		{
			long now = System.nanoTime();
			delta += (now - lastTime ) / ns;
			lastTime = now;
			if(delta >= 1)
			{
				move();
				checkCollision();
				repaint();
				delta--;
			}
		}
		
	}
	
	public class AL extends KeyAdapter
	{
		public void keyPressed(KeyEvent e)
		{
			//Checks to see if the key has been pressed
			paddle1.keyPressed(e);
			paddle2.keyPressed(e);
		}
		public void keyReleased(KeyEvent e)
		{
			//Checks to see if the key has be released
			paddle1.keyReleased(e);
			paddle2.keyReleased(e);
		}
	}

}
