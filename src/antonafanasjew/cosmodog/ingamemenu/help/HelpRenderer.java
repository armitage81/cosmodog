package antonafanasjew.cosmodog.ingamemenu.help;



import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.globals.FontProvider.FontTypeName;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.Renderer;
import antonafanasjew.cosmodog.rendering.renderer.textbook.FontRefToFontTypeMap;
import antonafanasjew.cosmodog.rendering.renderer.textbook.TextPageConstraints;
import antonafanasjew.cosmodog.rendering.renderer.textbook.placement.Book;
import antonafanasjew.cosmodog.util.TextBookRendererUtils;

public class HelpRenderer implements Renderer {

	
	@Override
	public void render(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {
		
		DrawingContext inGameMenuContentDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().inGameMenuContentDrawingContext();
		
		String[] actions = new String[] {
		
			"Move",
	
			"Skip turn",
			
			"Switch weapon",
	
			"Exit a vehicle",
	
			"Zoom in",
			
			"Zoom out",
	
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
				
				"[UP],[DOWN],[LEFT],[RIGHT]",
		
				"[ENTER]",
				
				"[TAB] or [SHIFT] + [TAB]",
		
				"[SHIFT] + [UP] or [DOWN] or [LEFT] or [RIGHT]",
		
				"Z (if owning binoculars)",
				
				"Y (if owning binoculars)",
		
				"[ESC]",
		
				"[TAB]",
		
				"[L]",
		
				"[I]",
		
				"[M]",
		
				"[P]",
		
				"[H]",
		
				"[Q]"
			};
		
		FontRefToFontTypeMap fontTypeSubheaders = FontRefToFontTypeMap.forOneFontTypeName(FontTypeName.SubHeader);
		FontRefToFontTypeMap fontTypeInformational = FontRefToFontTypeMap.forOneFontTypeName(FontTypeName.Informational);
		
		for (int i = 0; i < 14; i++) {
			if (i < actions.length) {
				
				Book textBook;
				
				TileDrawingContext tdcAction = new TileDrawingContext(inGameMenuContentDrawingContext, 10, 14, 0, i, 3, 1);
				TileDrawingContext tdcKey = new TileDrawingContext(inGameMenuContentDrawingContext, 10, 14, 3, i, 7, 1);
				
				textBook = TextPageConstraints.fromDc(tdcAction).textToBook(actions[i], fontTypeSubheaders);
				TextBookRendererUtils.renderVerticallyCenteredLabel(gameContainer, graphics, textBook);
				textBook = TextPageConstraints.fromDc(tdcKey).textToBook(keys[i], fontTypeInformational);
				TextBookRendererUtils.renderVerticallyCenteredLabel(gameContainer, graphics, textBook);
			}
		}
		
	}

}
