package antonafanasjew.cosmodog.model.menu;

public abstract class MenuElement {

	private String id;
	private Menu parentMenu;
	private MenuLabel label;
	
	public MenuElement(String id, Menu parentMenu, MenuLabel label) {
		this.id = id;
		this.parentMenu = parentMenu;
		this.label = label;
	}

	public Menu getParentMenu() {
		return parentMenu;
	}

	public MenuLabel getLabel() {
		return label;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
