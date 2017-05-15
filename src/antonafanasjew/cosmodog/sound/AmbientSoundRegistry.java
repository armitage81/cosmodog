package antonafanasjew.cosmodog.sound;

import java.util.HashMap;
import java.util.Set;

import org.newdawn.slick.Sound;

public class AmbientSoundRegistry extends HashMap<String, Sound> {

	private static final long serialVersionUID = -5146481706287722410L;

	@Override
	public Sound put(String key, Sound value) {
		if (!value.playing()) {
			value.play(1f, 0.25f);
		}
		return super.put(key, value);
	}
	
	@Override
	public Sound remove(Object key) {
		Sound sound = get(key);
		if (sound != null) {
			sound.stop();
		}
		return super.remove(key);
	}
	
	@Override
	public void clear() {
		//We need to call remove for each element to enforce stopping of the sounds.
		Set<String> keys = this.keySet();
		while (keys.size() > 0) {
			remove(keys.iterator().next());
			keys = this.keySet();
		}
	}
	
}
