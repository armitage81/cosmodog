package antonafanasjew.cosmodog.ingamemenu.map;

import java.util.Map;

import com.google.common.collect.Maps;

import antonafanasjew.cosmodog.CosmodogMapStatisticsProvider;
import antonafanasjew.cosmodog.ingamemenu.InGameMenuInputState;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.ChartInventoryItem;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class MapInputState implements InGameMenuInputState {

	public static float OFFSET_STEP = 0.25f;
	
	private int selectionX = 0;
	private int selectionY = 0;
	
	private float offsetX = 0;
	private float offsetY = 0;
	
	private Map<Position, Integer> infobitNumbersByChartPiece = Maps.newHashMap();
	
	public void left() {
		if (selectionX > 0 || offsetX > 0) {
			if (offsetX > 0) {
				offsetX -= OFFSET_STEP;
			} else {
				selectionX -= 1;
				offsetX = 1 - OFFSET_STEP;
			}
		}
	}
	
	public void up() {
		if (selectionY > 0 || offsetY > 0) {
			if (offsetY > 0) {
				offsetY -= OFFSET_STEP;
			} else {
				selectionY -= 1;
				offsetY = 1 - OFFSET_STEP;
			}
		}
	}
	
	public void right() {
		if (selectionX < ChartInventoryItem.VISIBLE_CHART_PIECE_NUMBER_X - 1) {
			
			if (offsetX < 1 - OFFSET_STEP) {
				offsetX += OFFSET_STEP;
			} else {
				selectionX += 1;
				offsetX = 0;
			}
			
		}
	}
	
	public void down() {
		if (selectionY < ChartInventoryItem.VISIBLE_CHART_PIECE_NUMBER_Y - 1) {
			
			if (offsetY < 1 - OFFSET_STEP) {
				offsetY += OFFSET_STEP;
			} else {
				selectionY += 1;
				offsetY = 0;
			}
			
		}
	}
	
	public int getSelectionX() {
		return selectionX;
	}
	public void setSelectionX(int selectionX) {
		this.selectionX = selectionX;
	}
	public int getSelectionY() {
		return selectionY;
	}
	public void setSelectionY(int selectionY) {
		this.selectionY = selectionY;
	}
	
	public float getOffsetX() {
		return offsetX;
	}
	
	public float getOffsetY() {
		return offsetY;
	}

	public Map<Position, Integer> getInfobitNumbersByChartPiece() {
		return infobitNumbersByChartPiece;
	}
	
	@Override
	public void initializeState() {
		Player player = ApplicationContextUtils.getPlayer();
		CosmodogMap map = ApplicationContextUtils.getCosmodogMap();
		int mapWidth = map.getWidth();
		int mapHeight = map.getHeight();
		int posX = (int)player.getPosition().getX();
		int posY = (int)player.getPosition().getY();
		
		int chartPieceWidthInTiles = mapWidth / ChartInventoryItem.VISIBLE_CHART_PIECE_NUMBER_X;
		int chartPieceHeightInTiles = mapHeight / ChartInventoryItem.VISIBLE_CHART_PIECE_NUMBER_Y;
		
		selectionX = posX / chartPieceWidthInTiles;
		selectionY = posY / chartPieceHeightInTiles;
		offsetX = 0;
		offsetY = 0;
		
		infobitNumbersByChartPiece.clear();
		infobitNumbersByChartPiece.putAll(CosmodogMapStatisticsProvider.getInstance().infobitValuePerChartPiece(map.getMapPieces(), map.getEnemies(), chartPieceWidthInTiles, chartPieceHeightInTiles));
		//System.out.println(infobitNumbersByChartPiece.entrySet().stream().mapToInt(e -> e.getValue()).sum());
	}
	
}
