package antonafanasjew.cosmodog.model.states;

import java.text.ParseException;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.CosmodogStarter;
import antonafanasjew.cosmodog.debug.PropertyAssignment;
import antonafanasjew.cosmodog.debug.PropertyAssignmentExecutor;
import antonafanasjew.cosmodog.debug.PropertyAssignmentParser;
import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.globals.Features;
import antonafanasjew.cosmodog.globals.Fonts;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.TextFieldRenderer;
import antonafanasjew.cosmodog.rendering.renderer.TextRenderer;

public class DebugState extends CosmodogAbstractState {

	private TextField textField;
	private TextFieldRenderer tfr;
	
	@Override
	public void everyEnter(GameContainer gc, StateBasedGame game) throws SlickException {
		
		DrawingContext debugConsoleContentDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().debugConsoleContentDrawingContext();
		
		gc.getInput().clearKeyPressedRecord();
				
		//The bounds of the text field should have better been calculated in the renderer, but as the API requires them at the creation of the object, this is the only way.
		//Note that we do not set the x/y offset, as the renderer will translate the text field according to the drawing context anyway.
		textField = new TextField(gc, gc.getDefaultFont(), 0, 0, (int)debugConsoleContentDrawingContext.w(), (int)debugConsoleContentDrawingContext.h());
		textField.setAcceptingInput(true);
		textField.setFocus(true);
		textField.setConsumeEvents(true);
		textField.setBackgroundColor(Color.blue);
		tfr = new TextFieldRenderer(textField, debugConsoleContentDrawingContext);
		
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int n) throws SlickException {
		if (gc.getInput().isKeyPressed(Input.KEY_ENTER)) {
			try {
				handleDebugCommand();
				sbg.enterState(CosmodogStarter.GAME_STATE_ID, new FadeOutTransition(), new FadeInTransition());
			} catch (ParseException e) {
				textField.setText(e.getMessage());
			}
		} else if (gc.getInput().isKeyPressed(Input.KEY_ESCAPE)) {
			sbg.enterState(CosmodogStarter.GAME_STATE_ID, new FadeOutTransition(), new FadeInTransition());
		}
	}

	private void handleDebugCommand() throws ParseException {
		CosmodogGame cosmodogGame = ApplicationContext.instance().getCosmodog().getCosmodogGame();
		try {
    		String text = textField.getText();
    		
    		String[] commands = text.split(";");
    		
    		for (String command : commands) {
        		if (command.startsWith("feature.")) {
        			String[] parts = command.split("=");
        			String feature = parts[0];
        			boolean flag = Boolean.valueOf(parts[1]);
        			if (Features.getInstance().getFeatureFlags().get(feature) != null) {
        				Features.getInstance().getFeatureFlags().put(feature, flag);
        			} else {
        				throw new ParseException("No feature '" + feature + "' found" , 0);
        			}
        		} else {
        			try {
            			PropertyAssignmentParser parser = new PropertyAssignmentParser();
            			PropertyAssignment propertyAssignment = parser.parseCommand(command);
            			PropertyAssignmentExecutor executor = new PropertyAssignmentExecutor();
            			executor.execute(cosmodogGame, propertyAssignment);
        			} catch (Exception e) {
        				e.printStackTrace();
        				throw new ParseException("Error: See console", 0);
        			}
        			
        		}
    		}
		} catch (Exception e) {
			throw new ParseException(e.getLocalizedMessage(), 0);
		}
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		
		DrawingContext debugConsoleHeaderDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().debugConsoleHeaderDrawingContext();
		DrawingContext debugConsoleControlsDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().debugConsoleControlsDrawingContext();
		
		TextRenderer trHeader = new TextRenderer(Fonts.DEBUG_FONT, true, debugConsoleHeaderDrawingContext);
		TextRenderer trControls = new TextRenderer(Fonts.DEBUG_FONT, true, debugConsoleControlsDrawingContext);
		
		trHeader.render(gc, g, "Debug console");
		
		tfr.renderInternally(gc, g, null);
		
		trControls.render(gc, g, "Press Enter to execute debug command. Press Esc to return to the game.");
		
	}

	@Override
	public int getID() {
		return CosmodogStarter.DEBUG_STATE_ID;
	}

}
