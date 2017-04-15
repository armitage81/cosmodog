package antonafanasjew.cosmodog.model.menu;


public class MenuItem extends MenuElement {

	private MenuAction menuAction;

	public MenuItem(String id, Menu parentMenu, MenuLabel label, MenuAction menuAction) {
		super(id, parentMenu, label);
		this.menuAction = menuAction;
	}
	
	public MenuAction getMenuAction() {
		return menuAction;
	}

}
