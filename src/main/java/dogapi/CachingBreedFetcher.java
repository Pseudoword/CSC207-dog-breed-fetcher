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
    private int callsMade = 0;
    private final HashMap<String, List<String>> breedToSubBreed = new HashMap<>();
    private final BreedFetcher fetcher;

    public CachingBreedFetcher(BreedFetcher fetcher) {
        this.fetcher = fetcher;
    }

    @Override
    public List<String> getSubBreeds(String breed) {

        try {
            if (breedToSubBreed.get(breed) != null) {
                return breedToSubBreed.get(breed);
            }
            callsMade ++;
            List<String> subBreeds = fetcher.getSubBreeds(breed);
            breedToSubBreed.put(breed, subBreeds);
            return subBreeds;

        } catch (BreedNotFoundException event) {
            throw new BreedNotFoundException(breed);
        }

    }

    public int getCallsMade() {
        return callsMade;
    }
}