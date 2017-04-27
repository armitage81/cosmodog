package antonafanasjew.cosmodog.resourcehandling.gamelogs;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import antonafanasjew.cosmodog.model.gamelog.GameLog;
import antonafanasjew.cosmodog.model.gamelog.GameLogs;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class GameLogBuilderImpl implements GameLogBuilder {

	
	@Override
	public GameLog buildGameLog(String resourcePath) throws IOException {
		File file = new File(resourcePath);
		File folder = file.getParentFile();
		String content = Files.toString(file, Charsets.UTF_8);
		
		String[] parts = content.split("[-]{10,}");
		
		String header = parts[0].trim();
		String text = parts[1].trim();
	
		/*
		 * 1 line break => ignore
		 * 2 line breaks => line break in game log
		 * 3 line breaks => paragraph in game log
		 * 4 line breaks => new page in game log
		 */
		
		text = text.replaceAll("\r\n\r\n\r\n\r\n", " <div> ");
		text = text.replaceAll("\n\n\n\n", " <div> ");
		
		text = text.replaceAll("\r\n\r\n\r\n", " <p> ");
		text = text.replaceAll("\n\n\n", " <p> ");
		
		text = text.replaceAll("\r\n\r\n", " <br> ");
		text = text.replaceAll("\n\n", " <br> ");
		
		text = text.replaceAll("\r\n", " ");
		text = text.replaceAll("\n", " ");
		
		GameLog retVal = GameLog.instance(folder.getName(), file.getName(), header, text);
		return retVal;
		
	}

	@Override
	public GameLogs buildGameLogs(String gameLogResourceFolderPath) throws IOException {
		
		GameLogs gameLogs = new GameLogs();
		
		File gameLogResourceFolder = new File(gameLogResourceFolderPath);
		
		if (!gameLogResourceFolder.exists()) {
			throw new IOException("Path " + gameLogResourceFolderPath + " does not exist.");
		}
		
		if (!gameLogResourceFolder.isDirectory()) {
			throw new IOException("Path " + gameLogResourceFolderPath + " does not represent a directory.");
		}
		
		File[] gameLogCategoryDirectories = gameLogResourceFolder.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File file) {
				return file.isDirectory();
			}
		});
		
		for (File gameLogCategoryDirectory : gameLogCategoryDirectories) {
			File[] gameLogTextFilesForCategory = gameLogCategoryDirectory.listFiles(new FileFilter() {
				
				@Override
				public boolean accept(File file) {
					return file.isFile();
				}
			});
			
			for (File gameLogTextFile : gameLogTextFilesForCategory) {
				GameLog gameLog = buildGameLog(gameLogTextFile.getAbsolutePath());
				gameLogs.addGameLog(gameLog);
			}
			
		}
		
		return gameLogs;
		
	}

}
