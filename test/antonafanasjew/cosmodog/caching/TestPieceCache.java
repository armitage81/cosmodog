package antonafanasjew.cosmodog.caching;

import antonafanasjew.cosmodog.domains.MapType;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.NpcActor;
import antonafanasjew.cosmodog.topology.Position;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestPieceCache {

    @Test
    public void test() throws Exception {
        PieceCache pieceCache = new PieceCache(MapType.MAIN, 10, 10);

        NpcActor piece;

        piece = new NpcActor();
        piece.setLife(1);
        piece.setPosition(Position.fromCoordinates(5, 5, null)); //sectorPosition 0/0
        pieceCache.addPiece(piece);

        piece = new NpcActor();
        piece.setLife(2);
        piece.setPosition(Position.fromCoordinates(15, 5, null)); //sectorPosition 1/0
        pieceCache.addPiece(piece);

        piece = new NpcActor();
        piece.setLife(3);
        piece.setPosition(Position.fromCoordinates(5, 15, null)); //sectorPosition 0/1
        pieceCache.addPiece(piece);

        piece = new NpcActor();
        piece.setLife(4);
        piece.setPosition(Position.fromCoordinates(15, 15, null)); //sectorPosition 1/1
        pieceCache.addPiece(piece);

        piece = new NpcActor();
        piece.setLife(5);
        piece.setPosition(Position.fromCoordinates(15, 15, null)); //sectorPosition 1/1 again
        pieceCache.addPiece(piece);

        List<Piece> pieces;

        NpcActor foundActor;

        pieces = pieceCache.piecesInArea((e) -> true, 0, 0, 10, 10);
        assertEquals(1, pieces.size());
        foundActor = ((NpcActor)pieces.getFirst());
        assertEquals(1, foundActor.getLife());

        pieces = pieceCache.piecesInArea((e) -> true, 10, 0, 10, 10);
        assertEquals(1, pieces.size());
        foundActor = ((NpcActor)pieces.getFirst());
        assertEquals(2, foundActor.getLife());

        pieces = pieceCache.piecesInArea((e) -> true, 0, 10, 10, 10);
        assertEquals(1, pieces.size());
        foundActor = ((NpcActor)pieces.getFirst());
        assertEquals(3, foundActor.getLife());

        pieces = pieceCache.piecesInArea((e) -> true, 10, 10, 10, 10);
        assertEquals(2, pieces.size());

        pieces = pieceCache.piecesInArea((e) -> true, 0, 0, 5, 5);
        assertEquals(0, pieces.size());

    }

}
