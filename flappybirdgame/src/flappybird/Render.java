package flappybird;

import java.awt.Graphics;//allows us t oset colors and details in the frame

import javax.swing.JPanel;//creates a buffered imagery for the window

public class Render extends JPanel {
	private static final long serializedVersionUID = 1L;
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);//calls the paintComponent parent so we can 
		
		FlappyBird.flappybird.repaint(g);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}

}
