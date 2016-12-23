package antonafanasjew.cosmodog.domains;

public enum QuadrandType {

	
	NW ("North West"), 
	NE ("North East"), 
	SW ("South West"), 
	SE ("South East");

	private String representation;

	private QuadrandType(String representation) {
		this.representation = representation;
	}

	public String getRepresentation() {
		return representation;
	}

}
