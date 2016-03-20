package antonafanasjew.cosmodog.debug;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;

public class PropertyAssignmentExecutor {

	public void execute(Object bean, PropertyAssignment propertyAssignment) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		String propertyAddress = propertyAssignment.getPropertyAddress();
		String propertyValueAsString = propertyAssignment.getPropertyValue();
		
		Object value = null;
		Class<?> type = PropertyUtils.getPropertyType(bean, propertyAddress);
		
		value = toObject(type, propertyValueAsString);
		PropertyUtils.setProperty(bean, propertyAddress, value);
	}

	private Object toObject(Class<?> clazz, String value) {
		if (boolean.class.isAssignableFrom(clazz))
			return Boolean.parseBoolean(value);
		if (byte.class.isAssignableFrom(clazz))
			return Byte.parseByte(value);
		if (short.class.isAssignableFrom(clazz))
			return Short.parseShort(value);
		if (int.class.isAssignableFrom(clazz))
			return Integer.parseInt(value);
		if (long.class.isAssignableFrom(clazz))
			return Long.parseLong(value);
		if (float.class.isAssignableFrom(clazz))
			return Float.parseFloat(value);
		if (double.class.isAssignableFrom(clazz))
			return Double.parseDouble(value);
		return value;
	}

}
