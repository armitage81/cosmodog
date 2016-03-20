package antonafanasjew.cosmodog.notifications;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;

public class NotificationQueue implements Serializable {

	private static final long serialVersionUID = -1040791630003678766L;

	public static final int MAX_MESSAGES_BEFORE_REMOVAL = 3;
	
	private List<Notification> queue = Lists.newArrayList();
	
	public void addNotificatoin(String text, int duration) {
		addNotificatoin(new Notification(text, duration));
	}
	
	public void addNotificatoin(Notification notification) {
		queue.add(notification);
	}
	
	public Notification getCurrentNotification() {
		return getCurrentNotification(0);
	}
	
	public Notification getCurrentNotification(int n) {
		if (n >= queue.size()) {
			return null;
		}
		return queue.get(n);
	}
	
	public int queueSize() {
		return queue.size();
	}
	
	public void update(int delta) {
		
		int remaining = delta;
		Notification notification = queue.isEmpty() ? null : queue.get(0);
		
		while (notification != null && remaining > 0) {
			remaining = notification.decreaseDuration(remaining);
			if (notification.getDuration() == 0) {
				queue.remove(0);
				notification = queue.isEmpty() ? null : queue.get(0);
			}
		}
		
	}
	
}
