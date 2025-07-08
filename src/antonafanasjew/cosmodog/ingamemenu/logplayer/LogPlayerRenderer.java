package antonafanasjew.cosmodog.ingamemenu.logplayer;

import java.util.List;
import java.util.function.Function;

import antonafanasjew.cosmodog.rendering.renderer.AbstractRenderer;
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

/**
 * Represents the renderer that renders the log menu, including all logs (found and not found) and the
 * contents of the selected log.
 * <p>
 * What to do if a new log is added to the game:
 * <p>
 * It is important to check that the log is not exceeding the maximal number of logs in a series
 * and also that the series (if new) is not exceeding the maximal number of series.
 * Also, if a new series is added, check that the indexes of labels and other series are updated.
 * If a new category of series is added, then check that it is added everywhere like other categories are.
 */
public class LogPlayerRenderer extends AbstractRenderer {

	/**
	 * The maximum number of logs that can be displayed in one row.
	 */
	private static final int MAX_NUMBER_OF_LOGS_IN_SERIES = 20;

	/**
	 * There can be maximally that many log series.
	 */
	private static final int MAX_NUMBER_OF_SERIES = 20;
	private static final int LOG_CATEGORY_LABELS_SIZE = 4;
	private static final int ROWS_FOR_LABEL = 2;


	private static final Function<Short, Short> entryPositionsByIndexes = x -> {
		if (x == 0) return (short)(x + 1 * ROWS_FOR_LABEL);
		if (x >= 1 && x < 4) return (short)(x + 2 * ROWS_FOR_LABEL);
		if (x >= 4 && x < 13) return (short)(x + 3 * ROWS_FOR_LABEL);
		return (short)(x + 4 * ROWS_FOR_LABEL);
	};
	
	@Override
	public void renderInternally(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {

		//Drawing context for the whole game menu frame.
		DrawingContext inGameMenuContentDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().inGameMenuContentDrawingContext();

		//Drawing context for the part of the game menu frame that shows the log overview (left hand).
		DrawingContext logOverviewDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().logOverviewDrawingContext();

		//Contains ALL game logs (not only the found ones).
		GameLogs gameLogs = ApplicationContext.instance().getGameLogs();

		//The log player that contains all collected logs by the player.
		LogPlayer logPlayer = ApplicationContextUtils.getPlayer().getLogPlayer();

		//The player can scroll through the log list and turn pages. The state of their input is stored in this object.
		LogPlayerInputState logPlayerInputState = (LogPlayerInputState)renderingParameter;

		//Currently selected series of logs (row).
		int selectedSeriesNumber = logPlayerInputState.getSeriesNumber();

		//Currently selected log in a series (column).
		int selectedLogNumber = logPlayerInputState.getLogNumber();

		//Draws the bluish frame around the log overview. It contains two sections: the log overview and the log content.
		ImageUtils.renderImage(gameContainer, graphics, "ui.ingame.ingamelogs", inGameMenuContentDrawingContext);

		//The log overview contains blocks for each log series, but also labels for log categories ("Hints", "Story", "Logs", "Other").
		//A label can use a block of 2 or more rows.
		//Example: There are 8 series, 4 categories, and 2 rows per label. This means that there are 16 rows in total.
		int numberOfRows = MAX_NUMBER_OF_SERIES + LOG_CATEGORY_LABELS_SIZE * ROWS_FOR_LABEL;

		//The number of log series, regardless of categories.
		int noOfSeries = gameLogs.getSeriesNames().size();
		
		//Drawing contexts for log category labels. Their vertical position depends on how many series are in each category.
		//Example: Category "Hints" has 1 series, "Story" comes afterward. So the "Hints" label starts at row 0 and takes two rows.
		//Then, the series takes one row. "Story" label starts at row 3 (and takes two rows) and so on.
		TileDrawingContext labelHintsDrawingContext = new TileDrawingContext(logOverviewDrawingContext, 1, numberOfRows, 0, 0, 1, ROWS_FOR_LABEL);
		TileDrawingContext labelStoryDrawingContext = new TileDrawingContext(logOverviewDrawingContext, 1, numberOfRows, 0, 3, 1, ROWS_FOR_LABEL);
		TileDrawingContext labelLogsDrawingContext = new TileDrawingContext(logOverviewDrawingContext, 1, numberOfRows, 0, 8, 1, ROWS_FOR_LABEL);
		TileDrawingContext labelMiscDrawingContext = new TileDrawingContext(logOverviewDrawingContext, 1, numberOfRows, 0, 19, 1, ROWS_FOR_LABEL);

		//Labels are rendered within their own drawing contexts.
		FontRefToFontTypeMap subHeaderFontType = FontRefToFontTypeMap.forOneFontTypeName(FontTypeName.SubHeader);
		Book hints = TextPageConstraints.fromDc(labelHintsDrawingContext).textToBook("Hints", subHeaderFontType);
		Book story = TextPageConstraints.fromDc(labelStoryDrawingContext).textToBook("Story", subHeaderFontType);
		Book logs = TextPageConstraints.fromDc(labelLogsDrawingContext).textToBook("Logs", subHeaderFontType);
		Book other = TextPageConstraints.fromDc(labelMiscDrawingContext).textToBook("Other", subHeaderFontType);
		TextBookRendererUtils.renderLeftAlignedLabel(gameContainer, graphics, hints);
		TextBookRendererUtils.renderLeftAlignedLabel(gameContainer, graphics, story);
		TextBookRendererUtils.renderLeftAlignedLabel(gameContainer, graphics, logs);
		TextBookRendererUtils.renderLeftAlignedLabel(gameContainer, graphics, other);

		//We iterate through maximal (that is potential) number of series while there could be less of them in reality.
		for (short i = 0; i < MAX_NUMBER_OF_SERIES; i++) {

			//If there are less series than the maximal number, we skip the rest of the loop.
			if (i >= noOfSeries) {
				continue;
			}

			//Series 0 is not drawn at row 0, because there is a category label taking some rows first.
			//Series 5 could have multiple other labels above it.
			//That's why we have to calculate the actual row number for each series.
			int rowNumber = entryPositionsByIndexes.apply(i);

			//numberOfRows includes rows for all labels and all series.
			//Each series will be drawn in the row rowNumber.
			DrawingContext oneSeriesDc = new TileDrawingContext(logOverviewDrawingContext, 1, numberOfRows, 0, rowNumber);

			//Each series of logs can maximally contain MAX_NUMBER_OF_LOGS_IN_SERIES logs.
			//Most of them will contain less logs, though.
			for (short j = 0; j < MAX_NUMBER_OF_LOGS_IN_SERIES; j++) {
				
				String seriesName = gameLogs.getSeriesNames().get(i);
				List<GameLog> gameLogsForSeries = gameLogs.getGameLogsForSeries(seriesName);

				//If the series does not contain as many logs as MAX_NUMBER_OF_LOGS_IN_SERIES, we skip the rest of the loop.
				if (j >= gameLogsForSeries.size()) {
					continue;
				}

				//Each log in the series will be drawn in its own context as a horizontal slice of the row.
				DrawingContext oneLogDc = new TileDrawingContext(oneSeriesDc, MAX_NUMBER_OF_LOGS_IN_SERIES, 1, j, 0);

				GameLog gameLog = gameLogsForSeries.get(j);

				//This flag defines whether a log has been found by the player, or not.
				//There are two types of logs: specific logs and series logs. Specific logs can be found independently
				//while series logs are found in an order regardless of the collectible on the map
				boolean found;
				if (GameLogs.SPECIFIC_LOGS_SERIES.contains(seriesName)) {
					found = logPlayer.allFoundSpecificLogs(seriesName).contains(j);
				} else {
					found = logPlayer.noOfFoundLogsForSeries(seriesName) > j;
				}

				//The selected log will be emphasized by a kind of cursor.
				if (i == selectedSeriesNumber && j == selectedLogNumber) {

					//Selected log is drawn with a red frame whether it is found or not.
					graphics.setColor(Color.red);
					graphics.drawRect(oneLogDc.x(), oneLogDc.y(), oneLogDc.w(), oneLogDc.h());

					//Selected and found log will be presented on the right half of the menu screen.
					if (found) {

						//First, log header is printed. If the log has more than one page, the page number is added.
						String logHeader = gameLog.getHeader();
						if (logPlayerInputState.getCurrentLogBook().size() > 1) {
							logHeader = logHeader + " (Page " + (logPlayerInputState.getCurrentLogBook().getCurrentPage() + 1) + "/" + logPlayerInputState.getCurrentLogBook().size() + ")";
						}

						//The log frame has three elements: header, content, and a controls hint.
						//The elements are arranged vertically while the content is in the middle and takes the most place.
						String logHint = "Press [ENTER] to turn page.";
						
						DrawingContext titleDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().logPlayerTitleDrawingContext();
						DrawingContext controlsHintDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().logPlayerControlsHintDrawingContext();
						
						FontRefToFontTypeMap fontTypeLogControlsHint = FontRefToFontTypeMap.forOneFontTypeName(FontTypeName.ControlsHint);
						
						Book header = TextPageConstraints.fromDc(titleDrawingContext).textToBook(logHeader, subHeaderFontType);
						TextBookRendererUtils.renderDynamicTextPage(gameContainer, graphics, header);
						
						//The controls hint is only shown if the log has more than one page.
						if (logPlayerInputState.getCurrentLogBook().size() > 1) {
							Book hint = TextPageConstraints.fromDc(controlsHintDrawingContext).textToBook(logHint, fontTypeLogControlsHint);
							TextBookRendererUtils.renderDynamicTextPage(gameContainer, graphics, hint);
						}
						
						Book content = logPlayerInputState.getCurrentLogBook();
						TextBookRendererUtils.renderDynamicTextPage(gameContainer, graphics, content);
					}
				}

				//If the log is not selected it is just drawn green or gray depending on whether it has been found or not.
				oneLogDc = new CenteredDrawingContext(oneLogDc, 5);
				graphics.setColor(found ? Color.green : Color.gray);
				graphics.fillRect(oneLogDc.x(), oneLogDc.y(), oneLogDc.w(), oneLogDc.h());
				
			}
		}
		
		
	}

}
