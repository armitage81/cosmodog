package antonafanasjew.cosmodog.model;

import java.util.Date;

import antonafanasjew.cosmodog.GameProgress;

public class CosmodogGameHeader extends CosmodogModel {

	private static final long serialVersionUID = -2582698521135435773L;


	private String gameName;
	private long score;
	private Date lastSave;

	public CosmodogGameHeader(CosmodogGame cosmodogGame, Date date) {
		this.gameName = cosmodogGame.getGameName();
		GameProgress gameProgress = cosmodogGame.getPlayer().getGameProgress();
		this.score = gameProgress.getGameScore();
		this.lastSave = date;
	}
	
	public String getGameName() {
		return gameName;
	}
	
	public long getScore() {
		return score;
	}
	
	public Date getLastSave() {
		return lastSave;
	}

}
