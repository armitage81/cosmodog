package antonafanasjew.cosmodog.model;

public class Mark extends Piece {

	private static final long serialVersionUID = -5005423708726302694L;

	public static final String FUEL_MARK_TYPE = "mark.fuel";
	
	private String markType;
	
	public Mark(String type) {
		this.markType = type;
	}

	public String getMarkType() {
		return markType;
	}

	public void setMarkType(String markType) {
		this.markType = markType;
	}
	
}
