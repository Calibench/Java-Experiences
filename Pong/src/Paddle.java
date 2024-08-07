import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class Paddle extends Rectangle {

	int id;
	int yVelocity;
	int speed = 10;
	
	Paddle(int x, int y, int PADDLE_WIDTH, int PADDLE_HEIGHT, int id)
	{
		//gets paddle to be a rectangle
		super(x, y, PADDLE_WIDTH, PADDLE_HEIGHT);
		this.id = id;
	}
	
	public void keyPressed(KeyEvent e)
	{
		//Checks to see which paddle is moving and what key press by VK_*
		switch(id)
		{
		case 1:
			if(e.getKeyCode() == KeyEvent.VK_W)
			{
				setYDirection(-speed);
				move();
			}
			if(e.getKeyCode() == KeyEvent.VK_S)
			{
				setYDirection(speed);
				move();
			}
			break;
		case 2:
			if(e.getKeyCode() == KeyEvent.VK_UP)
			{
				setYDirection(-speed);
				move();
			}
			if(e.getKeyCode() == KeyEvent.VK_DOWN)
			{
				setYDirection(speed);
				move();
			}
			break;
		}
	}
	
	public void keyReleased(KeyEvent e)
	{
		//Checks to see which paddle is stopping and what key release by VK_*
		switch(id)
		{
		case 1:
			if(e.getKeyCode() == KeyEvent.VK_W)
			{
				setYDirection(0);
				move();
			}
			if(e.getKeyCode() == KeyEvent.VK_S)
			{
				setYDirection(0);
				move();
			}
			break;
		case 2:
			if(e.getKeyCode() == KeyEvent.VK_UP)
			{
				setYDirection(0);
				move();
			}
			if(e.getKeyCode() == KeyEvent.VK_DOWN)
			{
				setYDirection(0);
				move();
			}
			break;
		}
	}
	
	public void setYDirection(int yDirection)
	{
		yVelocity = yDirection;
	}
	
	public void move()
	{
		y = y + yVelocity;
	}
	
	public void draw(Graphics g)
	{
		//colour of the paddles id 1 is left paddle, id 2 is right paddle
		if(id == 1)
		{
			g.setColor(Color.green);	
		}
		else
		{
			g.setColor(Color.pink);
		}
		g.fillRect(x, y, width, height);
	}

}
