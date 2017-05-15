package antonafanasjew.cosmodog.rendering.renderer;

import java.util.List;
import java.util.Set;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Sound;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.collision.CollisionStatus;
import antonafanasjew.cosmodog.collision.EnergyWallCollisionValidator;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.Effect;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

import com.google.common.collect.Lists;



public class EffectsRenderer extends AbstractRenderer {

	public static class EffectsRendererParam {
		
		public static EffectsRendererParam FOR_GROUND_EFFECTS = new EffectsRendererParam(Lists.newArrayList(Effect.EFFECT_TYPE_ELECTRICITY, Effect.EFFECT_TYPE_TELEPORT));
		public static EffectsRendererParam FOR_TOP_EFFECTS = new EffectsRendererParam(Lists.newArrayList(Effect.EFFECT_TYPE_SMOKE, Effect.EFFECT_TYPE_BIRDS, Effect.EFFECT_TYPE_FIRE, Effect.EFFECT_TYPE_ENERGYWALL));

		private List<String> effectTypes;

		public EffectsRendererParam(List<String> effectTypes) {
			this.effectTypes = effectTypes;
		}
		
		public List<String> getEffectTypes() {
			return effectTypes;
		}

		
	}
	

	//We need it to calculate whether to render the energy wall effect or not.
	private EnergyWallCollisionValidator energyWallCollisionValidator = new EnergyWallCollisionValidator();
	
	@Override
	protected void renderFromZero(GameContainer gameContainer, Graphics graphics, DrawingContext drawingContext, Object renderingParameter) {

		EffectsRendererParam param = (EffectsRendererParam)renderingParameter;
		List<String> effectsToRender = param.getEffectTypes();
		
		ApplicationContext applicationContext = ApplicationContext.instance();
		Cosmodog cosmodog = applicationContext.getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();
		CosmodogMap map = cosmodogGame.getMap();
		Cam cam = cosmodogGame.getCam();
		
		int tileWidth = map.getTileWidth();
		int tileHeight = map.getTileHeight();

		int scaledTileWidth = (int) (tileWidth * cam.getZoomFactor());
		int scaledTileHeight = (int) (tileHeight * cam.getZoomFactor());

		int camX = (int) cam.viewCopy().x();
		int camY = (int) cam.viewCopy().y();

		int x = -(int) ((camX % scaledTileWidth));
		int y = -(int) ((camY % scaledTileHeight));
		
		int tileNoX = camX / scaledTileWidth;
		int tileNoY = camY / scaledTileHeight;
		
		int tilesW = (int) (cam.viewCopy().width()) / scaledTileWidth + 2;
		int tilesH = (int) (cam.viewCopy().height()) / scaledTileHeight + 2;
		
		graphics.translate(x, y);
		graphics.scale(cam.getZoomFactor(), cam.getZoomFactor());
		
		Set<Piece> effectPieces = map.visibleEffectPieces(tileNoX, tileNoY, tilesW, tilesH, 2);
		
		
		
		boolean audibleElectricity = false;
		boolean audibleEnergyWall = false;
		boolean audibleFire = false;
		
		for (Piece piece : effectPieces) {
			if (piece instanceof Effect) {
				Effect effect = (Effect) piece;
				if (effectsToRender.contains(effect.getEffectType())) {
					
					if (effect.getEffectType().equals(Effect.EFFECT_TYPE_TELEPORT)) {
    					applicationContext.getAnimations().get("teleportEffect").draw((piece.getPositionX() - tileNoX) * tileWidth, (piece.getPositionY() - tileNoY) * tileHeight);
    				}
					
    				if (effect.getEffectType().equals(Effect.EFFECT_TYPE_FIRE)) {
    					audibleFire = true;
    					applicationContext.getAnimations().get("fire").draw((piece.getPositionX() - tileNoX) * tileWidth, (piece.getPositionY() - tileNoY) * (tileHeight - 1));
    				}
    				if (effect.getEffectType().equals(Effect.EFFECT_TYPE_SMOKE)) {
    					applicationContext.getAnimations().get("smoke").draw((piece.getPositionX() - tileNoX) * tileWidth, (piece.getPositionY() - tileNoY) * tileHeight);
    				}
    				if (effect.getEffectType().equals(Effect.EFFECT_TYPE_ELECTRICITY)) {
    					audibleElectricity = true;
    					applicationContext.getAnimations().get("electricity").draw((piece.getPositionX() - tileNoX - 2) * tileWidth, (piece.getPositionY() - tileNoY - 1) * tileHeight);
    				}
    				if (effect.getEffectType().equals(Effect.EFFECT_TYPE_ENERGYWALL)) {
    					//Only render, if the energy wall is not passable.
    					CollisionStatus energyWallCollisionStatus = energyWallCollisionValidator.collisionStatus(cosmodogGame, ApplicationContextUtils.getPlayer(), map, piece.getPositionX(), piece.getPositionY());
    					
    					if (energyWallCollisionStatus.isPassable() == false) {
    						audibleEnergyWall = true;
    						applicationContext.getAnimations().get("energywall").draw((piece.getPositionX() - tileNoX) * tileWidth, (piece.getPositionY() - tileNoY - 1) * (tileHeight));
    					}

    					
    				}
				}
			}
		}

		//Ambient sounds
		if (param.equals(EffectsRendererParam.FOR_GROUND_EFFECTS)) {
			if (audibleElectricity) {
				Sound sound = applicationContext.getSoundResources().get(SoundResources.SOUND_AMBIENT_ELECTRICITY);
				cosmodogGame.getAmbientSoundRegistry().put(SoundResources.SOUND_AMBIENT_ELECTRICITY, sound);
			} else {
				cosmodogGame.getAmbientSoundRegistry().remove(SoundResources.SOUND_AMBIENT_ELECTRICITY);
			}
			
		}
		if (param.equals(EffectsRendererParam.FOR_TOP_EFFECTS)) {
			if (audibleEnergyWall) {
				Sound sound = applicationContext.getSoundResources().get(SoundResources.SOUND_AMBIENT_ENERGYWALL);
				cosmodogGame.getAmbientSoundRegistry().put(SoundResources.SOUND_AMBIENT_ENERGYWALL, sound);
			} else {
				cosmodogGame.getAmbientSoundRegistry().remove(SoundResources.SOUND_AMBIENT_ENERGYWALL);
			}
			if (audibleFire) {
				Sound sound = applicationContext.getSoundResources().get(SoundResources.SOUND_AMBIENT_FIRE);
				cosmodogGame.getAmbientSoundRegistry().put(SoundResources.SOUND_AMBIENT_FIRE, sound);
			} else {
				cosmodogGame.getAmbientSoundRegistry().remove(SoundResources.SOUND_AMBIENT_FIRE);
			}
		}

		
		graphics.scale(1 / cam.getZoomFactor(), 1 / cam.getZoomFactor());
		graphics.translate(-x, -y);
		
	}

}
