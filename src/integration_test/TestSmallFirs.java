package integration_test;

import antonafanasjew.cosmodog.globals.CosmodogModelHolder;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.DebuggerInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.JacketInventoryItem;
import antonafanasjew.cosmodog.model.inventory.SkiInventoryItem;
import antonafanasjew.cosmodog.player.AbstractPlayerBuilder;
import antonafanasjew.cosmodog.player.PlayerBuilder;
import antonafanasjew.cosmodog.topology.Position;
import org.newdawn.slick.SlickException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TestSmallFirs {

	public static void main(String[] args) throws SlickException {
		
		PlayerBuilder playerBuilder = new AbstractPlayerBuilder() {
			
			@Override
			protected void updatePlayer(Player player) {
				player.setPosition(Position.fromCoordinates(378, 320));
				player.setMaxLife(100);
				player.setLife(100);
				player.getGameProgress().setWormActive(false);
				player.getInventory().put(InventoryItemType.SKI, new SkiInventoryItem());
				player.getInventory().put(InventoryItemType.JACKET, new JacketInventoryItem());

				List<String> debuggerPositions = new ArrayList<>();
				debuggerPositions.add("211/59");
				debuggerPositions.add("209/61");
				debuggerPositions.add("212/64");
				debuggerPositions.add("186/126");
				debuggerPositions.add("190/126");
				debuggerPositions.add("202/127");
				debuggerPositions.add("47/182");
				debuggerPositions.add("372/316");
				debuggerPositions.add("377/320");
				debuggerPositions.add("380/326");
				debuggerPositions.add("321/329");
				debuggerPositions.add("374/332");
				debuggerPositions.add("380/333");
				debuggerPositions.add("328/334");
				debuggerPositions.add("330/334");
				debuggerPositions.add("390/337");
				debuggerPositions.add("311/339");
				debuggerPositions.add("384/342");
				debuggerPositions.add("61/344");
				debuggerPositions.add("40/345");
				debuggerPositions.add("39/348");
				debuggerPositions.add("235/349");
				debuggerPositions.add("240/349");
				debuggerPositions.add("73/351");
				debuggerPositions.add("242/351");
				debuggerPositions.add("338/351");
				debuggerPositions.add("340/351");
				debuggerPositions.add("358/352");
				debuggerPositions.add("360/352");
				debuggerPositions.add("80/353");
				debuggerPositions.add("250/353");
				debuggerPositions.add("290/353");
				debuggerPositions.add("30/356");
				debuggerPositions.add("230/356");
				debuggerPositions.add("28/358");
				debuggerPositions.add("90/358");
				debuggerPositions.add("225/358");
				debuggerPositions.add("80/360");
				debuggerPositions.add("353/361");
				debuggerPositions.add("96/363");
				debuggerPositions.add("113/363");
				debuggerPositions.add("9/365");
				debuggerPositions.add("10/365");
				debuggerPositions.add("21/365");
				debuggerPositions.add("120/365");
				debuggerPositions.add("343/365");
				debuggerPositions.add("382/365");
				debuggerPositions.add("326/366");
				debuggerPositions.add("330/367");
				debuggerPositions.add("30/368");
				debuggerPositions.add("229/368");
				debuggerPositions.add("230/368");
				debuggerPositions.add("75/369");
				debuggerPositions.add("279/369");
				debuggerPositions.add("280/369");
				debuggerPositions.add("234/370");
				debuggerPositions.add("34/371");
				debuggerPositions.add("100/371");
				debuggerPositions.add("202/372");
				debuggerPositions.add("220/372");
				debuggerPositions.add("97/373");
				debuggerPositions.add("216/373");
				debuggerPositions.add("243/375");
				debuggerPositions.add("337/375");
				debuggerPositions.add("340/375");
				debuggerPositions.add("390/375");
				debuggerPositions.add("46/377");
				debuggerPositions.add("60/377");
				debuggerPositions.add("70/377");
				debuggerPositions.add("50/378");
				debuggerPositions.add("277/378");
				debuggerPositions.add("280/378");
				debuggerPositions.add("290/378");
				debuggerPositions.add("383/378");
				debuggerPositions.add("307/379");
				debuggerPositions.add("310/379");
				debuggerPositions.add("311/380");
				debuggerPositions.add("150/381");
				debuggerPositions.add("55/382");
				debuggerPositions.add("380/383");
				debuggerPositions.add("60/384");
				debuggerPositions.add("90/384");
				debuggerPositions.add("105/384");
				debuggerPositions.add("89/385");
				debuggerPositions.add("110/385");
				debuggerPositions.add("129/385");
				debuggerPositions.add("130/385");
				debuggerPositions.add("160/385");
				debuggerPositions.add("180/385");
				debuggerPositions.add("193/385");
				debuggerPositions.add("379/385");
				debuggerPositions.add("140/387");
				debuggerPositions.add("200/387");
				debuggerPositions.add("7/388");
				debuggerPositions.add("290/388");
				debuggerPositions.add("287/389");
				debuggerPositions.add("220/390");
				debuggerPositions.add("293/390");
				debuggerPositions.add("97/391");
				debuggerPositions.add("100/391");
				debuggerPositions.add("160/391");
				debuggerPositions.add("180/391");
				debuggerPositions.add("216/391");
				debuggerPositions.add("356/391");
				debuggerPositions.add("37/392");
				debuggerPositions.add("150/392");
				debuggerPositions.add("178/392");
				debuggerPositions.add("147/393");
				debuggerPositions.add("190/393");
				debuggerPositions.add("202/393");
				debuggerPositions.add("340/393");

				player.getInventory().put(InventoryItemType.DEBUGGER, new DebuggerInventoryItem(String.join(";", debuggerPositions)));
			}
		};

		CosmodogModelHolder.replacePlayerBuilder(playerBuilder);

		TestStarter.main(args);
	}
	
}
