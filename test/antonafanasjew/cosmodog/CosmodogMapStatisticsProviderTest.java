package antonafanasjew.cosmodog;

import java.util.Map;
import java.util.function.Function;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import antonafanasjew.cosmodog.model.CollectibleComposed;
import antonafanasjew.cosmodog.model.CollectibleGoodie;
import antonafanasjew.cosmodog.model.CollectibleGoodie.GoodieType;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.inventory.GoodieInventoryItem;
import antonafanasjew.cosmodog.topology.Position;

public class CosmodogMapStatisticsProviderTest {

	@Test
	public void testPieceValueInInfobitsFunction() {
		
		Function<Piece, Integer> f = CosmodogMapStatisticsProvider.getInstance().pieceValueInInfobitsFunction();
		
		Enemy enemyHoldingInfobit = new Enemy();
		enemyHoldingInfobit.setInventoryItem(new GoodieInventoryItem(GoodieType.infobit));
		
		Enemy enemyHoldingInfobyte = new Enemy();
		enemyHoldingInfobyte.setInventoryItem(new GoodieInventoryItem(GoodieType.infobyte));
		
		Enemy enemyHoldingInfobank = new Enemy();
		enemyHoldingInfobank.setInventoryItem(new GoodieInventoryItem(GoodieType.infobank));
		
		Enemy enemyWithoutItems = new Enemy();
		
		CollectibleGoodie infobit = new CollectibleGoodie(GoodieType.infobit);
		CollectibleGoodie infobyte = new CollectibleGoodie(GoodieType.infobyte);
		CollectibleGoodie infobank = new CollectibleGoodie(GoodieType.infobank);
		CollectibleGoodie supplies = new CollectibleGoodie(GoodieType.supplies);
		
		CollectibleComposed composedInfobits = new CollectibleComposed();
		composedInfobits.addElement(infobit);
		composedInfobits.addElement(infobyte);
		composedInfobits.addElement(infobank);
		
		CollectibleComposed composedSupplies = new CollectibleComposed();
		composedSupplies.addElement(supplies);
		composedSupplies.addElement(supplies);
		
		CollectibleComposed composedMixed = new CollectibleComposed();
		composedMixed.addElement(infobit);
		composedMixed.addElement(infobank);
		composedMixed.addElement(supplies);
		
		Assert.assertEquals(1, (int)f.apply(enemyHoldingInfobit));
		Assert.assertEquals(5, (int)f.apply(enemyHoldingInfobyte));
		Assert.assertEquals(25, (int)f.apply(enemyHoldingInfobank));
		Assert.assertEquals(0, (int)f.apply(enemyWithoutItems));
		
		Assert.assertEquals(1, (int)f.apply(infobit));
		Assert.assertEquals(5, (int)f.apply(infobyte));
		Assert.assertEquals(25, (int)f.apply(infobank));
		Assert.assertEquals(0, (int)f.apply(supplies));
		
		Assert.assertEquals(31, (int)f.apply(composedInfobits));
		Assert.assertEquals(0, (int)f.apply(composedSupplies));
		Assert.assertEquals(26, (int)f.apply(composedMixed));
		
	}

	@Test
	public void testInfobitValuePerChartPiece() {
		int chartPieceWidthInTiles = 2;
		int chartPieceHeightInTiles = 2;
		CollectibleGoodie g = new CollectibleGoodie(GoodieType.infobit);
		Position p1 = Position.fromCoordinates(0, 0);
		Position p2 = Position.fromCoordinates(1, 1);
		Position p3 = Position.fromCoordinates(2, 0);
		Position p4 = Position.fromCoordinates(0, 2);
		Map<Position, Piece> map = Maps.newHashMap();
		map.put(p1,  g);
		map.put(p2,  g);
		map.put(p3,  g);
		map.put(p4,  g);
		Map<Position, Integer> result = CosmodogMapStatisticsProvider.getInstance().infobitValuePerChartPiece(map, Sets.newHashSet(), chartPieceWidthInTiles, chartPieceHeightInTiles);
		
		Position m1 = Position.fromCoordinates(0f, 0f);
		Position m2 = Position.fromCoordinates(0f, 1f);
		Position m3 = Position.fromCoordinates(1f, 0f);
		Position m4 = Position.fromCoordinates(1f, 1f);
		
		Assert.assertEquals(2, (int)result.get(m1));
		Assert.assertEquals(1, (int)result.get(m2));
		Assert.assertEquals(1, (int)result.get(m3));
		Assert.assertNull(result.get(m4));
	}

}
