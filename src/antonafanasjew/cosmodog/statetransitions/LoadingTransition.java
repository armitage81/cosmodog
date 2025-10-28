package antonafanasjew.cosmodog.statetransitions;

import antonafanasjew.cosmodog.model.MapDescriptor;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.Transition;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.CustomTiledMap;
import antonafanasjew.cosmodog.globals.FontProvider.FontTypeName;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.SimpleDrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.textbook.FontRefToFontTypeMap;
import antonafanasjew.cosmodog.rendering.renderer.textbook.TextPageConstraints;
import antonafanasjew.cosmodog.rendering.renderer.textbook.placement.Book;
import antonafanasjew.cosmodog.util.TextBookRendererUtils;

import java.util.Map;

public class LoadingTransition implements Transition {

	private Map<MapDescriptor, CustomTiledMap> tiledMaps = null;
	
	private DrawingContext mainDrawingContext;
	
	private boolean startedLoading = false;


	
	@Override
	public void init(GameState arg0, GameState arg1) {
		
		startedLoading = false;
		
	}

	@Override
	public boolean isComplete() {
		return tiledMaps != null;
	}

	
	
	@Override
	public void postRender(StateBasedGame sbg, GameContainer gc, Graphics g) throws SlickException {

		mainDrawingContext = new SimpleDrawingContext(null, 0, 0, gc.getWidth(), gc.getHeight());
		g.setColor(Color.black);
		g.fillRect(mainDrawingContext.x(), mainDrawingContext.y(), mainDrawingContext.w(), mainDrawingContext.h());
		FontRefToFontTypeMap fontRefToFontTypeMap = FontRefToFontTypeMap.forOneFontTypeName(FontTypeName.LoadingOrGameOverOrTheEnd);
		Book loadingBook = TextPageConstraints.fromDc(mainDrawingContext).textToBook("Loading...", fontRefToFontTypeMap);
		TextBookRendererUtils.renderCenteredLabel(gc, g, loadingBook);
		
		
	}

	@Override
	public void preRender(StateBasedGame sbg, GameContainer gc, Graphics g) throws SlickException {
		
	}

	@Override
	public void update(StateBasedGame sbg, GameContainer gc, int delta) throws SlickException {

		gc.getInput().clearKeyPressedRecord();
		
		if (!startedLoading) {
			tiledMaps = ApplicationContext.instance().getCustomTiledMaps();
			startedLoading = true;
		}

		
		
	}
	
}
