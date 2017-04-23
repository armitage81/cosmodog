package antonafanasjew.cosmodog.resourcehandling.gamelogs;

import java.io.IOException;

import antonafanasjew.cosmodog.model.gamelog.GameLog;
import antonafanasjew.cosmodog.model.gamelog.GameLogs;

public interface GameLogBuilder {

	GameLog buildGameLog(String resourcePath) throws IOException;
	
	GameLogs buildGameLogs(String gameLogResourceFolderPath) throws IOException;
	
}
