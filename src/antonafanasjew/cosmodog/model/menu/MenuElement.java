package antonafanasjew.cosmodog.model.menu;

public abstract class MenuElement {

	private String id;
	private Menu parentMenu;
	private String label;
	
	public MenuElement(String id, Menu parentMenu, String label) {
		this.id = id;
		this.parentMenu = parentMenu;
		this.label = label;
	}

	public Menu getParentMenu() {
		return parentMenu;
	}

	public String getLabel() {
		return label;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
