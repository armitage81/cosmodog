package antonafanasjew.particlepattern;


import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ParticleFrame extends JFrame {

	private static final long serialVersionUID = 5651971366035327069L;

	private JPanel mainPanel = null;
	private ParticlePanel particlePanel = null;
	
	public ParticleFrame() {
		this.setContentPane(getMainPanel());
	}
	
	
	public JPanel getMainPanel() {
		
		if (mainPanel == null) {
			mainPanel = new JPanel();
			mainPanel.setLayout(new BorderLayout());
			mainPanel.add(getParticlePanel(), BorderLayout.CENTER);
			mainPanel.add(new StatusPanel(), BorderLayout.SOUTH);
		}
		
		return mainPanel;
	}
	
	public ParticlePanel getParticlePanel() {
		if (particlePanel == null) {
			particlePanel = new ParticlePanel();
		}
		
		return particlePanel;
	}
	
}
