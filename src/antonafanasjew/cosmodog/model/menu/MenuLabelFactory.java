package antonafanasjew.cosmodog.model.menu;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import org.newdawn.slick.util.Log;

import antonafanasjew.cosmodog.filesystem.CosmodogGamePersistor;
import antonafanasjew.cosmodog.filesystem.CosmodogPersistenceException;
import antonafanasjew.cosmodog.model.CosmodogGameHeader;

public class MenuLabelFactory {

	public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd' at 'HH:mm");
	
	public static MenuLabel newGameLabel(int number) {
		
		return new MenuLabel() {
			@Override
			public String labelText() {

				Cosmodog cosmodog = ApplicationContextUtils.getCosmodog();
				CosmodogGamePersistor gamePersistor = cosmodog.getGamePersistor();
				try {
					boolean headerExists = gamePersistor.cosmodogGameHeaderExists(number);
					if (headerExists) {
						CosmodogGameHeader header = gamePersistor.loadCosmodogGameHeader(number);
						return "Overwrite: " + DATE_FORMAT.format(header.getLastSave());
					} else {
						return "New Game";
					}
				} catch (CosmodogPersistenceException e) {
					Log.error("Could not load game");
					return "<ERROR>";
				}
			}
		};
	}
	
	public static MenuLabel loadGameLabel(int number) {
		return new MenuLabel() {
			@Override
			public String labelText() {
				Cosmodog cosmodog = ApplicationContextUtils.getCosmodog();
				CosmodogGamePersistor gamePersistor = cosmodog.getGamePersistor();
				try {
					boolean headerExists = gamePersistor.cosmodogGameHeaderExists(number);
					if (headerExists) {
						CosmodogGameHeader header = gamePersistor.loadCosmodogGameHeader(number);
						return DATE_FORMAT.format(header.getLastSave());
					} else {
						return "No Data";
					}
				} catch (CosmodogPersistenceException e) {
					Log.error("Could not load game");
					return "<ERROR>";
				}

			}
		};
	}
	
	public static MenuLabel fromText(String text) {
		return new MenuLabel() {
			@Override
			public String labelText() {
				return text;
			}
		};
	}
	
}
