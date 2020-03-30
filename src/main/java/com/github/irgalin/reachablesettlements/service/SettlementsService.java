package com.github.irgalin.reachablesettlements.service;

import javax.validation.constraints.NotNull;
import java.util.Set;

public interface SettlementsService {

    @NotNull
    Set<String> getReachableSettlements(@NotNull String startingPoint, int commuteTime);

}
