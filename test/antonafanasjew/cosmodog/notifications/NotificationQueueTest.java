package antonafanasjew.cosmodog.notifications;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NotificationQueueTest {

	private Notification n1 = new Notification("N1", 3000);
	private Notification n2 = new Notification("N2", 1000);
	private Notification n3 = new Notification("N3", 1000);
	private Notification n4 = new Notification("N4", 1000);
	private Notification n5 = new Notification("N5", 1000);
	
	private NotificationQueue q;
	
	@BeforeEach
	public void setUp() {
		q = new NotificationQueue();
		q.addNotificatoin(n1);
		q.addNotificatoin(n2);
		q.addNotificatoin(n3);
		q.addNotificatoin(n4);
		q.addNotificatoin(n5);
	}
	
	@Test
	public void testUpdate() throws Exception {
		assertEquals("N1", q.getCurrentNotification().getText());
		assertEquals(3000, q.getCurrentNotification().getDuration());
		
		q.update(1000);
		
		assertEquals("N1", q.getCurrentNotification().getText());
		assertEquals(2000, q.getCurrentNotification().getDuration());
		
		q.update(2000);
		
		assertEquals("N2", q.getCurrentNotification().getText());
		assertEquals(1000, q.getCurrentNotification().getDuration());
		
		q.update(2000);
		
		assertEquals("N4", q.getCurrentNotification().getText());
		assertEquals(1000, q.getCurrentNotification().getDuration());
		
		q.update(1500);
		
		assertEquals("N5", q.getCurrentNotification().getText());
		assertEquals(500, q.getCurrentNotification().getDuration());
		
		q.update(1000);
		
		assertEquals(null, q.getCurrentNotification());
		
	}
	
}
