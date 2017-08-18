package antonafanasjew.cosmodog.experiments.colorfilter;

import org.newdawn.slick.Animation;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;


public class ColorFilterExperimentStarter extends BasicGame {

	SpriteSheet spriteSheet;
	Animation animation;
	
	public ColorFilterExperimentStarter(String title) throws SlickException {
		super(title);		
	}

	public static void main(String[] arguments) throws SlickException {
		AppGameContainer app = new AppGameContainer(new ColorFilterExperimentStarter(ColorFilterExperimentStarter.class.getName()));
		app.setDisplayMode(500, 400, false);
		app.start();
	}

	@Override
	public void init(GameContainer container) throws SlickException {
	
		spriteSheet = new SpriteSheet("data/enemy_trike.png", 16, 16);
		
		animation = new Animation();
		animation.setAutoUpdate(true);
		
		Image i1 = spriteSheet.getSprite(0, 2);
		Image i2 = spriteSheet.getSprite(1, 2);
		Image i3 = spriteSheet.getSprite(2, 2);
		Image i4 = spriteSheet.getSprite(3, 2);
					
		animation.addFrame(i1, 100);
		animation.addFrame(i2, 100);
		animation.addFrame(i3, 100);
		animation.addFrame(i4, 100);
			
		
		boolean autoUpdate = true;
		boolean looping = true;
		
		animation.setAutoUpdate(autoUpdate);
		animation.setLooping(looping);
	
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {
	}

	public void render(GameContainer container, Graphics g) throws SlickException {
		g.setColor(Color.green);
		g.fillRect(0, 0, container.getWidth(), container.getHeight());
		g.translate(100, 100);
		g.scale(4, 4);
		animation.draw(0, 0);
		g.translate(-100, -100);
		g.scale(0.25f, 0.25f);
	
	}

}
