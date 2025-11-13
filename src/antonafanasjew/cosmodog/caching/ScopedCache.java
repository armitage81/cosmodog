package antonafanasjew.cosmodog.caching;


import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ScopedCache implements ScopeResetListener {

    public static ScopedCache CACHE_FOR_LOOP = new ScopedCache(Scope.LOOP);

    private Scope scope;

    private ScopedCache(Scope scope) {
        this.scope = scope;
    }

    private final Map<String, Object> cache = new HashMap<>();

    public <T> T getOrCalculate(String key, Supplier<T> supplier) {
        if (cache.containsKey(key)) {
    		return (T)cache.get(key);
    	} else {
    		T value = supplier.get();
    		cache.put(key, value);
    		return value;
    	}
    }

    @Override
    public void onScopeReset(Scope scope) {
        if (scope.equals(this.scope)) {
            cache.clear();
        }

    }
}
