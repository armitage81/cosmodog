package antonafanasjew.cosmodog.resourcehandling.builder.menu;

import java.util.List;
import java.util.Map;

import antonafanasjew.cosmodog.model.menu.Menu;
import antonafanasjew.cosmodog.model.menu.MenuAction;
import antonafanasjew.cosmodog.model.menu.MenuElement;
import antonafanasjew.cosmodog.model.menu.MenuItem;
import antonafanasjew.cosmodog.resourcehandling.AbstractResourceWrapperBuilder;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * Important for this builder: As menus are tree structures, it is required that each parent appears
 * before every of his children in the underlying menu resource. (Prefix notation).
 * The only exceptions are the main menus. They have no parent.
 * 
 * Also all menu elements of a menu have to be in order of their appearance.
 */
public class MenuBuilder extends AbstractResourceWrapperBuilder<MenuElement> {

	private Map<String, MenuAction> menuActionsById;
	
	private Map<String, Menu> builtMenuElements = Maps.newHashMap();
	
	
	private List<Menu> topMenus = Lists.newArrayList();
	

	public MenuBuilder(Map<String, MenuAction> menuActionsById) {
		this.menuActionsById = menuActionsById;
	}
	
	@Override
	protected MenuElement build(String line) {
		
		String[] values = line.split(";");
		
		String id = values[0];
		String label = values[1];
		String parentId = values[2];
		String actionId = values[3];
		
		boolean isLeaf = !actionId.equals("null");
		
		MenuElement retVal;
		
		Menu parentMenu = builtMenuElements.get(parentId);
		
		
		if (isLeaf) {
			
			MenuAction menuAction = menuActionsById.get(actionId);
			retVal = new MenuItem(id, parentMenu, label, menuAction);
			
		} else {
			
			Menu menu = new Menu(id, parentMenu, label);
			
			if (parentMenu == null) {
				topMenus.add(menu);
			}
			
			builtMenuElements.put(id, menu);
			
			retVal = menu;
		}
		
		if (parentMenu != null) {
			parentMenu.getMenuElements().add(retVal);
		}
		
		return retVal;
	}

	@Override
	protected String resourcePath() {
		return "antonafanasjew/cosmodog/menubuilder/menus.txt";
	}

	public List<Menu> getTopMenus() {
		return topMenus;
	}
	
	@Override
	protected void afterBuild() {
		for (Menu menu : topMenus) {
			menu.setInitialized();
		}
	}

}
