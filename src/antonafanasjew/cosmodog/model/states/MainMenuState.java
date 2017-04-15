package antonafanasjew.cosmodog.model.states;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.CosmodogStarter;
import antonafanasjew.cosmodog.model.menu.Menu;
import antonafanasjew.cosmodog.model.menu.MenuElement;
import antonafanasjew.cosmodog.model.menu.MenuItem;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.SimpleDrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.MenuRenderer;
import antonafanasjew.cosmodog.rendering.renderer.MenuRenderer.MenuRenderingParam;

public class MainMenuState extends BasicGameState {

	private Menu mainMenu;
	private MenuRenderer menuRenderer = new MenuRenderer();
	private MenuRenderingParam param = new MenuRenderingParam();
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		// TODO Auto-generated method stub

	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		mainMenu = ApplicationContext.instance().getMenus().get("mainMenu");
		mainMenu.setInitialized();
		menuRenderer.resetMenuLabelCache();
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int n) throws SlickException {

		if (gc.getInput().isKeyPressed(Input.KEY_DOWN)) {
			mainMenu.selectNext();
		} else if(gc.getInput().isKeyPressed(Input.KEY_UP)) {
			mainMenu.selectPrevious();
		} else if (gc.getInput().isKeyPressed(Input.KEY_ENTER)) {
			MenuElement menuElement = mainMenu.getSelectedMenuElement();
			if (menuElement instanceof MenuItem) {
				((MenuItem)menuElement).getMenuAction().execute(sbg);
			} else {
				mainMenu = (Menu)menuElement;
			}
		} else if (gc.getInput().isKeyPressed(Input.KEY_ESCAPE)) {
			Menu parentMenu = mainMenu.getParentMenu();
			if (parentMenu != null) {
				mainMenu = parentMenu;
			}
		}
		
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		
		
		DrawingContext dc = new SimpleDrawingContext(null, 0, 0, gc.getWidth(), gc.getHeight());
		DrawingContext menuDc = new TileDrawingContext(dc, 10, 10, 6, 7, 4, 3);
		
		
		Animation titleAnimation = ApplicationContext.instance().getAnimations().get("title");
		
		titleAnimation.draw(dc.x(), dc.y(), dc.w(), dc.h());
		
		param.menu = mainMenu;
		menuRenderer.render(gc, g, menuDc, param);
		
	}

	@Override
	public int getID() {
		return CosmodogStarter.MAIN_MENU_STATE_ID;
	}

}
