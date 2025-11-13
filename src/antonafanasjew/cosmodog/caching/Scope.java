package antonafanasjew.cosmodog.caching;

public class Scope {

    public static final Scope LOOP = new Scope("scope.loop");

    private String id;

    public Scope(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public boolean equals(Object other) {
    	if (other instanceof Scope otherScope) {
    		return this.id.equals(otherScope.id);
    	}
    	return false;
    }
}
