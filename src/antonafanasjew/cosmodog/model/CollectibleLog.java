package antonafanasjew.cosmodog.model;



public class CollectibleLog extends Collectible {

	private static final long serialVersionUID = 8589176730233780875L;
	
	//Defines the series for the log if it is part of the series or shows 'unsorted' for specific logs.
	private String logSeries;
	//Defines the id of the specific log in the 'unsorted' category. Has no meaning for other categories.
	private String logId;

	public CollectibleLog(String logSeries, String logId) {
		super(Collectible.CollectibleType.LOG);
		this.logSeries = logSeries;
		this.logId = logId;
	}

	public String getLogSeries() {
		return logSeries;
	}
	
	public String getLogId() {
		return logId;
	}
	
}
