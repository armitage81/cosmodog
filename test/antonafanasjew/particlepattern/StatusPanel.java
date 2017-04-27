package antonafanasjew.particlepattern;


import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Label;

import javax.swing.JPanel;

public class StatusPanel extends JPanel {

	public float x;
	public float y;
	
	private static final long serialVersionUID = 903530054283340671L;
	
	public StatusPanel() {
		this.setLayout(new FlowLayout());
		add(new Label(StartParticleFrame.CAM_OFFSET.getX() + "/" + StartParticleFrame.CAM_OFFSET.getY()));
	};
	
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(Color.red);
		g2.drawString(StartParticleFrame.CAM_OFFSET.getX() + "/" + StartParticleFrame.CAM_OFFSET.getY(), 10, 10);
	}
	

}
