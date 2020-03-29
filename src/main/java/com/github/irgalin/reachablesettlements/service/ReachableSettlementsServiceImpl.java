package com.github.irgalin.reachablesettlements.service;

import com.github.irgalin.reachablesettlements.cache.ReachableSettlementsCache;
import com.github.irgalin.reachablesettlements.entity.Commute;
import com.github.irgalin.reachablesettlements.entity.Settlement;
import com.github.irgalin.reachablesettlements.storage.SettlementsStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.util.*;

@Service
public class ReachableSettlementsServiceImpl implements ReachableSettlementsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReachableSettlementsServiceImpl.class);

    @Value("${jsonDataFile}")
    private String jsonDataFile;

    @PostConstruct
    public void prepareStorage() {
        if (!SettlementsStorage.hasData()) {
            SettlementsStorage.readDataFromJsonFile(jsonDataFile);
        }
    }

    @Override
    @NotNull
    public Set<String> getReachableSettlements(@NotNull final String startingPointName, final int commuteTime) {
        Set<String> result = ReachableSettlementsCache.getResultFromCache(startingPointName, commuteTime);
        if (result == null) {
            result = findReachableSettlements(startingPointName, commuteTime);
        }
        ReachableSettlementsCache.putResultInCache(startingPointName, commuteTime, result);
        return result;
    }

    private synchronized Set<String> findReachableSettlements(@NotNull final String startingPointName,
                                                              final int commuteTime) {
        Set<String> foundSettlementsNames = new LinkedHashSet<>();
        Stack<SettlementWrapper> settlementStack = new Stack<>();
        settlementStack.push(new SettlementWrapper(SettlementsStorage.getSettlementByName(startingPointName), 0));
        while (!settlementStack.empty()) {
            SettlementWrapper currentSettlement = settlementStack.pop();
            int curSettlementCommuteTime = currentSettlement.getCommuteTimeToStartingPoint();
            for (Commute commute : currentSettlement.getSettlement().getCommutes()) {
                Settlement neighborSettlement = SettlementsStorage.getSettlementByName(commute.getDestPoint());
                String neighborSettlementName = neighborSettlement.getName();
                if (foundSettlementsNames.contains(neighborSettlementName) ||
                        startingPointName.equals(neighborSettlementName)) {
                    continue;
                }
                int neighborSettlementCommuteTime = curSettlementCommuteTime + commute.getTime();
                if (neighborSettlementCommuteTime <= commuteTime) {
                    foundSettlementsNames.add(neighborSettlementName);
                    settlementStack.push(new SettlementWrapper(neighborSettlement, neighborSettlementCommuteTime));
                }
            }
        }
        return foundSettlementsNames;
    }

    private class SettlementWrapper {

        private final Settlement settlement;

        private int commuteTimeToStartingPoint;


        public SettlementWrapper(Settlement settlement, int timeToReachStartingPoint) {
            this.settlement = settlement;
            this.commuteTimeToStartingPoint = timeToReachStartingPoint;
        }

        public Settlement getSettlement() {
            return settlement;
        }

        public int getCommuteTimeToStartingPoint() {
            return commuteTimeToStartingPoint;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SettlementWrapper that = (SettlementWrapper) o;
            return commuteTimeToStartingPoint == that.commuteTimeToStartingPoint &&
                    settlement.equals(that.settlement);
        }

        @Override
        public int hashCode() {
            return Objects.hash(settlement, commuteTimeToStartingPoint);
        }
    }

}
