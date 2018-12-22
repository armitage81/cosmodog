package antonafanasjew.cosmodog.model.states;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.CosmodogStarter;
import antonafanasjew.cosmodog.MusicResources;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.globals.FontType;
import antonafanasjew.cosmodog.model.menu.Menu;
import antonafanasjew.cosmodog.model.menu.MenuElement;
import antonafanasjew.cosmodog.model.menu.MenuItem;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.MenuRenderer;
import antonafanasjew.cosmodog.rendering.renderer.MenuRenderer.MenuRenderingParam;
import antonafanasjew.cosmodog.topology.Vector;
import antonafanasjew.cosmodog.util.MusicUtils;
import antonafanasjew.cosmodog.util.TextBookRendererUtils;
import antonafanasjew.particlepattern.movement.SinusMovementFunction;

public class MainMenuState extends CosmodogAbstractState {

	private Menu mainMenu;
	private MenuRenderer menuRenderer = new MenuRenderer();
	private MenuRenderingParam param = new MenuRenderingParam();
	private SinusMovementFunction flying = new SinusMovementFunction(0, 10, 0, 500);
	private long stateEnteredTime;
	
	@Override
	public void everyEnter(GameContainer container, StateBasedGame game) throws SlickException {
		container.getInput().clearKeyPressedRecord();
		stateEnteredTime = System.currentTimeMillis();
		mainMenu = ApplicationContext.instance().getMenus().get("mainMenu");
		mainMenu.setInitialized();
		menuRenderer.resetMenuLabelCache();
		MusicUtils.loopMusic(MusicResources.MUSIC_MAIN_MENU);
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int n) throws SlickException {

		if (gc.getInput().isKeyPressed(Input.KEY_DOWN)) {
			mainMenu.selectNext();
			ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_MENU_MOVE).play();
		} else if(gc.getInput().isKeyPressed(Input.KEY_UP)) {
			mainMenu.selectPrevious();
			ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_MENU_MOVE).play();
		} else if (gc.getInput().isKeyPressed(Input.KEY_ENTER)) {
			MenuElement menuElement = mainMenu.getSelectedMenuElement();
			if (menuElement instanceof MenuItem) {
				((MenuItem)menuElement).getMenuAction().execute(sbg);
				ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_MENU_SELECT).play();
			} else {
				mainMenu = (Menu)menuElement;
				ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_MENU_SUB).play();
			}
		} else if (gc.getInput().isKeyPressed(Input.KEY_ESCAPE)) {
			Menu parentMenu = mainMenu.getParentMenu();
			if (parentMenu != null) {
				mainMenu = parentMenu;
				ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_MENU_BACK).play();
			}
		}
		
	}

	@Override
	public void leave(GameContainer container, StateBasedGame game) throws SlickException {
		Music music = ApplicationContext.instance().getMusicResources().get(MusicResources.MUSIC_MAIN_MENU);
		if (music.playing()) {
			music.stop();
		}
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		
		long timestamp = System.currentTimeMillis();
		long stateDuration = timestamp - stateEnteredTime; 
		
		DrawingContext gameContainerDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().gameContainerDrawingContext();
		DrawingContext startScreenLogoDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().startScreenLogoDrawingContext();
		DrawingContext startScreenReferencesDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().startScreenReferencesDrawingContext();
				
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
		
		Vector flyingVector = flying.apply(stateDuration);
		float shipOffsetX = flyingVector.getX();
		float shipOffsetY = flyingVector.getY();
		
		shipFrameCalmFlight.draw(gameContainerDrawingContext.x() - 20 + shipOffsetX, gameContainerDrawingContext.y() - 20 + shipOffsetY, gameContainerDrawingContext.w() + 40, gameContainerDrawingContext.h() + 40);
		
		Animation logo = ApplicationContext.instance().getAnimations().get("logo");
		
		logo.draw(startScreenLogoDrawingContext.x(), startScreenLogoDrawingContext.y(), startScreenLogoDrawingContext.w(), startScreenLogoDrawingContext.h());
		
		param.menu = mainMenu;
		menuRenderer.render(gc, g, param);
		
		
		
		
		String text = ApplicationContext.instance().getGameTexts().get("references").getLogText();
		TextBookRendererUtils.renderCenteredLabel(gc, g, startScreenReferencesDrawingContext, text, FontType.References, 0);
		
	}

	@Override
	public int getID() {
		return CosmodogStarter.MAIN_MENU_STATE_ID;
	}

}
