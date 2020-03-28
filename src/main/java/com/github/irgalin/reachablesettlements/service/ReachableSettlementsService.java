package com.github.irgalin.reachablesettlements.service;

import javax.validation.constraints.NotNull;
import java.util.Set;

public interface ReachableSettlementsService {

    @NotNull
    Set<String> getListOfReachableSettlements(@NotNull String startingPoint, int commuteTime);

}
