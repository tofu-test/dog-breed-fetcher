package dogapi;

import java.util.*;

/**
 * This BreedFetcher caches fetch request results to improve performance and
 * lessen the load on the underlying data source. An implementation of BreedFetcher
 * must be provided. The number of calls to the underlying fetcher are recorded.
 *
 * If a call to getSubBreeds produces a BreedNotFoundException, then it is NOT cached
 * in this implementation. The provided tests check for this behaviour.
 *
 * The cache maps the name of a breed to its list of sub breed names.
 */
public class CachingBreedFetcher implements BreedFetcher {
    // TODO Task 2: Complete this class
    private int callsMade = 0;
    private final BreedFetcher underlyingFetcher;
    private final Map<String, List<String>> cache;

    public CachingBreedFetcher(BreedFetcher fetcher) {
        this.underlyingFetcher = fetcher;
        this.cache = new HashMap<>();
    }

    @Override
    public List<String> getSubBreeds(String breed) {
        // Check if the breed is already in the cache
        if (cache.containsKey(breed)) {
            return cache.get(breed);
        }
        
        // If not cached, call the underlying fetcher
        callsMade++;
        
        try {
            // Fetch the sub-breeds from the underlying fetcher
            List<String> subBreeds = underlyingFetcher.getSubBreeds(breed);
            
            // Cache the successful result
            cache.put(breed, subBreeds);
            
            return subBreeds;
        } catch (BreedNotFoundException e) {
            // Don't cache BreedNotFoundException - just rethrow it
            throw e;
        }
    }

    public int getCallsMade() {
        return callsMade;
    }
}