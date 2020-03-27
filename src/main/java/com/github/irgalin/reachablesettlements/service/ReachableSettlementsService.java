package com.github.irgalin.reachablesettlements.service;

import com.github.irgalin.reachablesettlements.entity.Settlement;

import java.util.List;

public interface ReachableSettlementsService {

    List<Settlement> getListOfReachableSettlements(String startingPoint, int commuteTime);

}
