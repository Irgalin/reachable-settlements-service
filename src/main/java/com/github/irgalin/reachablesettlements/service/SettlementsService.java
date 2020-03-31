package com.github.irgalin.reachablesettlements.service;

import com.github.irgalin.reachablesettlements.storage.SettlementsStorage;

import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * Provides API to work on data stored in {@link SettlementsStorage}.
 */
public interface SettlementsService {

    /**
     * Gets the all settlement names that might be reached from a given starting point in a given commute time limit.
     *
     * @param startingPointName name of an existing settlement.
     * @param commuteTimeLimit commute time limit.
     * @return set of reachable settlement names.
     */
    @NotNull
    Set<String> getReachableSettlements(@NotNull String startingPointName, int commuteTimeLimit);

}
