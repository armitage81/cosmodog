package antonafanasjew.cosmodog.model.states;

import antonafanasjew.cosmodog.rendering.context.CenteredDrawingContext;
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
import antonafanasjew.cosmodog.globals.Features;
import antonafanasjew.cosmodog.globals.FontProvider.FontTypeName;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.textbook.FontRefToFontTypeMap;
import antonafanasjew.cosmodog.rendering.renderer.textbook.TextPageConstraints;
import antonafanasjew.cosmodog.rendering.renderer.textbook.placement.Book;
import antonafanasjew.cosmodog.statetransitions.LoadingTransition;
import antonafanasjew.cosmodog.topology.Vector;
import antonafanasjew.cosmodog.util.MusicUtils;
import antonafanasjew.cosmodog.util.TextBookRendererUtils;
import antonafanasjew.particlepattern.movement.ShakingMovementFunction;
import antonafanasjew.particlepattern.movement.SinusMovementFunction;

public class GameIntroState  extends CosmodogAbstractState {
	
	private SinusMovementFunction flying = new SinusMovementFunction(0, 10, 0, 500);
	private ShakingMovementFunction exploding = new ShakingMovementFunction(30, 10, 10, 3, 2);
	private ShakingMovementFunction shaking = new ShakingMovementFunction(50, 6, 3, 6, 4);
	
	private long phaseStart;
	
	private boolean cutsceneSkipped;
	
	private IntroPhase phase;
	
	private Book book;

	private enum IntroPhase {
		CALM_FLIGHT,
		EXPLOSION,
		FALLING,
		TEXT
	}

	@Override
	public void everyEnter(GameContainer gc, StateBasedGame sbg) throws SlickException {
		
		long referenceTime = System.currentTimeMillis();
		
		Animation phaetonBackground = ApplicationContext.instance().getAnimations().get("phaetonBackground");
		Image backgroundImage = phaetonBackground.getCurrentFrame();
		backgroundImage.setRotation(0f);
		
		phaseStart = referenceTime;
		phase = IntroPhase.CALM_FLIGHT;
		
		//Skip the intro cutscene if this feature is disabled.
		cutsceneSkipped = false;
		if (Features.getInstance().featureOn(Features.FEATURE_CUTSCENES) == false) {
			cutsceneSkipped = true;
		}
		
		ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_INTRO_MISSILE_ALERT).play();
		
		String text = ApplicationContext.instance().getGameTexts().get("intro").getLogText();
		DrawingContext textDc = DrawingContextProviderHolder.get().getDrawingContextProvider().gameIntroTextDrawingContext();
		TextPageConstraints tpc = TextPageConstraints.fromDc(textDc);
		book = tpc.textToBook(text, FontRefToFontTypeMap.forNarration(), 20);
		
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {		
		renderPhase(gc, sbg, g);
	}
	
	private void updatePhase(GameContainer gc, StateBasedGame sbg) {
		
		long referenceTime = System.currentTimeMillis();

		Input input = gc.getInput();

		if (input.isKeyPressed(Input.KEY_ESCAPE)) {
			cutsceneSkipped = true;
		}
		
		IntroPhase phaseBeforeSwitch = phase;
		
		if (phase == IntroPhase.CALM_FLIGHT) {

			if (referenceTime - phaseStart >= 10000 || cutsceneSkipped) {
				phase = IntroPhase.EXPLOSION;
				ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_GUARDIAN_DESTROYED).play();
			}
			
		} else if (phase == IntroPhase.EXPLOSION) {
			
			if (referenceTime - phaseStart >= 3000  || cutsceneSkipped) {
				phase = IntroPhase.FALLING;
			}
			
		} else if (phase == IntroPhase.FALLING) {
			
			if (referenceTime - phaseStart >= 6000  || cutsceneSkipped) {
				phase = IntroPhase.TEXT;

				book.resetTimeAfterPageOpen();
				ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_TEXT_TYPING).loop();
				MusicUtils.loopMusic(MusicResources.MUSIC_CUTSCENE);
			}

			Sound sirenSound = ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_INTRO_SIREN);
			
			if (!sirenSound.playing()) {
				sirenSound.play();
			}
		} else if (phase == IntroPhase.TEXT) {
		
			if (input.isKeyPressed(Input.KEY_ENTER)) {
				
				if (book.isSkipPageBuildUpRequest() || book.dynamicPageComplete(referenceTime)) {
				
					ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_MENU_SELECT).play();
					if (!book.onLastPage()) {
						book.nextPage();
						ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_TEXT_TYPING).stop();
						ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_TEXT_TYPING).loop();
					} else {
						ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_TEXT_TYPING).stop();
						sbg.enterState(CosmodogStarter.GAME_STATE_ID, new LoadingTransition(), new FadeInTransition());
					}
				
				} else {
					book.setSkipPageBuildUpRequest(true);
				}
			}
			
		}
		
		if (phaseBeforeSwitch != phase) {
			phaseStart = referenceTime;
		}
		
		//After processing a loop, clear the record of pressed buttons.
		input.clearKeyPressedRecord();
		
	}
	
	private void renderPhase(GameContainer gc2, StateBasedGame sbg, Graphics g) {
		
		DrawingContext dc2 = DrawingContextProviderHolder.get().getDrawingContextProvider().gameContainerDrawingContext();

		DrawingContext imageDc = new CenteredDrawingContext(dc2, 1024, 1024);

		DrawingContext controlsDc = DrawingContextProviderHolder.get().getDrawingContextProvider().gameIntroControlsDrawingContext();
		
		long referenceTime = System.currentTimeMillis();
		long phaseDuration = referenceTime - phaseStart;

		if (phase == IntroPhase.CALM_FLIGHT) {
			
			Animation shipFrameCalmFlight = ApplicationContext.instance().getAnimations().get("introShipCalmFlight");
			Animation phaetonBackground = ApplicationContext.instance().getAnimations().get("phaetonBackground");
			
			float backgroundLength = imageDc.w() * 1.3f;

			Image backgroundImage = phaetonBackground.getCurrentFrame();

			g.setClip((int)imageDc.x(), (int)imageDc.y(), (int)imageDc.w(), (int)imageDc.h());

			backgroundImage.draw(
					imageDc.x() - (backgroundLength - imageDc.w()) / 2,
					imageDc.y() - (backgroundLength - imageDc.h()) / 2,
					backgroundLength, 
					backgroundLength
			);
			
			Vector flyingVector = flying.apply(phaseDuration);
			float shipOffsetX = flyingVector.getX();
			float shipOffsetY = flyingVector.getY();

			shipFrameCalmFlight.draw(imageDc.x() - 20 + shipOffsetX, imageDc.y() - 20 + shipOffsetY, imageDc.w() + 40, imageDc.h() + 40);

			g.setClip(0, 0, gc2.getWidth(), gc2.getHeight());
		}
		
		if (phase == IntroPhase.EXPLOSION) {
			
			Animation shipFrame = ApplicationContext.instance().getAnimations().get("introShipDamaged");
			Animation phaetonBackground = ApplicationContext.instance().getAnimations().get("phaetonBackground");
			
			float backgroundLength = imageDc.w() * 1.3f;

			Image backgroundImage = phaetonBackground.getCurrentFrame();

			g.setClip((int)imageDc.x(), (int)imageDc.y(), (int)imageDc.w(), (int)imageDc.h());

			backgroundImage.draw(
					imageDc.x() - (backgroundLength - imageDc.w()) / 2,
					imageDc.y() - (backgroundLength - imageDc.h()) / 2,
					backgroundLength, 
					backgroundLength
			);
			
			Vector explodingVector = exploding.apply(phaseDuration);
			float shipOffsetX = explodingVector.getX();
			float shipOffsetY = explodingVector.getY();

			shipFrame.draw(imageDc.x() - 20 + shipOffsetX, imageDc.y() - 20 + shipOffsetY, imageDc.w() + 40, imageDc.h() + 40);

			g.setClip(0, 0, gc2.getWidth(), gc2.getHeight());
		}
		
		if (phase == IntroPhase.FALLING) {

			Animation shipFrame = ApplicationContext.instance().getAnimations().get("introShipDamaged");
			Animation phaetonBackground = ApplicationContext.instance().getAnimations().get("phaetonBackground");
			
			float backgroundLength = imageDc.w() * 1.3f;

			Image backgroundImage = phaetonBackground.getCurrentFrame();
			backgroundImage.setCenterOfRotation(backgroundLength / 2, backgroundLength / 2);
			backgroundImage.setRotation(backgroundImage.getRotation() + 1);

			g.setClip((int)imageDc.x(), (int)imageDc.y(), (int)imageDc.w(), (int)imageDc.h());
			
			backgroundImage.draw(
					imageDc.x() - (backgroundLength - imageDc.w()) / 2,
					imageDc.y() - (backgroundLength - imageDc.h()) / 2,
					backgroundLength, 
					backgroundLength
			);
								
			
			Vector shakingVector = shaking.apply(phaseDuration);
			float shipOffsetX = shakingVector.getX();
			float shipOffsetY = shakingVector.getY();
			
			shipFrame.draw(imageDc.x() - 20 + shipOffsetX, imageDc.y() - 20 + shipOffsetY, imageDc.w() + 40, imageDc.h() + 40);
			
			int warnLampRest = (int)((referenceTime / 200) % 2);
			
			if (warnLampRest == 0) {
				g.setColor(new Color(1f, 0f, 0f, 0.2f));
				g.fillRect(imageDc.x(), imageDc.y(), imageDc.w(), imageDc.h());
			}

			g.setClip(0, 0, gc2.getWidth(), gc2.getHeight());
		}
		
		if (phase == IntroPhase.TEXT) {
			Animation shipFrame = ApplicationContext.instance().getAnimations().get("introShipDamaged");
			Animation phaetonBackground = ApplicationContext.instance().getAnimations().get("phaetonBackground");
			
			float backgroundLength = imageDc.w() * 1.3f;

			Image backgroundImage = phaetonBackground.getCurrentFrame();
			backgroundImage.setCenterOfRotation(backgroundLength / 2, backgroundLength / 2);
			backgroundImage.setRotation(backgroundImage.getRotation() + 1);

			g.setClip((int)imageDc.x(), (int)imageDc.y(), (int)imageDc.w(), (int)imageDc.h());

			backgroundImage.draw(
					imageDc.x() - (backgroundLength - imageDc.w()) / 2,
					imageDc.y() - (backgroundLength - imageDc.h()) / 2,
					backgroundLength, 
					backgroundLength
			);
								
			
			Vector shakingVector = shaking.apply(phaseDuration);
			float shipOffsetX = shakingVector.getX();
			float shipOffsetY = shakingVector.getY();
			
			shipFrame.draw(imageDc.x() - 20 + shipOffsetX, imageDc.y() - 20 + shipOffsetY, imageDc.w() + 40, imageDc.h() + 40);

			g.setClip(0, 0, gc2.getWidth(), gc2.getHeight());

			g.setColor(new Color(0f, 0f, 0f, 0.9f));
			g.fillRect(dc2.x(), dc2.y(), dc2.w(), dc2.h());
			
			TextBookRendererUtils.renderDynamicTextPage(gc2, g, book);
			
			boolean renderHint = book.dynamicPageComplete(referenceTime);
			boolean renderBlinkingHint = (referenceTime / 250 % 2) == 1;
			if (renderHint && renderBlinkingHint) {
				FontRefToFontTypeMap fontRefToFontTypeMap = FontRefToFontTypeMap.forOneFontTypeName(FontTypeName.ControlsHint);
				Book controlHint = TextPageConstraints.fromDc(controlsDc).textToBook("Press [ENTER]", fontRefToFontTypeMap);
				TextBookRendererUtils.renderCenteredLabel(gc2, g, controlHint);
			}
			
		}
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		
		updatePhase(gc, sbg);
		
	}

	@Override
	public int getID() {
		return CosmodogStarter.GAME_INTRO_STATE_ID;
	}

}
