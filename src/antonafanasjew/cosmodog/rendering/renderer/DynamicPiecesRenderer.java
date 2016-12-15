package antonafanasjew.cosmodog.rendering.renderer;

import java.util.Collection;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.DynamicPiece;
import antonafanasjew.cosmodog.model.dynamicpieces.Bamboo;
import antonafanasjew.cosmodog.model.dynamicpieces.Crate;
import antonafanasjew.cosmodog.model.dynamicpieces.HardStone;
import antonafanasjew.cosmodog.model.dynamicpieces.Stone;
import antonafanasjew.cosmodog.model.dynamicpieces.Tree;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;

import com.google.common.collect.Multimap;



public class DynamicPiecesRenderer extends AbstractRenderer {

	public static class DynamicPiecesRendererParam {
		
		public static DynamicPiecesRendererParam BOTTOM = new DynamicPiecesRendererParam(true);
		public static DynamicPiecesRendererParam TOP = new DynamicPiecesRendererParam(false);

		private boolean bottomNotTop;
		
		public DynamicPiecesRendererParam(boolean bottomNotTop) {
			this.bottomNotTop = bottomNotTop;
		}

		public boolean isBottomNotTop() {
			return bottomNotTop;
		}

	}
	
	@Override
	protected void renderFromZero(GameContainer gameContainer, Graphics graphics, DrawingContext drawingContext, Object renderingParameter) {

		DynamicPiecesRendererParam dynamicPiecerenderingParam = (DynamicPiecesRendererParam)renderingParameter;
		
		ApplicationContext applicationContext = ApplicationContext.instance();
		Cosmodog cosmodog = applicationContext.getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();
		CosmodogMap map = cosmodogGame.getMap();
		Cam cam = cosmodogGame.getCam();
		
		int tileWidth = map.getTileWidth();
		int tileHeight = map.getTileHeight();

		int scaledTileWidth = (int) (tileWidth * cam.getZoomFactor());
		int scaledTileHeight = (int) (tileHeight * cam.getZoomFactor());

		int camX = (int) cam.viewCopy().x();
		int camY = (int) cam.viewCopy().y();

		int x = -(int) ((camX % scaledTileWidth));
		int y = -(int) ((camY % scaledTileHeight));
		
		int tileNoX = camX / scaledTileWidth;
		int tileNoY = camY / scaledTileHeight;
		
		int tilesW = (int) (cam.viewCopy().width()) / scaledTileWidth + 2;
		int tilesH = (int) (cam.viewCopy().height()) / scaledTileHeight + 2;
		
		graphics.translate(x, y);
		graphics.scale(cam.getZoomFactor(), cam.getZoomFactor());
		
		Multimap<Class<?>, DynamicPiece> dynamicPieces = map.visibleDynamicPieces(tileNoX, tileNoY, tilesW, tilesH, 2);
		
		Collection<DynamicPiece> stones = dynamicPieces.get(Stone.class);
		
		for (DynamicPiece piece : stones) {
			Stone stone = (Stone) piece;
			
			String animationIdPrefix = "dynamicPieceStone";
			String animationIdPrefixIndex = String.valueOf(stone.getShapeNumber());
			String animationIdInfix = dynamicPiecerenderingParam.isBottomNotTop() ? "Bottom" : "Top";
			String animationSuffix = stone.animationSuffixFromState();
			String animationId = animationIdPrefix + animationIdPrefixIndex + animationIdInfix + animationSuffix;
			applicationContext.getAnimations().get(animationId).draw((piece.getPositionX() - tileNoX) * tileWidth, (piece.getPositionY() - tileNoY - (dynamicPiecerenderingParam.isBottomNotTop() ? 0 : 1)) * tileHeight);
			
		}
		
		Collection<DynamicPiece> hardStones = dynamicPieces.get(HardStone.class);
		
		for (DynamicPiece piece : hardStones) {
			HardStone hardStone = (HardStone) piece;
			
			String animationIdPrefix = "dynamicPieceHardStone";
			String animationIdPrefixIndex = String.valueOf(hardStone.getShapeNumber());
			String animationIdInfix = dynamicPiecerenderingParam.isBottomNotTop() ? "Bottom" : "Top";
			String animationSuffix = hardStone.animationSuffixFromState();
			String animationId = animationIdPrefix + animationIdPrefixIndex + animationIdInfix + animationSuffix;
			applicationContext.getAnimations().get(animationId).draw((piece.getPositionX() - tileNoX) * tileWidth, (piece.getPositionY() - tileNoY - (dynamicPiecerenderingParam.isBottomNotTop() ? 0 : 1)) * tileHeight);
			
		}
		
		Collection<DynamicPiece> trees = dynamicPieces.get(Tree.class);
		
		for (DynamicPiece piece : trees) {
			Tree tree = (Tree) piece;
			
			String animationIdPrefix = "dynamicPieceTree";
			String animationIdPrefixIndex = String.valueOf(tree.getShapeNumber());
			String animationIdInfix = dynamicPiecerenderingParam.isBottomNotTop() ? "Bottom" : "Top";
			String animationSuffix = tree.animationSuffixFromState();
			String animationId = animationIdPrefix + animationIdPrefixIndex + animationIdInfix + animationSuffix;
			applicationContext.getAnimations().get(animationId).draw((piece.getPositionX() - tileNoX) * tileWidth, (piece.getPositionY() - tileNoY - (dynamicPiecerenderingParam.isBottomNotTop() ? 0 : 1)) * tileHeight);
			
		}
		
		
		Collection<DynamicPiece> bamboos = dynamicPieces.get(Bamboo.class);
		
		for (DynamicPiece piece : bamboos) {
			Bamboo bamboo = (Bamboo) piece;
			
			String animationIdPrefix = "dynamicPieceBamboo";
			String animationIdPrefixIndex = String.valueOf(bamboo.getShapeNumber());
			String animationIdInfix = dynamicPiecerenderingParam.isBottomNotTop() ? "Bottom" : "Top";
			String animationSuffix = bamboo.animationSuffixFromState();
			String animationId = animationIdPrefix + animationIdPrefixIndex + animationIdInfix + animationSuffix;
			applicationContext.getAnimations().get(animationId).draw((piece.getPositionX() - tileNoX) * tileWidth, (piece.getPositionY() - tileNoY - (dynamicPiecerenderingParam.isBottomNotTop() ? 0 : 1)) * tileHeight);
			
		}
		
		Collection<DynamicPiece> crates = dynamicPieces.get(Crate.class);
		
		for (DynamicPiece piece : crates) {
			Crate crate = (Crate) piece;
			
			String animationIdPrefix = "dynamicPieceCrate";
			String animationIdPrefixIndex = String.valueOf(crate.getShapeNumber());
			String animationIdInfix = dynamicPiecerenderingParam.isBottomNotTop() ? "Bottom" : "Top";
			String animationSuffix = crate.animationSuffixFromState();
			String animationId = animationIdPrefix + animationIdPrefixIndex + animationIdInfix + animationSuffix;
			applicationContext.getAnimations().get(animationId).draw((piece.getPositionX() - tileNoX) * tileWidth, (piece.getPositionY() - tileNoY - (dynamicPiecerenderingParam.isBottomNotTop() ? 0 : 1)) * tileHeight);
			
		}
		
		graphics.scale(1 / cam.getZoomFactor(), 1 / cam.getZoomFactor());
		graphics.translate(-x, -y);
		
	}

}
