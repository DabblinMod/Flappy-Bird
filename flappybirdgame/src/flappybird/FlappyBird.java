package flappybird;

import java.util.Random;

import java.awt.Graphics;
import java.awt.event.MouseListener;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;//class that takes our actions in game 
import java.awt.event.ActionListener;//is used to receive our button responses
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.awt.Rectangle;//represents the objects in our game
import java.awt.Color;
import java.awt.Font;


import javax.swing.JFrame;//used to create our window
import javax.swing.Timer;

public class FlappyBird implements ActionListener, MouseListener, KeyListener {

	public static FlappyBird flappybird; //static to be available for every instance
	
	public final int WIDTH=800, HEIGHT=800; // sets the size of the window frame
	
	public Render renderer;
	
	public Rectangle bird;//represents the bird object for the gamme
	
	public ArrayList<Rectangle> columns; //the obstacles that will be added to our game

	private Random rand;
	
	public boolean gameOver=false, started;//represents when the game is over
	
	protected int ticks=0, yMotion=0; //movements of the bird

	private int score;
	
	public FlappyBird() {//constructor
		JFrame jf=new JFrame();
		
		Timer timer=new Timer(20, this);
		
		renderer= new Render();	//render obj
		
		bird=new Rectangle(WIDTH/2-10, HEIGHT/2-10, 20, 20);
		columns=new ArrayList<Rectangle>();
		rand=new Random();
		
		jf.add(renderer); //adds the render obj to the frame so it has an actual effect on app
		jf.addMouseListener(this);
		jf.addKeyListener(this);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setTitle("Flappy Bird");
		jf.setSize(WIDTH, HEIGHT);
		jf.setResizable(false);
		jf.setVisible(true);
		
		addColumn(true);
		addColumn(true);
		addColumn(true);
		addColumn(true);
		
		timer.start();
		
	}
	
	public void addColumn(boolean state) {
		int space = 300;//spaces between columns;
		int width = 100;
		int height = 50 + rand.nextInt(300);
		if(state) {
			columns.add(new Rectangle(WIDTH + width + columns.size() * 300, HEIGHT - height - 120, width, height)); //ensures the column is on the far right side of the screen.
			columns.add(new Rectangle(WIDTH + width + (columns.size() - 1) *300, 0, width, HEIGHT - height -space)); //sets the next rectangle at a smaller size by comparison to maintain a space
		}
		else {
			columns.add(new Rectangle(columns.get(columns.size() - 1).x + 600, HEIGHT - height - 120, width, height)); //ensures the column is on the far right side of the screen.
			columns.add(new Rectangle(columns.get(columns.size() - 1).x, 0, width, HEIGHT - height -space));
		}
		
	}
	public void paintColumn(Graphics g, Rectangle column) {
		g.setColor(Color.green.darker());
		g.fillRect(column.x, column.y, column.width, column.height);
		
	}
	
	public void repaint(Graphics g) {
		g.setColor(Color.cyan);//background 
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		g.setColor(Color.orange);//ground level
		g.fillRect(0, HEIGHT-150, WIDTH, 150);
		
		g.setColor(Color.red);//player
		g.fillRect(bird.x, bird.y, bird.width, bird.height);//use the bird states to set its size 
		
		for(Rectangle column: columns) 
		{
			paintColumn(g, column);
		}
		g.setColor(Color.white);
		g.setFont(new Font("Arial", 1, 80));//setting the font to Arial with a font size of 80
		
		if(!started) {
			g.drawString("Click To Start!", 75, HEIGHT/2 - 50);
		}
		if(gameOver) {
			g.drawString("Game Over!", 100, HEIGHT/2 - 50);
		}
		if(!gameOver && started) {//if the game hasn't started yet draw the string value
			 g.drawString(String.valueOf(score), WIDTH / 2 - 25, 100);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		int speed=10;
		ticks++;
		if(started) 
		{
			for(int i=0; i < columns.size(); i++) { //moves the columns into the screen's view
				Rectangle column=columns.get(i);
				
				column.x -= speed; //x determines the rate at which the columns come into view.
			}
			
			for(int i=0; i < columns.size(); i++) { //moves the columns into the screen's view
				Rectangle column=columns.get(i);
				
				if(column.x + column.width < 0) {
					columns.remove(column);//takes the bottom column out
					if(column.y==0) { //if the top column has no column then we can add another column type to it
						addColumn(false);
					}
				}
				
			}
			
			if(ticks % 2 == 0 && yMotion < 15) {
				yMotion += 2;
			}
			bird.y+=yMotion;//represents the birds increased movement	
			
			//check if bird collides with any rectangles
			for(int i=0; i < columns.size(); i++) { //moves the columns into the screen's view
				Rectangle column=columns.get(i);
				
				if(column.y==0 && bird.x + bird.width / 2 > column.x + column.width/2 - 5 && bird.x + bird.width / 2 < column.x + column.width/2 + 5) {//checks if bird is in between the columns
					score++;
				}
				if(column.intersects(bird)) {
					gameOver=true;
					
					if(bird.x < column.x) {
						bird.x= column.x - bird.width; //keeps the bird in place at the column it hits
					}
					else {
						if(column.y !=0) {//if it hits the bottom column it will set it to ground level
							bird.y = column.y - bird.height;
						}
						else if(bird.y < column.height) {
							bird.y = column.height; //sets it so he can't go past the height
						}
					}
				}
			}
			//if the bird is greater than the height of a column or below
			if(bird.y > HEIGHT-120 || bird.y < 0) {
				gameOver=true;
			}
			
			if(bird.y + yMotion >= HEIGHT - 120) {//keeps the bird on ground level.
				bird.y= HEIGHT -120 - bird.height;
			}
		}
		renderer.repaint();
	}
	
	private void jump() {
		if(gameOver) {
			bird=new Rectangle(WIDTH/2-10, HEIGHT/2-10, 20, 20);
			columns.clear();
			yMotion=0;
			score=0;
			
			addColumn(true);
			addColumn(true);
			addColumn(true);
			addColumn(true);
			
			gameOver=false;
		}
		if(!started) {
			started=true;//begins the game
		}
		else if(!gameOver) {//if the game isn't over we have to reset the bird objs y-position
			if(yMotion > 0) {
				yMotion = 0;
			}
			yMotion-=10;
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		jump();//starts the game
		
	}

	@Override
	public void mousePressed(MouseEvent e) {

		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {

		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			jump();
		}
		
	}
	
	public static void main(String[] args) {
		flappybird= new FlappyBird();
		
		
	}

}
