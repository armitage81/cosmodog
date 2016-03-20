package antonafanasjew.cosmodog.resourcewrappers;


public class CollectibleItemResourceWrapper {

	private String collectibleType;
	private String animationIdOnMap;
	private String animationIdInInventory;
	private String descriptionWhenFound;
	private String commentWhenFound;
	private boolean active;
	private boolean countable;
	private boolean removeOtherInstancesOnMap;
	
	public String getCollectibleType() {
		return collectibleType;
	}
	public void setCollectibleType(String collectibleType) {
		this.collectibleType = collectibleType;
	}
	public String getAnimationIdOnMap() {
		return animationIdOnMap;
	}
	public void setAnimationIdOnMap(String animationIdOnMap) {
		this.animationIdOnMap = animationIdOnMap;
	}
	public String getAnimationIdInInventory() {
		return animationIdInInventory;
	}
	public void setAnimationIdInInventory(String animationIdInInventory) {
		this.animationIdInInventory = animationIdInInventory;
	}
	public String getDescriptionWhenFound() {
		return descriptionWhenFound;
	}
	public void setDescriptionWhenFound(String descriptionWhenFound) {
		this.descriptionWhenFound = descriptionWhenFound;
	}
	public String getCommentWhenFound() {
		return commentWhenFound;
	}
	public void setCommentWhenFound(String commentWhenFound) {
		this.commentWhenFound = commentWhenFound;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public boolean isCountable() {
		return countable;
	}
	public void setCountable(boolean countable) {
		this.countable = countable;
	}
	public boolean isRemoveOtherInstancesOnMap() {
		return removeOtherInstancesOnMap;
	}
	public void setRemoveOtherInstancesOnMap(boolean removeOtherInstancesOnMap) {
		this.removeOtherInstancesOnMap = removeOtherInstancesOnMap;
	}
	
	
	
	
}
