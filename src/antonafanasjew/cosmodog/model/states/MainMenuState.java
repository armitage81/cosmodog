package antonafanasjew.cosmodog.model.states;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
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
import antonafanasjew.cosmodog.util.MusicUtils;
import antonafanasjew.cosmodog.util.TextBookRendererUtils;

public class MainMenuState extends CosmodogAbstractState {

	private Menu mainMenu;
	private MenuRenderer menuRenderer = new MenuRenderer();
	private MenuRenderingParam param = new MenuRenderingParam();
	
	@Override
	public void everyEnter(GameContainer container, StateBasedGame game) throws SlickException {
		container.getInput().clearKeyPressedRecord();
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
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		
		DrawingContext gameContainerDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().gameContainerDrawingContext();
		DrawingContext startScreenLogoDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().startScreenLogoDrawingContext();
		DrawingContext startScreenReferencesDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().startScreenReferencesDrawingContext();
				
		Animation titleAnimation = ApplicationContext.instance().getAnimations().get("title");
		
		titleAnimation.draw(gameContainerDrawingContext.x(), gameContainerDrawingContext.y(), gameContainerDrawingContext.w(), gameContainerDrawingContext.h());
		
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
