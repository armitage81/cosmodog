package antonafanasjew.cosmodog.rendering.renderer;

import java.util.List;
import java.util.Map;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import com.google.common.collect.Lists;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.tooltip.WeaponTooltipAction;
import antonafanasjew.cosmodog.actions.tooltip.WeaponTooltipAction.WeaponTooltipTransition;
import antonafanasjew.cosmodog.domains.ArmorType;
import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.upgrades.Weapon;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.LetterTextRenderer.LetterTextRenderingParameter;
import antonafanasjew.cosmodog.text.Letter;
import antonafanasjew.cosmodog.text.LetterUtils;
import antonafanasjew.cosmodog.topology.Rectangle;

public class WeaponTooltipRenderer implements Renderer {


	@Override
	public void render(GameContainer gameContainer, Graphics graphics, DrawingContext drawingContext, Object renderingParameter) {
		Map<Character, Letter> characterLetters = ApplicationContext.instance().getCharacterLetters();
		Letter defaultLetter = characterLetters.get('?');
		
		ApplicationContext applicationContext = ApplicationContext.instance();
		Cosmodog cosmodog = applicationContext.getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();

		WeaponTooltipAction action = (WeaponTooltipAction)cosmodogGame.getActionRegistry().getRegisteredAction(AsyncActionType.WEAPON_TOOLTIP);
		
		if (action == null) {
			return;
		}
		
		WeaponTooltipTransition transition = action.getTransition();
		
		if (transition == null) {
			return;
		}
		
		
		float completion = transition.completion;

		Weapon weapon = transition.weapon;
		
		List<String> weaponTypeTexts = Lists.newArrayList();
		weaponTypeTexts.add(weapon.getWeaponType().name());
		weaponTypeTexts.add("Ammo: " + weapon.getAmmunition());
		WeaponType weaponType = weapon.getWeaponType();
		weaponTypeTexts.add("Basic damage: " + weaponType.getBasicDamage());
		for (ArmorType armorType : ArmorType.values()) {
			int damageVsArmor = weaponType.getDamage(armorType);
			if (damageVsArmor != weaponType.getBasicDamage()) {
				weaponTypeTexts.add("Damage vs " + armorType + ": " + damageVsArmor);
			}
		}
		
		
		List<Letter> dummyLetters = LetterUtils.lettersForText("dummy", characterLetters, defaultLetter);
		Rectangle textBounds = LetterUtils.letterListBounds(dummyLetters, 0);
		
		float letterHeight = textBounds.getHeight();
		int numberOfLetterLinesFittingInDrawingContext = (int)drawingContext.h() / (int)letterHeight;
		
		for (int i = 0; i < weaponTypeTexts.size(); i++) {
			String weaponTypeText = weaponTypeTexts.get(i);
			DrawingContext textLineDc = new TileDrawingContext(drawingContext, 1, numberOfLetterLinesFittingInDrawingContext, 0, i);
			LetterTextRenderer.getInstance().render(gameContainer, graphics, textLineDc, LetterTextRenderingParameter.fromText(weaponTypeText));			
		}
	}

}
