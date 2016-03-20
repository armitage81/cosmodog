package antonafanasjew.cosmodog.notifications;

import java.util.ArrayList;
import java.util.Iterator;

public class NotificationList extends ArrayList<Notification> {

	private static final long serialVersionUID = 1725524380280856529L;
	
	public void update(int delta) {

		Iterator<Notification> it = this.iterator();
		
		while (it.hasNext()) {
			Notification n = it.next();
			n.decreaseDuration(delta);
			if (n.getDuration() <= 0 ) {
				it.remove();
			}
		}
		
	}

}
