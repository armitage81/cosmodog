package antonafanasjew.cosmodog.model.dynamicpieces;

import antonafanasjew.cosmodog.model.MoveableDynamicPiece;

public class Block extends MoveableDynamicPiece {

	private static final long serialVersionUID = 8490611107485532770L;

	public static final String STIL_BLOCK = "block";
	public static final String STIL_CONTAINER = "container";
	public static final String STIL_ICE = "ice";
	public static final String STIL_PLANT = "plant";
	
	public static final short STATE_DEFAULT = 0;
	
	private static final short NUMBER_OF_SHAPES = 3;
	private static short shapeLoopCounter = 0;
	
	private short state = STATE_DEFAULT;
	private short shapeNumber = (short)((shapeLoopCounter++) % NUMBER_OF_SHAPES);
	
	private String stil;
	
	public short getState() {
		return state;
	}
	
	public static Block create(int x, int y) {
		Block block = new Block();
		block.setPositionX(x);
		block.setPositionY(y);
		return block;
	}
	
	public String animationSuffixFromState() {
		return String.valueOf(state);
	}

	public short getShapeNumber() {
		return shapeNumber;
	}

	@Override
	public void interact() {
		
	}
	
	public String getStil() {
		return stil;
	}
	
	public void setStil(String stil) {
		this.stil = stil;
	}
}
