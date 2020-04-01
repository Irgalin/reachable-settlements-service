package com.github.irgalin.reachablesettlements.cache;

import com.github.irgalin.reachablesettlements.controller.ReachableSettlementsRequest;
import com.github.irgalin.reachablesettlements.service.SettlementsService;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The cache that stores results of {@link SettlementsService#getReachableSettlements(String, int)} operation.
 */
public class ReachableSettlementsCache {

    private final static Map<ReachableSettlementsRequest, Set<String>> resultsCache = new ConcurrentHashMap<>();

    @NotNull
    public static Set<String> getResultFromCache(@NotNull ReachableSettlementsRequest request) {
        return resultsCache.get(request);
    }

    public static void putResultInCache(@NotNull ReachableSettlementsRequest request, @NotNull Set<String> result) {
        resultsCache.put(request, result);
    }

    public static int size() {
        return resultsCache.size();
    }

    public static void clearCache() {
        resultsCache.clear();
    }
}
