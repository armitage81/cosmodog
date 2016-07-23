package antonafanasjew.cosmodog.collision;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * There can be many ways of preventing a passage. Some of them can be described individually, but some will be generated at the run time.
 * One example are the energy walls that forbid passage based on their infobit costs.
 * This class describes the passage blockers by assigning them one of the defined types and, optionally, one or more parameters.
 */
public class PassageBlockerDescriptor {
	
	private PassageBlockerType passageBlockerType;
	
	private List<Object> parameters = Lists.newArrayList();

	public static PassageBlockerDescriptor fromPassageBlockerType(PassageBlockerType passageBlockerType) {
		return fromPassageBlockerTypeAndParameters(passageBlockerType, Lists.newArrayList());
	}
	
	public static PassageBlockerDescriptor fromPassageBlockerTypeAndParameter(PassageBlockerType passageBlockerType, Object parameterValue) {
		return fromPassageBlockerTypeAndParameters(passageBlockerType, Lists.newArrayList(parameterValue));
	}
	
	public static PassageBlockerDescriptor fromPassageBlockerTypeAndParameters(PassageBlockerType passageBlockerType, List<Object> parameters) {
		PassageBlockerDescriptor retVal = new PassageBlockerDescriptor();
		
		retVal.setPassageBlockerType(passageBlockerType);
		retVal.getParameters().clear();
		retVal.getParameters().addAll(parameters);
		
		return retVal;
	}
	
	public PassageBlockerType getPassageBlockerType() {
		return passageBlockerType;
	}

	public void setPassageBlockerType(PassageBlockerType passageBlockerType) {
		this.passageBlockerType = passageBlockerType;
	}

	public List<Object> getParameters() {
		return parameters;
	}
	
	public String asText() {
		Object[] params = this.parameters.toArray();
		return String.format(this.passageBlockerType.getDescriptionTemplate(), params);
	}
	
}
