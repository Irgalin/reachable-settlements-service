package com.github.irgalin.reachablesettlements.cache;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ReachableSettlementsCache {

    private final static Map<ResultCacheKey, Set<String>> resultsCache = new ConcurrentHashMap<>();

    @NotNull
    public static Set<String> getResultFromCache(@NotNull String startingPointName, int commuteTime) {
        return resultsCache.get(new ResultCacheKey(startingPointName, commuteTime));
    }

    public static void putResultInCache(@NotNull String startingPointName, int commuteTime, @NotNull Set<String> result) {
        resultsCache.put(new ResultCacheKey(startingPointName, commuteTime), result);
    }

    public static int size(){
        return resultsCache.size();
    }

    public static void clearCache() {
        resultsCache.clear();
    }

    private static class ResultCacheKey {

        private final String startingPointName;

        private final int commuteTime;

        public ResultCacheKey(@NotNull String startingPointName, int commuteTime) {
            this.startingPointName = startingPointName;
            this.commuteTime = commuteTime;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ResultCacheKey that = (ResultCacheKey) o;
            return commuteTime == that.commuteTime &&
                    startingPointName.equals(that.startingPointName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(startingPointName, commuteTime);
        }
    }
}
