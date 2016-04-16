package antonafanasjew.cosmodog.model.menu;

import java.util.List;

import com.google.common.collect.Lists;

public class Menu extends MenuElement {
	
	private List<MenuElement> menuElements = Lists.newArrayList();
	
	private MenuElement selectedMenuElement;

	public Menu(String id, Menu parentMenu, String label) {
		super(id, parentMenu, label);
	}
	
	public List<MenuElement> getMenuElements() {
		return menuElements;
	}

	public void setMenuElements(List<MenuElement> menuElements) {
		this.menuElements = menuElements;
	}

	public MenuElement getSelectedMenuElement() {
		return selectedMenuElement;
	}
	
	public void setInitialized() {
		this.selectedMenuElement = menuElements.get(0);
		for (MenuElement child : menuElements) {
			if (child instanceof Menu) {
				((Menu)child).setInitialized();
			}
		}
	}
	
	public boolean isInitialized() {
		return this.selectedMenuElement != null;
	}
	
	public void selectNext() {
		
		if (isInitialized() == false) {
			setInitialized();
		} else {
			int newIndex = (menuElements.indexOf(selectedMenuElement) + 1) % menuElements.size();
			selectedMenuElement = menuElements.get(newIndex);
		}
		
	}
	
	public void selectPrevious() {
		
		if (isInitialized() == false) {
			setInitialized();
		} else {
			int newIndex = (menuElements.indexOf(selectedMenuElement) - 1);
			newIndex = newIndex < 0 ? menuElements.size() - 1 : newIndex;
			selectedMenuElement = menuElements.get(newIndex);
		}
		
	}
	
}
