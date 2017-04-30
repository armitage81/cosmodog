package antonafanasjew.cosmodog.model.states;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.CosmodogStarter;
import antonafanasjew.cosmodog.MusicResources;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.globals.FontType;
import antonafanasjew.cosmodog.model.menu.Menu;
import antonafanasjew.cosmodog.model.menu.MenuElement;
import antonafanasjew.cosmodog.model.menu.MenuItem;
import antonafanasjew.cosmodog.rendering.context.CenteredDrawingContext;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.SimpleDrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.MenuRenderer;
import antonafanasjew.cosmodog.rendering.renderer.MenuRenderer.MenuRenderingParam;
import antonafanasjew.cosmodog.util.TextBookRendererUtils;

public class MainMenuState extends BasicGameState {

	private Menu mainMenu;
	private MenuRenderer menuRenderer = new MenuRenderer();
	private MenuRenderingParam param = new MenuRenderingParam();
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		// TODO Auto-generated method stub

	}

	@Override
	public void leave(GameContainer container, StateBasedGame game) throws SlickException {
		super.leave(container, game);
		ApplicationContext.instance().getMusicResources().get(MusicResources.MUSIC_MAIN_MENU).stop();
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		container.getInput().clearKeyPressedRecord();
		mainMenu = ApplicationContext.instance().getMenus().get("mainMenu");
		mainMenu.setInitialized();
		menuRenderer.resetMenuLabelCache();
		ApplicationContext.instance().getMusicResources().get(MusicResources.MUSIC_MAIN_MENU).loop();
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
		
		
		DrawingContext dc = new SimpleDrawingContext(null, 0, 0, gc.getWidth(), gc.getHeight());
		
		DrawingContext logoDc = new TileDrawingContext(dc, 1, 3, 0, 1);
		
		DrawingContext menuDc = new TileDrawingContext(dc, 10, 10, 6, 7, 4, 2);
		
		DrawingContext referencesDc = new TileDrawingContext(dc, 10, 10, 0, 9, 10, 1);
		
		
		Animation titleAnimation = ApplicationContext.instance().getAnimations().get("title");
		
		titleAnimation.draw(dc.x(), dc.y(), dc.w(), dc.h());
		
		Animation logo = ApplicationContext.instance().getAnimations().get("logo");
		logoDc = new CenteredDrawingContext(logoDc, 640, 192);
		logo.draw(logoDc.x(), logoDc.y(), logoDc.w(), logoDc.h());
		
		param.menu = mainMenu;
		menuRenderer.render(gc, g, menuDc, param);
		
		
		
		
		String text = ApplicationContext.instance().getGameTexts().get("references").getLogText();
		TextBookRendererUtils.renderCenteredLabel(gc, g, referencesDc, text, FontType.References);
		
	}

	@Override
	public int getID() {
		return CosmodogStarter.MAIN_MENU_STATE_ID;
	}

}
