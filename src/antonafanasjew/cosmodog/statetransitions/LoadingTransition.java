package antonafanasjew.cosmodog.statetransitions;

import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.Transition;
import org.newdawn.slick.tiled.TiledMap;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.SimpleDrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.WritingRenderer;
import antonafanasjew.cosmodog.util.WritingRendererUtils;
import antonafanasjew.cosmodog.writing.dynamics.DynamicsTypes;
import antonafanasjew.cosmodog.writing.model.TextBlock;
import antonafanasjew.cosmodog.writing.textbox.WritingTextBox;
import antonafanasjew.cosmodog.writing.textbox.WritingTextBoxContent;
import antonafanasjew.cosmodog.writing.textbox.WritingTextBoxState;

import com.google.common.collect.Lists;

public class LoadingTransition implements Transition {

	private TiledMap tiledMap = null;
	
	private DrawingContext mainDrawingContext;
	private DrawingContext loadingDrawingContext;
	
	private boolean startedLoading = false;
	private boolean startedInitializing = false;
	
	private WritingTextBoxState textBoxState;
	private WritingRenderer wr;

	
	@Override
	public void init(GameState arg0, GameState arg1) {
		
		startedLoading = false;
		
	}

	@Override
	public boolean isComplete() {
		return tiledMap != null;
	}

	
	
	@Override
	public void postRender(StateBasedGame sbg, GameContainer gc, Graphics g) throws SlickException {


		if (startedInitializing  == false) {
			
			mainDrawingContext = new SimpleDrawingContext(null, 0, 0, gc.getWidth(), gc.getHeight());
			loadingDrawingContext = new TileDrawingContext(mainDrawingContext, 3, 9, 1, 4);
			
			TextBlock textBlock = new TextBlock();
			textBlock.setText("Loading...");
			textBlock.setSpeaker(WritingRendererUtils.SPEAKER_SYSTEM);
			textBlock.setDynamicsType(DynamicsTypes.STAMP.toString());
			
			List<TextBlock> text = Lists.newArrayList(textBlock);
			
			WritingTextBox textBox = new WritingTextBox(loadingDrawingContext.w(), loadingDrawingContext.h(), 0, 5, 20, 30);
			WritingTextBoxContent textBoxContent = new WritingTextBoxContent(textBox, text);
			textBoxState = new WritingTextBoxState(textBoxContent);
			textBoxState.update(10000);
			wr = new WritingRenderer(true);
			
			startedInitializing = true;
		}
		
		
		
		g.setColor(Color.black);
		g.fillRect(mainDrawingContext.x(), mainDrawingContext.y(), mainDrawingContext.w(), mainDrawingContext.h());
		
		wr.render(gc, g, loadingDrawingContext, textBoxState);
		
	}

	@Override
	public void preRender(StateBasedGame sbg, GameContainer gc, Graphics g) throws SlickException {
		
	}

	@Override
	public void update(StateBasedGame sbg, GameContainer gc, int delta) throws SlickException {

		gc.getInput().clearKeyPressedRecord();
		
		if (startedLoading == false) {
			tiledMap = ApplicationContext.instance().getTiledMap();
			startedLoading = true;
		}

		
		
	}
	
}
