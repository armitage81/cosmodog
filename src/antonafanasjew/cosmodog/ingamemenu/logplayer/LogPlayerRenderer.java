package antonafanasjew.cosmodog.ingamemenu.logplayer;

import java.util.List;
import java.util.function.Function;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.globals.FontProvider.FontTypeName;
import antonafanasjew.cosmodog.model.gamelog.GameLog;
import antonafanasjew.cosmodog.model.gamelog.GameLogs;
import antonafanasjew.cosmodog.model.inventory.LogPlayer;
import antonafanasjew.cosmodog.rendering.context.CenteredDrawingContext;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.Renderer;
import antonafanasjew.cosmodog.rendering.renderer.textbook.FontRefToFontTypeMap;
import antonafanasjew.cosmodog.rendering.renderer.textbook.TextPageConstraints;
import antonafanasjew.cosmodog.rendering.renderer.textbook.placement.Book;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.ImageUtils;
import antonafanasjew.cosmodog.util.TextBookRendererUtils;

public class LogPlayerRenderer implements Renderer {

	private static final int MAX_NUMBER_OF_LOGS_IN_SERIES = 20;
	private static final int MAX_NUMBER_OF_SERIES = 20;
	private static final String[] LOG_CATEGORY_LABELS = new String[] {"Tutorials", "Story", "Logs", "Other"};
	private static final int ROWS_FOR_LABEL = 2;
 	
	private static final Function<Short, Short> entryPositionsByIndexes = x -> {
		if (x == 0) return (short)(x + 1 * ROWS_FOR_LABEL);
		if (x >= 1 && x < 4) return (short)(x + 2 * ROWS_FOR_LABEL);
		if (x >= 4 && x < 13) return (short)(x + 3 * ROWS_FOR_LABEL);
		return (short)(x + 4 * ROWS_FOR_LABEL);
	};
	
	@Override
	public void render(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {
		
		DrawingContext inGameMenuContentDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().inGameMenuContentDrawingContext();
		DrawingContext logOverviewDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().logOverviewDrawingContext();
		
		GameLogs gameLogs = ApplicationContext.instance().getGameLogs();
		
		LogPlayer logPlayer = ApplicationContextUtils.getPlayer().getLogPlayer();
		
		LogPlayerInputState logPlayerInputState = (LogPlayerInputState)renderingParameter;
		
		int seriesNumber = logPlayerInputState.getSeriesNumber();
		int logNumber = logPlayerInputState.getLogNumber();
		
		ImageUtils.renderImage(gameContainer, graphics, "ui.ingame.ingamelogs", inGameMenuContentDrawingContext);
		
		int numberOfRows = MAX_NUMBER_OF_SERIES + LOG_CATEGORY_LABELS.length * ROWS_FOR_LABEL;
		
		int noOfSeries = gameLogs.getSeriesNames().size();
		
		
		TileDrawingContext labelHintsDrawingContext = new TileDrawingContext(logOverviewDrawingContext, 1, numberOfRows, 0, 0, 1, 2);
		TileDrawingContext labelStoryDrawingContext = new TileDrawingContext(logOverviewDrawingContext, 1, numberOfRows, 0, 3, 1, 2);
		TileDrawingContext labelLogsDrawingContext = new TileDrawingContext(logOverviewDrawingContext, 1, numberOfRows, 0, 8, 1, 2);
		TileDrawingContext labelMiscDrawingContext = new TileDrawingContext(logOverviewDrawingContext, 1, numberOfRows, 0, 19, 1, 2);
		
		FontRefToFontTypeMap subHeaderFontType = FontRefToFontTypeMap.forOneFontTypeName(FontTypeName.SubHeader);
		
		Book hints = TextPageConstraints.fromDc(labelHintsDrawingContext).textToBook("Hints", subHeaderFontType);
		Book story = TextPageConstraints.fromDc(labelStoryDrawingContext).textToBook("Story", subHeaderFontType);
		Book logs = TextPageConstraints.fromDc(labelLogsDrawingContext).textToBook("Logs", subHeaderFontType);
		Book other = TextPageConstraints.fromDc(labelMiscDrawingContext).textToBook("Other", subHeaderFontType);
		
		
		TextBookRendererUtils.renderLeftAlignedLabel(gameContainer, graphics, hints);
		TextBookRendererUtils.renderLeftAlignedLabel(gameContainer, graphics, story);
		TextBookRendererUtils.renderLeftAlignedLabel(gameContainer, graphics, logs);
		TextBookRendererUtils.renderLeftAlignedLabel(gameContainer, graphics, other);
		
		for (short i = 0; i < MAX_NUMBER_OF_SERIES; i++) {
			
			if (i >= noOfSeries) {
				continue;
			}
			
			int rowNumber = entryPositionsByIndexes.apply(i);
			
			DrawingContext oneSeriesDc = new TileDrawingContext(logOverviewDrawingContext, 1, numberOfRows, 0, rowNumber);
			for (short j = 0; j < MAX_NUMBER_OF_LOGS_IN_SERIES; j++) {
				
				String seriesName = gameLogs.getSeriesNames().get(i);
				List<GameLog> gameLogsForSeries = gameLogs.getGameLogsForSeries(seriesName);
				
				if (j >= gameLogsForSeries.size()) {
					continue;
				}
				
				DrawingContext oneLogDc = new TileDrawingContext(oneSeriesDc, MAX_NUMBER_OF_LOGS_IN_SERIES, 1, j, 0);

				GameLog gameLog = gameLogsForSeries.get(j);

				boolean found;
				if (GameLogs.SPECIFIC_LOGS_SERIES.contains(seriesName)) {
					found = logPlayer.allFoundSpecificLogs(seriesName).contains(j);
				} else {
					found = logPlayer.noOfFoundLogsForSeries(seriesName) > j;
				}
				
				if (i == seriesNumber && j == logNumber) {
					
					graphics.setColor(Color.red);
					graphics.drawRect(oneLogDc.x(), oneLogDc.y(), oneLogDc.w(), oneLogDc.h());
					
					
					if (found) {
						
						String logHeader = gameLog.getHeader();
						
						if (logPlayerInputState.getCurrentLogBook().size() > 1) {
							logHeader = logHeader + " (Page " + (logPlayerInputState.getCurrentLogBook().getCurrentPage() + 1) + "/" + logPlayerInputState.getCurrentLogBook().size() + ")";
						}
						
						String logHint = "Press [ENTER] to turn page.";
						
						DrawingContext titleDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().logPlayerTitleDrawingContext();
						DrawingContext controlsHintDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().logPlayerControlsHintDrawingContext();
						
						FontRefToFontTypeMap fontTypeLogControlsHint = FontRefToFontTypeMap.forOneFontTypeName(FontTypeName.ControlsHint);
						
						Book header = TextPageConstraints.fromDc(titleDrawingContext).textToBook(logHeader, subHeaderFontType);
						TextBookRendererUtils.renderDynamicTextPage(gameContainer, graphics, header);
						
						
						if (logPlayerInputState.getCurrentLogBook().size() > 1) {
							Book hint = TextPageConstraints.fromDc(controlsHintDrawingContext).textToBook(logHint, fontTypeLogControlsHint);
							TextBookRendererUtils.renderDynamicTextPage(gameContainer, graphics, hint);
						}
						
						Book content = logPlayerInputState.getCurrentLogBook();
						TextBookRendererUtils.renderDynamicTextPage(gameContainer, graphics, content);
					}
					
				}
				
				oneLogDc = new CenteredDrawingContext(oneLogDc, 5);
				graphics.setColor(found ? Color.green : Color.gray);
				graphics.fillRect(oneLogDc.x(), oneLogDc.y(), oneLogDc.w(), oneLogDc.h());
				
			}
		}
		
		
	}

}
