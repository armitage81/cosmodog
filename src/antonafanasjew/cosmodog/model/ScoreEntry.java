package antonafanasjew.cosmodog.model;

import java.util.Date;

/**
 * Represents an entry in the score list. 
 * Can have data for user, score points, date etc.
 */
public class ScoreEntry extends CosmodogModel implements Comparable<ScoreEntry> {

    private static final long serialVersionUID = 675564499573109328L;
    
    private Date date;
	private long score;
    
    public ScoreEntry(Date date, long score) {
    	this.date = date;
    	this.score = score;
    }
    
    public long getScore() {
		return score;
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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ScoreEntry other = (ScoreEntry) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (score != other.score)
			return false;
		return true;
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
