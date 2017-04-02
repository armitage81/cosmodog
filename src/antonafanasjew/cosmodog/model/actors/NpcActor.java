package antonafanasjew.cosmodog.model.actors;

import antonafanasjew.cosmodog.domains.ChaussieType;
import antonafanasjew.cosmodog.model.inventory.InventoryItem;

/**
 * Represents all non player characters, be it friendly or hostile
 */
public class NpcActor extends Actor {
	
	private static final long serialVersionUID = -7670644820503474045L;
	
	private float speedFactor;
	
	private String homeRegionName;
	
	private ChaussieType chaussieType;
	
	private int timeBudgetOverhead;
	
	private InventoryItem inventoryItem;
	
	/**
	 * Returns the speed factor as related to the player.
	 * As the player is the time trigger, everything else has to use him as reference. 
	 * 
	 * speedFactor == 2 would mean that the NPC will cross two tiles after the player
	 * has crossed one tile of the same type under the same conditions.
	 * 
	 * @return The speed factor related to the player. 
	 */
	public float getSpeedFactor() {
		return speedFactor;
	}

	/**
	 * Sets the speed factor as related to the player. 0.5 means half so fast as the player, 2.0 means double as fast as the player.
	 * @param speedFactor The speed factor. Recommended values are 0,0.25,0.5,1,2,4,8
	 */
	public void setSpeedFactor(float speedFactor) {
		this.speedFactor = speedFactor;
	}
	
	public ChaussieType getChaussieType() {
		return chaussieType;
	}

	public void setChaussieType(ChaussieType chaussieType) {
		this.chaussieType = chaussieType;
	}

	public String getHomeRegionName() {
		return homeRegionName;
	}

	public void setHomeRegionName(String homeRegionName) {
		this.homeRegionName = homeRegionName;
	}

	/**
	 * Returns the collected time budget overhead.
	 * The overhead is being collected if the NPC has some time budget
	 * but is not able to execute a movement because it requires more time.
	 * In this case, the remaining time budget will be stored as overhead and
	 * can be used next turn.
	 * @return Time budget overhead.
	 */
	public int getTimeBudgetOverhead() {
		return timeBudgetOverhead;
	}

	public void setTimeBudgetOverhead(int timeBudgetOverhead) {
		this.timeBudgetOverhead = timeBudgetOverhead;
	}

	public InventoryItem getInventoryItem() {
		return inventoryItem;
	}

	public void setInventoryItem(InventoryItem inventoryItem) {
		this.inventoryItem = inventoryItem;
	}

}
