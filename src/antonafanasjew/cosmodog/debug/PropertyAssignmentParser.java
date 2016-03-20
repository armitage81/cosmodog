package antonafanasjew.cosmodog.debug;

import java.text.ParseException;


public class PropertyAssignmentParser {


	public PropertyAssignment parseCommand(String command) throws ParseException {
		String[] parts = command.split(":=");
		if (parts == null || parts.length != 2) {
			throw new ParseException("No ':=' operator found. The command pattern should be in form <propertyAddress> := <primitiveLiteral>", 0);
		}
		PropertyAssignment propertyAssignment = new PropertyAssignment();
		propertyAssignment.setPropertyAddress(parts[0].trim());
		propertyAssignment.setPropertyValue(parts[1].trim());
		return propertyAssignment;
		
	}
	
	public static void main(String[] args) throws ParseException {
		PropertyAssignmentParser parser = new PropertyAssignmentParser();
		parser.parseCommand("field.items[1][1].properties(condition).value := yourmama");
	}
	
}
