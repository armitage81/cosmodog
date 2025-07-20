package antonafanasjew.cosmodog.model;

import java.util.Date;
import java.util.Objects;

/**
 * Represents an entry in the score list. 
 * Can have data for user, score points, date etc.
 */
public class ScoreEntry extends CosmodogModel implements Comparable<ScoreEntry> {

    private static final long serialVersionUID = 675564499573109328L;
    
    private Date date;
	private long score;
	private int starScore;
    
    public ScoreEntry(Date date, long score, int starScore) {
    	this.date = date;
    	this.score = score;
		this.starScore = starScore;
    }
    
    public long getScore() {
		return score;
	}

	public int getStarScore() {
		return starScore;
	}

	public void setStarScore(int starScore) {
		this.starScore = starScore;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + (int) (score ^ (score >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ScoreEntry that = (ScoreEntry) o;
		return score == that.score && starScore == that.starScore && Objects.equals(date, that.date);
	}

	@Override
	public int compareTo(ScoreEntry o) {
		if (o == null) {
			return 1;
		}
		
		if (this.getScore() > o.getScore()) {
			return 1;
		}
		
		if (this.getScore() < o.getScore()) {
			return -1;
		}
		
		return 0;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
    
    

}
