package antonafanasjew.cosmodog.model.states;

import java.io.IOException;
import java.util.concurrent.Callable;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.util.Log;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.CosmodogStarter;
import antonafanasjew.cosmodog.globals.Features;
import antonafanasjew.cosmodog.globals.Fonts;
import antonafanasjew.cosmodog.rendering.context.CenteredDrawingContext;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.SimpleDrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.TextRenderer;
import antonafanasjew.cosmodog.rendering.renderer.WritingRenderer;
import antonafanasjew.cosmodog.statetransitions.LoadingTransition;
import antonafanasjew.cosmodog.writing.io.NarrativeSequenceReaderImpl;
import antonafanasjew.cosmodog.writing.model.NarrativeSequence;
import antonafanasjew.cosmodog.writing.textbox.WritingTextBox;
import antonafanasjew.cosmodog.writing.textbox.WritingTextBoxContent;
import antonafanasjew.cosmodog.writing.textbox.WritingTextBoxState;

public class GameIntroState  extends BasicGameState {

	private WritingRenderer wr;
	
	private DrawingContext gameContainerDrawingContext;
	private DrawingContext centerContainerDrawingContext;
	private DrawingContext dialogBoxDrawingContext;
	private DrawingContext dialogBoxContentDrawingContext;
	private DrawingContext textDrawingContext;
	private DrawingContext bottomContainerDrawingContext;
	
	private int timePassed;
	
	private WritingTextBoxState introTextBoxState;
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		wr = new WritingRenderer();
		
		gameContainerDrawingContext = new SimpleDrawingContext(null, 0, 0, gc.getWidth(), gc.getHeight());
		centerContainerDrawingContext = new TileDrawingContext(gameContainerDrawingContext, 1, 10, 0, 1, 1, 8);
		dialogBoxDrawingContext = new CenteredDrawingContext(centerContainerDrawingContext, 100);
		dialogBoxContentDrawingContext = new CenteredDrawingContext(dialogBoxDrawingContext, 10);
		textDrawingContext = new CenteredDrawingContext(dialogBoxContentDrawingContext, 10);
		bottomContainerDrawingContext = new TileDrawingContext(gameContainerDrawingContext, 1, 10, 0, 9);
		
		
		NarrativeSequenceReaderImpl r = new NarrativeSequenceReaderImpl();
		NarrativeSequence ns = null;
		try {
			
			ns = r.read(Features.getInstance().featureBoundFunction(Features.FEATURE_STORY, new Callable<String>() {

				@Override
				public String call() throws Exception {
					return "intro.0001.opening.html";
				}
				
			}, "intro.0001.opening.nostory.html"));
			
		} catch (IOException e) {
			Log.error("Error while initializing GameIntroState. Could not read the narrative sequence. " + e.getMessage(), e);
		}
		
		WritingTextBox textBox = new WritingTextBox(textDrawingContext.w(), textDrawingContext.h(), 0, 5, 20, 30);
		WritingTextBoxContent textBoxContent = new WritingTextBoxContent(textBox, ns.getTextBlocks());
		introTextBoxState = new WritingTextBoxState(textBoxContent);
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		timePassed = 0;
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		
		g.setColor(Color.white);
		g.fillRoundRect(dialogBoxDrawingContext.x(), dialogBoxDrawingContext.y(), dialogBoxDrawingContext.w(), dialogBoxDrawingContext.h(), 5);
		
		g.setColor(new Color(0, 0, 205));
		g.fillRect(dialogBoxContentDrawingContext.x(), dialogBoxContentDrawingContext.y(), dialogBoxContentDrawingContext.w(), dialogBoxContentDrawingContext.h());
		DrawingContext d = dialogBoxContentDrawingContext; //Just as a shortcut variable
		
		if (introTextBoxState.completeBoxDisplayed()) {
			Animation animation;
			if (introTextBoxState.hasMoreBoxes()) {
				animation = ApplicationContext.instance().getAnimations().get("nextDialogBox");
			} else {
				animation = ApplicationContext.instance().getAnimations().get("closeDialogBox");
			}
			int size = 48;
			animation.draw(d.x() + d.w() - size, d.y() + d.h() - size, size, size);
		}
		wr.render(gc, g, textDrawingContext, introTextBoxState);				
		
		TextRenderer tr = new TextRenderer(Fonts.DEFAULT_FONT, true);
		tr.render(gc, g, bottomContainerDrawingContext, "Press [Enter]");
		
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		
		if (timePassed < 300 * 1000) {
			timePassed += delta;
		}
		
		introTextBoxState.update(delta);

		if (gc.getInput().isKeyPressed(Input.KEY_ENTER)) {
			introTextBoxState.displayCompleteBoxOrSwitchToNextBoxOrFinish();
			if (introTextBoxState.isFinish()) {
				sbg.enterState(CosmodogStarter.GAME_STATE_ID, new LoadingTransition(), new FadeInTransition());
			}
		}
		
	}

	@Override
	public int getID() {
		return CosmodogStarter.GAME_INTRO_STATE_ID;
	}

}
