package antonafanasjew.cosmodog.model.states;

import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.CosmodogStarter;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.globals.FontType;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.InsightInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.rendering.context.CenteredDrawingContext;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.SimpleDrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.GameFlowUtils;
import antonafanasjew.cosmodog.util.TextBookRendererUtils;

import com.google.common.collect.Lists;

public class OutroState extends BasicGameState {

	private List<String> texts = Lists.newArrayList();
	private int page;
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		
		container.getInput().clearKeyPressedRecord();
		GameFlowUtils.updateScoreList();
		
		page = 0;
		texts.clear();
		
		String outro1 = ApplicationContext.instance().getGameTexts().get("outro1").getLogText();
		String outro2 = ApplicationContext.instance().getGameTexts().get("outro2").getLogText();
		String outro3 = ApplicationContext.instance().getGameTexts().get("outro3").getLogText();
		String outrohint = ApplicationContext.instance().getGameTexts().get("outrohint").getLogText();
		
		int noOfInsights = 0;
		Player player = ApplicationContextUtils.getPlayer();
		Object uncastedInsight = player.getInventory().get(InventoryItemType.INSIGHT);
		if (uncastedInsight != null) {
			InsightInventoryItem insight = (InsightInventoryItem)uncastedInsight;
			noOfInsights = insight.getNumber();
		}
		
		if (noOfInsights < 25) {
			texts.add(outro1);
			texts.add(outrohint);
		} else if (noOfInsights < 32) {
			texts.add(outro1);
			texts.add(outro2);
			texts.add(outrohint);
		} else {
			texts.add(outro1);
			texts.add(outro2);
			texts.add(outro3);
		}
		texts.add("The End");
		
		
	}
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {

	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int n) throws SlickException {
		if (gc.getInput().isKeyPressed(Input.KEY_ENTER)) {
			ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_MENU_SELECT).play();
			if (page < texts.size() - 1) {
				page++;
			} else {
				sbg.enterState(CosmodogStarter.CREDITS_STATE_ID, new FadeOutTransition(), new FadeInTransition());
			}
		}

	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		
		DrawingContext gameContainerDrawingContext = new SimpleDrawingContext(null, 0, 0, gc.getWidth(), gc.getHeight());
		
		gameContainerDrawingContext = new CenteredDrawingContext(gameContainerDrawingContext, 800, 500);
		
		DrawingContext introTextDc = new TileDrawingContext(gameContainerDrawingContext, 1, 7, 0, 0, 1, 6);
		DrawingContext pressEnterTextDc = new TileDrawingContext(gameContainerDrawingContext, 1, 7, 0, 6, 1, 1);
		
		boolean endLabel = page == texts.size() - 1 ? true : false;
		
		if (endLabel) {
			TextBookRendererUtils.renderCenteredLabel(gc, g, introTextDc, texts.get(page), FontType.EndLabel);
		} else {
			TextBookRendererUtils.renderTextPage(gc, g, introTextDc, texts.get(page), FontType.OutroText);
		}
		
		boolean renderBlinkingHint = (System.currentTimeMillis() / 250 % 2) == 1;
		if (renderBlinkingHint) {
			TextBookRendererUtils.renderCenteredLabel(gc, g, pressEnterTextDc, "Press [ENTER]", FontType.PopUpInterface);
		}

	}

	@Override
	public int getID() {
		return CosmodogStarter.OUTRO_STATE_ID;
	}

}
