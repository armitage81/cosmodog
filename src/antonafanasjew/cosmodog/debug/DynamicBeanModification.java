package antonafanasjew.cosmodog.debug;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class DynamicBeanModification {

	private static final List<String> COMMANDS = new ArrayList<String>();
	
	static {
		COMMANDS.add("field.items[0][0].properties(condition).value := yourmama");
		COMMANDS.add("field.name := yourpapa");
		COMMANDS.add("field.person.age := 34");
	}
	
	public static void main(String[] args) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		
//		Session session = new SessionBuilder().buildSession();
//		
//		
//		for (String command : COMMANDS) {
//			PropertyAssignmentParser parser = new PropertyAssignmentParser();
//			PropertyAssignment propertyAssignment = parser.parseCommand(command);
//			PropertyAssignmentExecutor executor = new PropertyAssignmentExecutor();
//			executor.execute(session, propertyAssignment);
//		}
//		
//		System.out.println(session.getField().getItems()[0][0].getProperties().get("condition").getValue());
//		System.out.println(session.getField().getName());
//		System.out.println(session.getField().getPerson().getAge());
	}

}
