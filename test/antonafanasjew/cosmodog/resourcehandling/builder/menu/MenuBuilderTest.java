package antonafanasjew.cosmodog.resourcehandling.builder.menu;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.model.menu.Menu;
import antonafanasjew.cosmodog.model.menu.MenuAction;
import antonafanasjew.cosmodog.model.menu.MenuLabel;

import com.google.common.collect.Maps;

public class MenuBuilderTest {

	private Map<String, MenuAction> menuActions = Maps.newHashMap();
	private Map<String, MenuLabel> menuLabels = Maps.newHashMap();
	
	public MenuBuilderTest() {
		for (String s : new String[]{"loadGameMenuAction", "showRecordsGameMenuAction", "quitGameMenuAction"}) {
			menuActions.put(s, new MenuAction() {

				@Override
				public void execute(StateBasedGame sbg) {
					System.out.println(s);
				}
				
			});
		}
		
		for (String s : new String[]{"loadSavedGame", "showRecords", "quit"}) {
			menuLabels.put(s, new MenuLabel() {

				@Override
				public String labelText() {
					return s;
				}
				
			});
		}
	}
	
	@Test
	public void testMenuBuilder() {
		MenuBuilder b = new MenuBuilder(menuActions, menuLabels);
		b.build();
		List<Menu> menus = b.getTopMenus();
		menus.size();
	}
	
	
}
