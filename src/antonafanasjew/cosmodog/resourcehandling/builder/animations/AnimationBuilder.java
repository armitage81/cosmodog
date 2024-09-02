package antonafanasjew.cosmodog.resourcehandling.builder.animations;

import org.newdawn.slick.Animation;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.util.Log;

import antonafanasjew.cosmodog.resourcehandling.AbstractResourceWrapperBuilder;

public class AnimationBuilder extends AbstractResourceWrapperBuilder<Animation> {

	@Override
	protected Animation build(String line) {
		String[] animationDataset = line.split(";");
		String resourceAddress = animationDataset[1];
		int spriteWidth = Integer.parseInt(animationDataset[2]);
		int spriteHeight = Integer.parseInt(animationDataset[3]);
		String frameDataset = animationDataset[4];
		frameDataset = frameDataset.substring(1, frameDataset.length() - 1);
		
		SpriteSheet spriteSheet = null;
		try {
			spriteSheet = new SpriteSheet(resourceAddress, spriteWidth, spriteHeight);
		} catch (SlickException e) {
			Log.error("Could not load sprite sheet from the resource: " + resourceAddress + ". " + e.getLocalizedMessage(), e);
		}
		
		Animation animation = new Animation();

		String[] frameData = frameDataset.split(",");
		
		for (String frameDate : frameData) {
			String[] frameDateParts = frameDate.split("/");
			int spriteX = Integer.parseInt(frameDateParts[0]);
			int spriteY = Integer.parseInt(frameDateParts[1]);
			int duration = Integer.parseInt(frameDateParts[2]);
			
			animation.addFrame(spriteSheet.getSprite(spriteX, spriteY), duration);
			
		}
		
		boolean autoUpdate = Boolean.valueOf(animationDataset[5]);
		boolean looping = Boolean.valueOf(animationDataset[6]);
		
		animation.setAutoUpdate(autoUpdate);
		animation.setLooping(looping);

		return animation;
	}

	@Override
	protected String resourcePath() {
		return "antonafanasjew/cosmodog/animationbuilder/animations.txt";
	}

}
