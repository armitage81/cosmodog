package antonafanasjew.cosmodog.model;

/**
 * Represents an entry in the score list. 
 * Can have data for user, score points, date etc.
 */
public class ScoreEntry extends CosmodogModel implements Comparable<ScoreEntry> {

    private static final long serialVersionUID = 675564499573109328L;
    
    private String userName;
	private int score;
    
    public ScoreEntry(String userName, int score) {
    	this.userName = userName;
    	this.score = score;
    }
    
    public int getScore() {
		return score;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + score;
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
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
		if (score != other.score)
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
    
    

}
