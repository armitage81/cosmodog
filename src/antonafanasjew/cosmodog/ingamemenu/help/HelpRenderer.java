package antonafanasjew.cosmodog.ingamemenu.help;



import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.globals.FontType;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.Renderer;
import antonafanasjew.cosmodog.util.TextBookRendererUtils;

public class HelpRenderer implements Renderer {

	
	@Override
	public void render(GameContainer gameContainer, Graphics graphics, DrawingContext drawingContext, Object renderingParameter) {
		
		String[] actions = new String[] {
		
			"Move",
	
			"Skip turn",
			
			"Switch to the next or previous weapon",
	
			"Switch to unarmed mode",
	
			"Exit a vehicle",
	
			"Zoom in",
			
			"Zoom out",
			
			"Quick save",
	
			"Quick load",
	
			"Menu",
	
			"Next menu page",
	
			"Log player",
	
			"Inventory",
	
			"Map",
	
			"Progress",
	
			"Help",
	
			"Save and Quit"
		};
		
		String[] keys = new String[] {
				
				"[UP],[DOWN],[LEFT],[RIGHT] (Use the arrow keys on the keyboard)",
		
				"[ENTER]",
				
				"[TAB] or [SHIFT] + [TAB] (Works only when at least one weapon was found)",
		
				"[TAB] or [SHIFT] + [TAB] multiple times to go through all available weapons until no weapon is selected.",
		
				"[SHIFT] + [UP] or [DOWN] or [LEFT] or [RIGHT]",
		
				"Z (if have binoculars)",
				
				"Y (if have binoculars)",
				
				"[CTRL] + [S]",
		
				"[CTRL] + [L]",
		
				"[ESC]",
		
				"[TAB]",
		
				"[L]",
		
				"[I]",
		
				"[M]",
		
				"[P]",
		
				"[H]",
		
				"[Q]"
			};
		
		for (int i = 0; i < 20; i++) {
			if (i < actions.length) {
				TileDrawingContext tdcAction = new TileDrawingContext(drawingContext, 10, 20, 0, i, 3, 1);
				TileDrawingContext tdcKey = new TileDrawingContext(drawingContext, 10, 20, 3, i, 7, 1);
				TextBookRendererUtils.renderVerticallyCenteredLabel(gameContainer, graphics, tdcAction, actions[i], FontType.HintsActions, 0);
				TextBookRendererUtils.renderVerticallyCenteredLabel(gameContainer, graphics, tdcKey, keys[i], FontType.HintsKeys, 0);
			}
		}
		
	}

}
