package antonafanasjew.cosmodog.ingamemenu.logplayer;

import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.globals.FontType;
import antonafanasjew.cosmodog.model.gamelog.GameLog;
import antonafanasjew.cosmodog.model.gamelog.GameLogs;
import antonafanasjew.cosmodog.model.inventory.LogPlayer;
import antonafanasjew.cosmodog.rendering.context.CenteredDrawingContext;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.Renderer;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
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
		
		drawingContext = new CenteredDrawingContext(drawingContext, 5);
		
		DrawingContext logOverviewDrawingContext = new TileDrawingContext(drawingContext, 3, 1, 0, 0);
		DrawingContext logContentDrawingContext = new TileDrawingContext(drawingContext, 3, 1, 1, 0, 2, 1);
		
		graphics.drawRoundRect(logOverviewDrawingContext.x(), logOverviewDrawingContext.y(), logOverviewDrawingContext.w(), logOverviewDrawingContext.h(), 5);
		graphics.drawRoundRect(logContentDrawingContext.x(), logContentDrawingContext.y(), logContentDrawingContext.w(), logContentDrawingContext.h(), 5);
	
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
					
					
					String logHeader = "Not found.";
					String logContent = "";
					
					if (found) {
						logHeader = gameLog.getHeader();
						logContent = gameLog.getLogText();
					}
					
					DrawingContext authorDrawingContext = new TileDrawingContext(logContentDrawingContext, 1, 7, 0, 0);
					DrawingContext textDrawingContext = new TileDrawingContext(logContentDrawingContext, 1, 7, 0, 1, 1, 5);
					DrawingContext pressEnterDrawingContext = new TileDrawingContext(logContentDrawingContext, 1, 7, 0, 6, 1, 1);
					
					authorDrawingContext = new CenteredDrawingContext(authorDrawingContext, 15);
					textDrawingContext = new CenteredDrawingContext(textDrawingContext, 15); 
					
					TextBookRendererUtils.renderTextPage(gameContainer, graphics, authorDrawingContext, logHeader, FontType.GameLogHeader);
					TextBookRendererUtils.renderTextPage(gameContainer, graphics, textDrawingContext, logContent, FontType.GameLog);
					
				}
				

				
				

				
				oneLogDc = new CenteredDrawingContext(oneLogDc, 5);
				graphics.setColor(found ? Color.green : Color.gray);
				graphics.fillRect(oneLogDc.x(), oneLogDc.y(), oneLogDc.w(), oneLogDc.h());
				
			}
		}
		
		
	}

}
