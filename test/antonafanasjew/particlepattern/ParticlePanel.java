package antonafanasjew.particlepattern;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;

import javax.swing.JPanel;

import antonafanasjew.particlepattern.model.Particle;

public class ParticlePanel extends JPanel {

	private static final long serialVersionUID = 7130151092199668458L;

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		if (StartParticleFrame.CURRENT_PARTICLE_PATTERN == null) {
			return;
		}
		
		Graphics2D g2 = (Graphics2D)g;

		Random random = new Random();

		
		
		for (Particle particle : StartParticleFrame.CURRENT_PARTICLE_PATTERN.getParticles().values()) {
			
			int n = random.nextInt() % 1;
			
			if (n == 0) {
				g2.setColor(Color.red);
			}
			
			if (n == 1) {
				g2.setColor(Color.blue);
			}
			
			if (n == 2) {
				g2.setColor(Color.green);
			}
			
			if (n == 3) {
				g2.setColor(Color.yellow);
			}
			g2.fillRect((int)particle.getOffset().getX(), (int)particle.getOffset().getY(), 20, 20);
		}
		
	}
	
}
