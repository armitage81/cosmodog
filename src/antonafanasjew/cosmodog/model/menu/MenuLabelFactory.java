package antonafanasjew.cosmodog.model.menu;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.newdawn.slick.util.Log;

import antonafanasjew.cosmodog.filesystem.CosmodogGamePersistor;
import antonafanasjew.cosmodog.filesystem.CosmodogPersistenceException;
import antonafanasjew.cosmodog.model.CosmodogGameHeader;
import antonafanasjew.cosmodog.util.PathUtils;

public class MenuLabelFactory {

	public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd' at 'HH:mm");
	
	public static MenuLabel newGameLabel(int number) {
		
		return new MenuLabel() {
			@Override
			public String labelText() {
				
				CosmodogGamePersistor gamePersistor = CosmodogGamePersistor.instance();
				String filePath = PathUtils.gameSaveDir() + "/" + number + ".sav";
				File f = new File(filePath);
				if (f.exists()) {
					try {
						CosmodogGameHeader header = gamePersistor.loadCosmodogGameHeader(filePath);
						return "Delete: " + DATE_FORMAT.format(header.getLastSave());
					} catch (CosmodogPersistenceException e) {
						Log.error("Could not load game");
						return "<ERROR>";
					}
				} else {
					return "New Game";
				}
			}
		};
	}
	
	public static MenuLabel loadGameLabel(int number) {
		return new MenuLabel() {
			@Override
			public String labelText() {
				
				CosmodogGamePersistor gamePersistor = CosmodogGamePersistor.instance();
				String filePath = PathUtils.gameSaveDir() + "/" + number + ".sav";
				File f = new File(filePath);
				if (f.exists()) {
					try {
						CosmodogGameHeader header = gamePersistor.loadCosmodogGameHeader(filePath);
						return DATE_FORMAT.format(header.getLastSave());
					} catch (CosmodogPersistenceException e) {
						Log.error("Could not load game");
						return "<ERROR>";
					}
				} else {
					return "No Save";
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
