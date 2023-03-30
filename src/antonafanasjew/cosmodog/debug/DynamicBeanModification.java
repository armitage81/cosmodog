package antonafanasjew.cosmodog.debug;

import java.util.ArrayList;
import java.util.List;

public class DynamicBeanModification {

	private static final List<String> COMMANDS = new ArrayList<String>();
	
	static {
		COMMANDS.add("field.items[0][0].properties(condition).value := yourmama");
		COMMANDS.add("field.name := yourpapa");
		COMMANDS.add("field.person.age := 34");
	}
	
}
