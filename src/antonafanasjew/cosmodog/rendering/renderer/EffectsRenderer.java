package antonafanasjew.cosmodog.rendering.renderer;

import java.util.List;
import java.util.Set;

import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.model.portals.Entrance;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.topology.Vector;
import antonafanasjew.cosmodog.util.TileUtils;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Sound;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.collision.CollisionStatus;
import antonafanasjew.cosmodog.collision.validators.player.EnergyWallCollisionValidatorForPlayer;
import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.Effect;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

import com.google.common.collect.Lists;



public class EffectsRenderer extends AbstractRenderer {

	public record EffectsRendererParam(List<String> effectTypes) {

		public static EffectsRendererParam FOR_GROUND_EFFECTS = new EffectsRendererParam(Lists.newArrayList(Effect.EFFECT_TYPE_ELECTRICITY, Effect.EFFECT_TYPE_TELEPORT));
		public static EffectsRendererParam FOR_TOP_EFFECTS = new EffectsRendererParam(Lists.newArrayList(Effect.EFFECT_TYPE_SMOKE, Effect.EFFECT_TYPE_BIRDS, Effect.EFFECT_TYPE_FIRE, Effect.EFFECT_TYPE_ENERGYWALL));

	}

	//We need it to calculate whether to render the energy wall effect or not.
	private final EnergyWallCollisionValidatorForPlayer energyWallCollisionValidator = new EnergyWallCollisionValidatorForPlayer();
	
	@Override
	public void renderInternally(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {

		int tileLength = TileUtils.tileLengthSupplier.get();

		DrawingContext sceneDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().sceneDrawingContext();
		
		EffectsRendererParam param = (EffectsRendererParam)renderingParameter;
		List<String> effectsToRender = param.effectTypes();
		
		ApplicationContext applicationContext = ApplicationContext.instance();
		Cosmodog cosmodog = applicationContext.getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();
		CosmodogMap map = cosmodogGame.mapOfPlayerLocation();
		Cam cam = cosmodogGame.getCam();

		Cam.CamTilePosition camTilePosition = cam.camTilePosition();

		graphics.translate(sceneDrawingContext.x(), sceneDrawingContext.y());
		graphics.translate(camTilePosition.offsetX(), camTilePosition.offsetY());
		graphics.scale(cam.getZoomFactor(), cam.getZoomFactor());

		Position tilePosition = Position.fromCoordinates(camTilePosition.tileX(), camTilePosition.tileY(), map.getMapDescriptor());

		Set<Piece> effectPieces = map.visibleEffectPieces(tilePosition, camTilePosition.widthInTiles(), camTilePosition.heightInTiles(), 2);

		boolean audibleElectricity = false;
		boolean audibleEnergyWall = false;
		boolean audibleFire = false;
		
		for (Piece piece : effectPieces) {

			Vector pieceVectorRelatedToCam = Cam.positionVectorRelatedToCamTilePosition(piece.getPosition(), camTilePosition);
			Vector adjacentNorthPieceVectorRelatedToCam = new Vector(pieceVectorRelatedToCam.getX(), pieceVectorRelatedToCam.getY() - tileLength);
			Vector electricityEffectVectorRelatedToCam = new Vector(pieceVectorRelatedToCam.getX() - 2 * tileLength, pieceVectorRelatedToCam.getY() - tileLength);


			if (piece instanceof Effect effect) {

				float pieceX = pieceVectorRelatedToCam.getX();
				float pieceY = pieceVectorRelatedToCam.getY();
				float pieceNorthY = adjacentNorthPieceVectorRelatedToCam.getY();
				float electricityEffectX = electricityEffectVectorRelatedToCam.getX();
				float electricityEffectY = electricityEffectVectorRelatedToCam.getY();

                if (effectsToRender.contains(effect.getEffectType())) {
					
					if (effect.getEffectType().equals(Effect.EFFECT_TYPE_TELEPORT)) {
    					applicationContext.getAnimations().get("teleportEffect").draw(pieceX, pieceY);
    				}
					
    				if (effect.getEffectType().equals(Effect.EFFECT_TYPE_FIRE)) {
    					audibleFire = true;
    					applicationContext.getAnimations().get("fire").draw(pieceX, pieceNorthY);
    				}
    				if (effect.getEffectType().equals(Effect.EFFECT_TYPE_SMOKE)) {
    					applicationContext.getAnimations().get("smoke").draw(pieceX, pieceY);
    				}
    				if (effect.getEffectType().equals(Effect.EFFECT_TYPE_ELECTRICITY)) {
    					audibleElectricity = true;
    					applicationContext.getAnimations().get("electricity").draw(electricityEffectX, electricityEffectY);
    				}
    				if (effect.getEffectType().equals(Effect.EFFECT_TYPE_ENERGYWALL)) {
    					//Only render, if the energy wall is not passable.
						Entrance entrance = Entrance.instance(piece.getPosition(), DirectionType.UP);
    					CollisionStatus energyWallCollisionStatus = energyWallCollisionValidator.collisionStatus(cosmodogGame, ApplicationContextUtils.getPlayer(), map, entrance);
    					
    					if (!energyWallCollisionStatus.isPassable()) {
    						audibleEnergyWall = true;
    						applicationContext.getAnimations().get("energywall").draw(pieceX, pieceNorthY);
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
		graphics.translate(-camTilePosition.offsetX(), -camTilePosition.offsetY());
		graphics.translate(-sceneDrawingContext.x(), -sceneDrawingContext.y());
		
	}

}
