import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class Ball extends Rectangle{

	Random random;
	int yVelocity;
	int xVelocity;
	int initialSpeed = 2;
	
	Ball(int x, int y, int width, int height)
	{
		//Creates the ball
		super(x, y, width, height);
		random = new Random();
		//Sets the direction of where the ball will go
		int randomXDirection = random.nextInt(2);
		if(randomXDirection == 0)
		{
			randomXDirection--;
		}
		setXDirection(randomXDirection * initialSpeed);
		
		int randomYDirection = random.nextInt(2);
		if(randomYDirection == 0)
		{
			randomYDirection--;
		}
		setYDirection(randomYDirection * initialSpeed);
	}
	
	public void setXDirection(int randomXDirection)
	{
		//sets the velocity of x with a random direction
		xVelocity = randomXDirection;
	}
	
	public void setYDirection(int randomYDirection)
	{
		//sets the velocity of Y with a random direction
		yVelocity = randomYDirection;
	}
	
	public void move()
	{
		//Sets the velocity of ball
		x += xVelocity;
		y += yVelocity;
	}
	
	public void draw(Graphics g)
	{
		//Colour the ball
		g.setColor(Color.orange);
		//draws it as a circle
		g.fillOval(x, y, height, width);
	}

}
