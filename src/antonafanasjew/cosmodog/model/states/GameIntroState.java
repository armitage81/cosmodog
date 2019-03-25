package antonafanasjew.cosmodog.model.states;

import java.util.List;

import javax.crypto.spec.OAEPParameterSpec;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.CosmodogStarter;
import antonafanasjew.cosmodog.MusicResources;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.globals.FontType;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.statetransitions.LoadingTransition;
import antonafanasjew.cosmodog.topology.Vector;
import antonafanasjew.cosmodog.util.MusicUtils;
import antonafanasjew.cosmodog.util.TextBookRendererUtils;
import antonafanasjew.particlepattern.movement.ShakingMovementFunction;
import antonafanasjew.particlepattern.movement.SinusMovementFunction;

import com.google.common.collect.Lists;

public class GameIntroState  extends CosmodogAbstractState {
	
	private List<String> texts = Lists.newArrayList();
	private int page;
	
	private SinusMovementFunction flying = new SinusMovementFunction(0, 10, 0, 500);
	private ShakingMovementFunction exploding = new ShakingMovementFunction(30, 10, 10, 3, 2);
	private ShakingMovementFunction shaking = new ShakingMovementFunction(50, 6, 3, 6, 4);
	
	private long phaseStart;
	private long pageStart;
	
	private IntroPhase phase;

	private enum IntroPhase {
		CALM_FLIGHT,
		EXPLOSION,
		FALLING,
		TEXT
	}

	@Override
	public void everyEnter(GameContainer gc, StateBasedGame sbg) throws SlickException {
		
		Animation phaetonBackground = ApplicationContext.instance().getAnimations().get("phaetonBackground");
		Image backgroundImage = phaetonBackground.getCurrentFrame();
		backgroundImage.setRotation(0f);
		
		phaseStart = System.currentTimeMillis();
		phase = IntroPhase.CALM_FLIGHT;
		
		ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_INTRO_MISSILE_ALERT).play();
		
		String intro1 = ApplicationContext.instance().getGameTexts().get("intro1").getLogText();
		String intro2 = ApplicationContext.instance().getGameTexts().get("intro2").getLogText();
		String intro3 = ApplicationContext.instance().getGameTexts().get("intro3").getLogText();
		String intro4 = ApplicationContext.instance().getGameTexts().get("intro4").getLogText();
		String intro5 = ApplicationContext.instance().getGameTexts().get("intro5").getLogText();
		
		texts.clear();
		texts.add(intro1);
		texts.add(intro2);
		texts.add(intro3);
		texts.add(intro4);
		texts.add(intro5);
		
		page = 0;
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
				
//		DrawingContext gameContainerDrawingContext = new SimpleDrawingContext(null, 0, 0, gc.getWidth(), gc.getHeight());
//		
//		gameContainerDrawingContext = new CenteredDrawingContext(gameContainerDrawingContext, 1000, 500);
//		
//		DrawingContext introTextDc = new TileDrawingContext(gameContainerDrawingContext, 1, 7, 0, 0, 1, 6);
//		DrawingContext pressEnterTextDc = new TileDrawingContext(gameContainerDrawingContext, 1, 7, 0, 6, 1, 1);
//		
//		TextBookRendererUtils.renderTextPage(gc, g, introTextDc, texts.get(page), FontType.IntroText, 0);
//		
//		boolean renderBlinkingHint = (System.currentTimeMillis() / 250 % 2) == 1;
//		if (renderBlinkingHint) {
//			TextBookRendererUtils.renderCenteredLabel(gc, g, pressEnterTextDc, "Press [ENTER]", FontType.PopUpInterface, 0);
//		}
		
		renderPhase(gc, sbg, g);
		
	}
	
	private void updatePhase(GameContainer gc, StateBasedGame sbg) {
		
		Input input = gc.getInput();
		
		long timestamp = System.currentTimeMillis();
		
		IntroPhase phaseBeforeSwitch = phase;
		
		if (phase == IntroPhase.CALM_FLIGHT) {

			if (timestamp - phaseStart >= 10000) {
				phase = IntroPhase.EXPLOSION;
				ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_GUARDIAN_DESTROYED).play();
			}
			
		} else if (phase == IntroPhase.EXPLOSION) {
			
			if (timestamp - phaseStart >= 3000) {
				phase = IntroPhase.FALLING;
			}
			
		} else if (phase == IntroPhase.FALLING) {
			
			if (timestamp - phaseStart >= 6000) {
				phase = IntroPhase.TEXT;
				pageStart = System.currentTimeMillis();
				ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_TEXT_TYPING).loop();
				MusicUtils.loopMusic(MusicResources.MUSIC_CUTSCENE);
			}

			Sound sirenSound = ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_INTRO_SIREN);
			
			if (!sirenSound.playing()) {
				sirenSound.play();
			}
		} else if (phase == IntroPhase.TEXT) {
		
			if (input.isKeyPressed(Input.KEY_ENTER)) {
				ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_MENU_SELECT).play();
				if (page < texts.size() - 1) {
					page++;
					pageStart = System.currentTimeMillis();
					ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_TEXT_TYPING).stop();
					ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_TEXT_TYPING).loop();
				} else {
					ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_TEXT_TYPING).stop();
					sbg.enterState(CosmodogStarter.GAME_STATE_ID, new LoadingTransition(), new FadeInTransition());
				}
			}
			
		}
		
		if (phaseBeforeSwitch != phase) {
			phaseStart = timestamp;
		}
		
		//After processing a loop, clear the record of pressed buttons.
		input.clearKeyPressedRecord();
		
	}
	
	private void renderPhase(GameContainer gc, StateBasedGame sbg, Graphics g) {
		
		DrawingContext dc = DrawingContextProviderHolder.get().getDrawingContextProvider().gameContainerDrawingContext();
		DrawingContext gameIntroTextDc = DrawingContextProviderHolder.get().getDrawingContextProvider().gameIntroTextDrawingContext();
		DrawingContext gameIntroControlsDc = DrawingContextProviderHolder.get().getDrawingContextProvider().gameIntroControlsDrawingContext();
		
		long timestamp = System.currentTimeMillis();
		long phaseDuration = timestamp - phaseStart;
		
		if (phase == IntroPhase.CALM_FLIGHT) {
			
			Animation shipFrameCalmFlight = ApplicationContext.instance().getAnimations().get("introShipCalmFlight");
			Animation phaetonBackground = ApplicationContext.instance().getAnimations().get("phaetonBackground");
			
			float backgroundLength = gc.getWidth() * 1.3f;

			Image backgroundImage = phaetonBackground.getCurrentFrame();
			
			backgroundImage.draw(
					-(backgroundLength - gc.getWidth()) / 2, 
					-(backgroundLength - gc.getHeight()) / 2,
					backgroundLength, 
					backgroundLength
			);
			
			Vector flyingVector = flying.apply(phaseDuration);
			float shipOffsetX = flyingVector.getX();
			float shipOffsetY = flyingVector.getY();
			
			shipFrameCalmFlight.draw(dc.x() - 20 + shipOffsetX, dc.y() - 20 + shipOffsetY, dc.w() + 40, dc.h() + 40);
			
		}
		
		if (phase == IntroPhase.EXPLOSION) {
			
			Animation shipFrame = ApplicationContext.instance().getAnimations().get("introShipDamaged");
			Animation phaetonBackground = ApplicationContext.instance().getAnimations().get("phaetonBackground");
			
			float backgroundLength = gc.getWidth() * 1.3f;

			Image backgroundImage = phaetonBackground.getCurrentFrame();
			
			backgroundImage.draw(
					-(backgroundLength - gc.getWidth()) / 2, 
					-(backgroundLength - gc.getHeight()) / 2,
					backgroundLength, 
					backgroundLength
			);
			
			Vector explodingVector = exploding.apply(phaseDuration);
			float shipOffsetX = explodingVector.getX();
			float shipOffsetY = explodingVector.getY();
			
			shipFrame.draw(dc.x() - 20 + shipOffsetX, dc.y() - 20 + shipOffsetY, dc.w() + 40, dc.h() + 40);			
		}
		
		if (phase == IntroPhase.FALLING) {

			Animation shipFrame = ApplicationContext.instance().getAnimations().get("introShipDamaged");
			Animation phaetonBackground = ApplicationContext.instance().getAnimations().get("phaetonBackground");
			
			float backgroundLength = gc.getWidth() * 1.3f;

			Image backgroundImage = phaetonBackground.getCurrentFrame();
			backgroundImage.setCenterOfRotation(backgroundLength / 2, backgroundLength / 2);
			backgroundImage.setRotation(backgroundImage.getRotation() + 1);
			
			
			backgroundImage.draw(
					-(backgroundLength - gc.getWidth()) / 2, 
					-(backgroundLength - gc.getHeight()) / 2,
					backgroundLength, 
					backgroundLength
			);
								
			
			Vector shakingVector = shaking.apply(phaseDuration);
			float shipOffsetX = shakingVector.getX();
			float shipOffsetY = shakingVector.getY();
			
			shipFrame.draw(dc.x() - 20 + shipOffsetX, dc.y() - 20 + shipOffsetY, dc.w() + 40, dc.h() + 40);
			
			int warnLampRest = (int)((timestamp / 200) % 2);
			
			if (warnLampRest == 0) {
				g.setColor(new Color(1f, 0f, 0f, 0.2f));
				g.fillRect(dc.x(), dc.y(), dc.w(), dc.h());
			}
			
		}
		
		if (phase == IntroPhase.TEXT) {
			Animation shipFrame = ApplicationContext.instance().getAnimations().get("introShipDamaged");
			Animation phaetonBackground = ApplicationContext.instance().getAnimations().get("phaetonBackground");
			
			float backgroundLength = gc.getWidth() * 1.3f;

			Image backgroundImage = phaetonBackground.getCurrentFrame();
			backgroundImage.setCenterOfRotation(backgroundLength / 2, backgroundLength / 2);
			backgroundImage.setRotation(backgroundImage.getRotation() + 1);
			
			
			backgroundImage.draw(
					-(backgroundLength - gc.getWidth()) / 2, 
					-(backgroundLength - gc.getHeight()) / 2,
					backgroundLength, 
					backgroundLength
			);
								
			
			Vector shakingVector = shaking.apply(phaseDuration);
			float shipOffsetX = shakingVector.getX();
			float shipOffsetY = shakingVector.getY();
			
			shipFrame.draw(dc.x() - 20 + shipOffsetX, dc.y() - 20 + shipOffsetY, dc.w() + 40, dc.h() + 40);
			
			g.setColor(new Color(0f, 0f, 0f, 0.9f));
			g.fillRect(dc.x(), dc.y(), dc.w(), dc.h());
			
			long millisSinsePageOpened = System.currentTimeMillis() - pageStart;
			TextBookRendererUtils.renderDynamicTextPage(gc, g, gameIntroTextDc, texts.get(page), FontType.IntroText, 0, millisSinsePageOpened);
						
			boolean renderBlinkingHint = (System.currentTimeMillis() / 250 % 2) == 1;
			if (renderBlinkingHint) {
				TextBookRendererUtils.renderCenteredLabel(gc, g, gameIntroControlsDc, "Press [ENTER]", FontType.PopUpInterface, 0);
			}
			
		}
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		
		updatePhase(gc, sbg);
		
//		if (gc.getInput().isKeyPressed(Input.KEY_ENTER)) {
//			ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_MENU_SELECT).play();
//			if (page < texts.size() - 1) {
//				page++;
//			} else {
//				sbg.enterState(CosmodogStarter.GAME_STATE_ID, new LoadingTransition(), new FadeInTransition());
//			}
//		}
		
	}

	@Override
	public int getID() {
		return CosmodogStarter.GAME_INTRO_STATE_ID;
	}

}
