package antonafanasjew.cosmodog.sight;

public class VisionElement {

    private final int x;
    private final int y;

    public VisionElement(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof VisionElement) {
            VisionElement other = (VisionElement) obj;
            return x == other.x && y == other.y;
        }
        return false;
    }



}
