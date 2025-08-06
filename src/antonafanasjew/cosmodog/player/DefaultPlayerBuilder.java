package antonafanasjew.cosmodog.player;

import antonafanasjew.cosmodog.model.actors.Player;

public class DefaultPlayerBuilder extends AbstractPlayerBuilder {

	@Override
	protected void updatePlayer(Player player) {
		
		player.getLogPlayer().addLogToSeries("hints");
		player.getLogPlayer().addLogToSeries("hints");
		player.getLogPlayer().addLogToSeries("hints");
		player.getLogPlayer().addLogToSeries("hints");
		player.getLogPlayer().addLogToSeries("hints");
		player.getLogPlayer().addLogToSeries("hints");
		player.getLogPlayer().addLogToSeries("hints");
		player.getLogPlayer().addLogToSeries("hints");
		player.getLogPlayer().addLogToSeries("hints");
		player.getLogPlayer().addLogToSeries("hints");
		player.getLogPlayer().addLogToSeries("hints");
		player.getLogPlayer().addLogToSeries("hints");
		player.setLife(1);

	}

}
