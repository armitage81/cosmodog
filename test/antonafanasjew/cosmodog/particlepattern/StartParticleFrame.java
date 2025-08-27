package antonafanasjew.cosmodog.particlepattern;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Date;

import javax.swing.JFrame;

import antonafanasjew.cosmodog.domains.MapType;
import antonafanasjew.cosmodog.particlepattern.model.GridParticlePatternBuilder;
import antonafanasjew.cosmodog.particlepattern.model.ParticlePattern;
import antonafanasjew.cosmodog.particlepattern.model.ParticlePatternBuilder;
import antonafanasjew.cosmodog.particlepattern.movement.SinusMovementFunction;
import antonafanasjew.cosmodog.topology.PlacedRectangle;
import antonafanasjew.cosmodog.topology.Rectangle;
import antonafanasjew.cosmodog.topology.Vector;

import com.google.common.base.Function;

public class StartParticleFrame {


	public static final float CLOUD_TILE_WIDTH = 2000;
	public static final float CLOUD_TILE_HEIGHT = 2000;
	
	public static Vector CAM_OFFSET = new Vector(0f, 0f);
		
	public static long INITIAL_TIMESTAMP = new Date().getTime();
	
	//public static Function<Float, Vector> MOVEMENT_FUNCTION = new CircleMovementFunction(1f, 0.3333f, 400, true, 1000);
	public static Function<Long, Vector> MOVEMENT_FUNCTION = new SinusMovementFunction(25f, 100f, 0f, 1000);

	public static ParticlePattern PARTICLE_PATTERN = new ParticlePattern(Rectangle.fromSize(CLOUD_TILE_WIDTH, CLOUD_TILE_HEIGHT));
	
	public static ParticlePattern CURRENT_PARTICLE_PATTERN = null;
	
	static {
		ParticlePatternBuilder ppb = new GridParticlePatternBuilder(50, 50);
		PARTICLE_PATTERN = ppb.build(Rectangle.fromSize(CLOUD_TILE_WIDTH, CLOUD_TILE_HEIGHT));
	}
	
	public static void main(String[] args) {
		
		
		final ParticleFrame frame = new ParticleFrame();
		
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600);
		frame.setVisible(true);
		
		frame.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_W) {
					CAM_OFFSET = CAM_OFFSET.add(new Vector(0, -30));
				}
				
				if (e.getKeyCode() == KeyEvent.VK_S) {
					CAM_OFFSET = CAM_OFFSET.add(new Vector(0, 30));
				}
				
				if (e.getKeyCode() == KeyEvent.VK_A) {
					CAM_OFFSET = CAM_OFFSET.add(new Vector(-30, 0));
				}
				
				if (e.getKeyCode() == KeyEvent.VK_D) {
					CAM_OFFSET = CAM_OFFSET.add(new Vector(30, 0));
				}
				frame.invalidate();
				frame.repaint();
			}
		});
		
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				while (true) {
					
					long timeOffset = new Date().getTime() - INITIAL_TIMESTAMP;
					PlacedRectangle cam = PlacedRectangle.fromAnchorAndSize(CAM_OFFSET.getX(), CAM_OFFSET.getY(), frame.getContentPane().getWidth(), frame.getContentPane().getHeight(), MapType.MAIN);
					CURRENT_PARTICLE_PATTERN = newPattern(MOVEMENT_FUNCTION, cam, timeOffset);
										
					frame.invalidate();
					frame.repaint();
					
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
				}
			}
		});
		
		t.start();
		
	}
	
	private static ParticlePattern newPattern(Function<Long, Vector> movementOffsetFunction, PlacedRectangle cam, long timeOffset) {
		
		OffsetCalculator oc = new OffsetCalculator();
		oc.setMovementOffsetFunction(movementOffsetFunction);
		oc.setParticlePattern(PARTICLE_PATTERN);
		return oc.particlePatternForPlaceAndTime(cam, timeOffset);
	}
	
}
