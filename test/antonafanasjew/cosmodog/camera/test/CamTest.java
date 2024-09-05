package antonafanasjew.cosmodog.camera.test;

import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.camera.CamPositioningException;
import antonafanasjew.cosmodog.topology.PlacedRectangle;
import antonafanasjew.cosmodog.topology.Rectangle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CamTest {

	private Cam cam;
	private Rectangle originalScene;
	private PlacedRectangle originalView;

	@BeforeEach
	public void init() throws CamPositioningException {
		originalScene = Rectangle.fromSize(1000, 1000);
		originalView = PlacedRectangle.fromAnchorAndSize(0, 0, 50, 50);
		cam = new Cam(Cam.CAM_MODE_CENTER_IN_SCENE, originalScene, 0, 0, originalView.width(), originalView.height());
	}
	
	//Slightly shift.
	@Test
	public void testShifting() {
		cam.gentleShift(-10, 0);
		assertEquals(-10f, cam.viewCopy().x(), 0.00001);
		cam.gentleShift(20f, 0);
		assertEquals(10f, cam.viewCopy().x(), 0.00001);
		
		cam.gentleShift(0, -10);
		assertEquals(-10, cam.viewCopy().y(), 0.00001);
		cam.gentleShift(0, 20);
		assertEquals(10f, cam.viewCopy().y(), 0.00001);
	}
	
	
	//Move as far as possible to the left.
	@Test
	public void testBorderShifting() {
		cam.gentleShift(-25, 0);
		assertEquals(-25f, cam.viewCopy().x(), 0.00001);
		cam.gentleShift(50f, 0);
		assertEquals(25f, cam.viewCopy().x(), 0.00001);
		
		cam.gentleShift(0, -25);
		assertEquals(-25, cam.viewCopy().y(), 0.00001);
		cam.gentleShift(0, 50);
		assertEquals(25f, cam.viewCopy().y(), 0.00001);
	}
	
	//Try to move further then edge to the left.
	@Test
	public void testBeyoundBorderShifting() {
		cam.gentleShift(-26, 0);
		assertEquals(-25f, cam.viewCopy().x(), 0.00001);
		cam.gentleShift(1000 + 1, 0);
		assertEquals(1000 - 25, cam.viewCopy().x(), 0.00001);
		
		cam.gentleShift(0, -26);
		assertEquals(-25, cam.viewCopy().y(), 0.00001);
		cam.gentleShift(0, 1000 + 1);
		assertEquals(1000 - 25, cam.viewCopy().y(), 0.00001);
	}
	
	//Zoom out, test proportions
	@Test
	public void testZoomingOut() {
		cam.zoom(0.5f);
		assertEquals(500, cam.sceneCopy().getWidth(), 0.0f);
		assertEquals(500, cam.sceneCopy().getHeight(), 0.0f);
		assertEquals(-12.5f, cam.viewCopy().x(), 0.0f);
		assertEquals(-12.5f, cam.viewCopy().y(), 0.0f);
		//Now the map is zoomed already, so the new zoom has to be calculated based on the old zoom.
		cam.zoom(0.25f);
		assertEquals(250, cam.sceneCopy().getWidth(), 0.0f);
		assertEquals(250, cam.sceneCopy().getHeight(), 0.0f);
		assertEquals(-18.75f, cam.viewCopy().x(), 0.0f);
		assertEquals(-18.75f, cam.viewCopy().y(), 0.0f);
	}

	//Zoom in, test proportions
	@Test
	public void testZoomingIn() {
		cam.zoom(2f);
		assertEquals(2000, cam.sceneCopy().getWidth(), 0.0f);
		assertEquals(2000, cam.sceneCopy().getHeight(), 0.0f);
		assertEquals(25f, cam.viewCopy().x(), 0.0f);
		assertEquals(25f, cam.viewCopy().y(), 0.0f);
		//Now the map is zoomed already, so the new zoom has to be calculated based on the old zoom.
		cam.zoom(4.0f);
		assertEquals(4000, cam.sceneCopy().getWidth(), 0.0f);
		assertEquals(4000, cam.sceneCopy().getHeight(), 0.0f);
		assertEquals(75f, cam.viewCopy().x(), 0.0f);
		assertEquals(75f, cam.viewCopy().y(), 0.0f);
	}
	
	//Zoom out after shift, test proportions
	@Test
	public void testZoomingOutAfterShift() {
		cam.gentleShift(100, 200);
		
		float xProportion = cam.viewCopy().centerX() / cam.sceneCopy().getWidth();
		float yProportion = cam.viewCopy().centerY() / cam.sceneCopy().getHeight();
		
		cam.zoom(0.5f);
		
		float xProportionNew = cam.viewCopy().centerX() / cam.sceneCopy().getWidth();
		float yProportionNew = cam.viewCopy().centerY() / cam.sceneCopy().getHeight();
		
		assertEquals(xProportion, xProportionNew, 0.0f);
		assertEquals(yProportion, yProportionNew, 0.0f);
		
	}

	//Zoom in after shift, test proportions
	@Test
	public void testZoomingInAfterShift() {
		cam.gentleShift(100, 200);
		
		float xProportion = cam.viewCopy().centerX() / cam.sceneCopy().getWidth();
		float yProportion = cam.viewCopy().centerY() / cam.sceneCopy().getHeight();
		
		cam.zoom(2f);
		
		float xProportionNew = cam.viewCopy().centerX() / cam.sceneCopy().getWidth();
		float yProportionNew = cam.viewCopy().centerY() / cam.sceneCopy().getHeight();
		
		assertEquals(xProportion, xProportionNew, 0.0f);
		assertEquals(yProportion, yProportionNew, 0.0f);
	}

}
