package antonafanasjew.cosmodog.rules.factories;

import java.util.Map;

import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.rules.Rule;

public interface RuleFactory {

	Map<String, Rule> buildRules(CosmodogGame cosmodogGame);
	
}
