package com.github.irgalin.reachablesettlements.service;

import com.github.irgalin.reachablesettlements.entity.Commute;
import com.github.irgalin.reachablesettlements.entity.Settlement;
import com.github.irgalin.reachablesettlements.storage.SettlementsStorage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.Stack;

@Service
public class SettlementsServiceImpl implements SettlementsService {

    private final static Logger LOGGER = Logger.getLogger(SettlementsServiceImpl.class);

    @Value("${json.data.file}")
    private String jsonDataFile;

    private SettlementsStorage storage;

    @PostConstruct
    public void initiateStorage() {
        storage = new SettlementsStorage(jsonDataFile);
    }

    @Override
    @NotNull
    public Set<String> getReachableSettlements(@NotNull final String startingPointName, final int commuteTimeLimit) {
        if (storage.getSettlementByName(startingPointName) == null) {
            throw new SettlementsServiceException("Unknown settlement name: " + startingPointName);
        }
        return findReachableSettlements(startingPointName, commuteTimeLimit);
    }

    private Set<String> findReachableSettlements(@NotNull final String startingPointName,
                                                 final int commuteTimeLimit) {
        Set<String> foundSettlementsNames = new LinkedHashSet<>();
        Stack<SettlementWrapper> settlementStack = new Stack<>();
        settlementStack.push(new SettlementWrapper(storage.getSettlementByName(startingPointName), 0));
        while (!settlementStack.empty()) {
            SettlementWrapper currentSettlement = settlementStack.pop();
            int curSettlementCommuteTime = currentSettlement.getCommuteTimeToStartingPoint();
            for (Commute commute : currentSettlement.getSettlement().getCommutes()) {
                Settlement neighborSettlement = storage.getSettlementByName(commute.getDestPointName());
                if (neighborSettlement == null) {
                    LOGGER.warn("The following destination point is absent: " + commute.getDestPointName() +
                            ". Please check correctness of input data.");
                    continue;
                }
                String neighborSettlementName = neighborSettlement.getName();
                if (foundSettlementsNames.contains(neighborSettlementName) ||
                        startingPointName.equals(neighborSettlementName)) {
                    continue;
                }
                int neighborSettlementCommuteTime = curSettlementCommuteTime + commute.getTime();
                if (neighborSettlementCommuteTime <= commuteTimeLimit) {
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
