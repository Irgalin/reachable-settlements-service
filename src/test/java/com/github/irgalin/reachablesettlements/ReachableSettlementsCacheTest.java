package com.github.irgalin.reachablesettlements;

import com.github.irgalin.reachablesettlements.cache.ReachableSettlementsCache;
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
        Set<String> result1 = new HashSet<>(Arrays.asList("town1", "town2", "town3"));
        ReachableSettlementsCache.putResultInCache("town1", 30, result1);
        Set<String> result2 = new HashSet<>(Arrays.asList("town4", "town5"));
        ReachableSettlementsCache.putResultInCache("town3", 20, result2);
        assertThat(ReachableSettlementsCache.size()).isEqualTo(2);
        assertThat(ReachableSettlementsCache.getResultFromCache("town1", 30))
                .containsExactlyInAnyOrder("town1", "town2", "town3");
        assertThat(ReachableSettlementsCache.getResultFromCache("town3", 20))
                .containsExactlyInAnyOrder("town4", "town5");
        ReachableSettlementsCache.clearCache();
        assertThat(ReachableSettlementsCache.getResultFromCache("town1", 30)).isNull();
    }

    @AfterAll
    public static void cleanUp() {
        ReachableSettlementsCache.clearCache();
    }


}
