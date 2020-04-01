package com.github.irgalin.reachablesettlements;

import com.github.irgalin.reachablesettlements.cache.ReachableSettlementsCache;
import com.github.irgalin.reachablesettlements.controller.ReachableSettlementsRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ReachableSettlementsCacheTest {

    @BeforeEach
    public void setUp() {
        ReachableSettlementsCache.clearCache();
    }

    @Test
    public void testCacheMethods() {
        ReachableSettlementsRequest request1 = new ReachableSettlementsRequest("town1", 30);
        Set<String> result1 = new HashSet<>(Arrays.asList("town1", "town2", "town3"));
        ReachableSettlementsCache.putResultInCache(request1, result1);

        ReachableSettlementsRequest request2 = new ReachableSettlementsRequest("town3", 20);
        Set<String> result2 = new HashSet<>(Arrays.asList("town4", "town5"));
        ReachableSettlementsCache.putResultInCache(request2, result2);

        assertThat(ReachableSettlementsCache.size()).isEqualTo(2);
        assertThat(ReachableSettlementsCache.getResultFromCache(request1))
                .containsExactlyInAnyOrder("town1", "town2", "town3");
        assertThat(ReachableSettlementsCache.getResultFromCache(request2))
                .containsExactlyInAnyOrder("town4", "town5");

        ReachableSettlementsCache.clearCache();
        assertThat(ReachableSettlementsCache.getResultFromCache(request1)).isNull();
    }

    @AfterAll
    public static void cleanUp() {
        ReachableSettlementsCache.clearCache();
    }


}
