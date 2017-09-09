package antonafanasjew.cosmodog.ingamemenu.logplayer;

import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.globals.FontType;
import antonafanasjew.cosmodog.model.gamelog.GameLog;
import antonafanasjew.cosmodog.model.gamelog.GameLogs;
import antonafanasjew.cosmodog.model.inventory.LogPlayer;
import antonafanasjew.cosmodog.rendering.context.CenteredDrawingContext;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.SimpleDrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.Renderer;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.ImageUtils;
import antonafanasjew.cosmodog.util.TextBookRendererUtils;

public class LogPlayerRenderer implements Renderer {

	private static final int MAX_NUMBER_OF_LOGS_IN_SERIES = 20;
	private static final int MAX_NUMBER_OF_SERIES = 20;

	@Override
	public void render(GameContainer gameContainer, Graphics graphics, DrawingContext drawingContext, Object renderingParameter) {
		
		GameLogs gameLogs = ApplicationContext.instance().getGameLogs();
		
		LogPlayer logPlayer = ApplicationContextUtils.getPlayer().getLogPlayer();
		
		LogPlayerInputState logPlayerInputState = (LogPlayerInputState)renderingParameter;
		
		int seriesNumber = logPlayerInputState.getSeriesNumber();
		int logNumber = logPlayerInputState.getLogNumber();
		
		ImageUtils.renderImage(gameContainer, graphics, "ui.ingame.ingamelogs", drawingContext);
		
		DrawingContext logOverviewDrawingContext = new SimpleDrawingContext(drawingContext, 12, 13, 397, 406);
		DrawingContext logContentDrawingContext = new SimpleDrawingContext(drawingContext, 441, 13, 759, 406);
		
		logOverviewDrawingContext = new CenteredDrawingContext(logOverviewDrawingContext, 10);
		logContentDrawingContext = new CenteredDrawingContext(logContentDrawingContext, 10);
		
		int noOfSeries = gameLogs.getSeriesNames().size();
		
		for (short i = 0; i < MAX_NUMBER_OF_SERIES; i++) {
			
			if (i >= noOfSeries) {
				continue;
			}
			
			DrawingContext oneSeriesDc = new TileDrawingContext(logOverviewDrawingContext, 1, MAX_NUMBER_OF_SERIES, 0, i);
			for (short j = 0; j < MAX_NUMBER_OF_LOGS_IN_SERIES; j++) {
				
				String seriesName = gameLogs.getSeriesNames().get(i);
				List<GameLog> gameLogsForSeries = gameLogs.getGameLogsForSeries(seriesName);
				
				if (j >= gameLogsForSeries.size()) {
					continue;
				}
				
				DrawingContext oneLogDc = new TileDrawingContext(oneSeriesDc, MAX_NUMBER_OF_LOGS_IN_SERIES, 1, j, 0);

				GameLog gameLog = gameLogsForSeries.get(j);

				boolean found;
				if (GameLogs.SPECIFIC_LOGS_SERIES.equals(seriesName)) {
					found = logPlayer.allFoundSpecificLogs().contains(j);
				} else {
					found = logPlayer.noOfFoundLogsForSeries(seriesName) > j;
				}
				
				if (i == seriesNumber && j == logNumber) {
					
					graphics.setColor(Color.red);
					graphics.drawRect(oneLogDc.x(), oneLogDc.y(), oneLogDc.w(), oneLogDc.h());
					
					
					String logHeader = "";
					String logContent = "";
					String logHint = "";
					
					if (found) {
						logHeader = gameLog.getHeader();
						if (logPlayerInputState.getPages() > 1) {
							logHeader = logHeader + " (Page " + (logPlayerInputState.getCurrentPage() + 1) + "/" + logPlayerInputState.getPages() + ")";
						}
						logContent = gameLog.getLogText();
						logHint = "Press [ENTER] to turn page.";
					}
					
					DrawingContext headerDrawingContext = new TileDrawingContext(logContentDrawingContext, 1, 7, 0, 0);

					DrawingContext titleDrawingContext = new TileDrawingContext(headerDrawingContext, 1, 2, 0, 0);
					DrawingContext controlsHintDrawingContext = new TileDrawingContext(headerDrawingContext, 1, 2, 0, 1);
					
					//DrawingContext textDrawingContext = new TileDrawingContext(logContentDrawingContext, 1, 7, 0, 1, 1, 5);
					DrawingContext absoluteTextDrawingContext = new SimpleDrawingContext(logContentDrawingContext, 0, 55, Constants.LOG_PLAYER_TEXT_WIDTH, Constants.LOG_PLAYER_TEXT_HEIGHT);
					
//					graphics.setColor(Color.red);
//					graphics.drawRect(textDrawingContext.x(), textDrawingContext.y(), textDrawingContext.w(), textDrawingContext.h());
//					graphics.setColor(Color.green);
//					graphics.drawRect(absoluteTextDrawingContext.x(), absoluteTextDrawingContext.y(), absoluteTextDrawingContext.w(), absoluteTextDrawingContext.h());
					
					TextBookRendererUtils.renderTextPage(gameContainer, graphics, titleDrawingContext, logHeader, FontType.GameLogHeader, 0);
					if (logPlayerInputState.getPages() > 1) {
						TextBookRendererUtils.renderTextPage(gameContainer, graphics, controlsHintDrawingContext, logHint, FontType.GameLogControlsHint, 0);
					}
					TextBookRendererUtils.renderTextPage(gameContainer, graphics, absoluteTextDrawingContext, logContent, FontType.GameLog, logPlayerInputState.getCurrentPage());
					
				}
				

				
				

				
				oneLogDc = new CenteredDrawingContext(oneLogDc, 5);
				graphics.setColor(found ? Color.green : Color.gray);
				graphics.fillRect(oneLogDc.x(), oneLogDc.y(), oneLogDc.w(), oneLogDc.h());
				
			}
		}
		
		
	}

}
