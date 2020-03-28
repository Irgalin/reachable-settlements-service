package com.github.irgalin.reachablesettlements.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.irgalin.reachablesettlements.entity.Commute;
import com.github.irgalin.reachablesettlements.entity.Settlement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ReachableSettlementsServiceImpl implements ReachableSettlementsService {

    @Value("${jsonDataFile}")
    private String jsonDataFile;

    private Map<String, Settlement> settlementMap = new ConcurrentHashMap<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(ReachableSettlementsServiceImpl.class);

    @PostConstruct
    public void readDataFromFile() {
        try {
            File jsonData = ResourceUtils.getFile(jsonDataFile);
            if (jsonData.isFile()) {
                ObjectMapper mapper = new ObjectMapper();
                Settlement[] settlements = mapper.readValue(jsonData, Settlement[].class);
                for (Settlement settlement : settlements) {
                    settlementMap.put(settlement.getName(), settlement);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Failed to parse data file!", e);
        }

    }

    @Override
    @NotNull
    public Set<String> getListOfReachableSettlements(@NotNull final String startingPointName, final int commuteTime) {
        Set<String> reachableSettlementNames = new LinkedHashSet<>();
        Stack<SettlementWrapper> settlementStack = new Stack<>();
        settlementStack.push(new SettlementWrapper(settlementMap.get(startingPointName), 0));
        while (!settlementStack.empty()) {
            SettlementWrapper currentSettlement = settlementStack.pop();
            int curSettlementCommuteTime = currentSettlement.getCommuteTimeToStartingPoint();
            for (Commute commute : currentSettlement.getSettlement().getCommutes()) {
                Settlement neighborSettlement = settlementMap.get(commute.getDestPoint());
                String neighborSettlementName = neighborSettlement.getName();
                if (reachableSettlementNames.contains(neighborSettlementName) ||
                        startingPointName.equals(neighborSettlementName)) {
                    continue;
                }
                int neighborSettlementCommuteTime = curSettlementCommuteTime + commute.getTime();
                if (neighborSettlementCommuteTime <= commuteTime) {
                    reachableSettlementNames.add(neighborSettlementName);
                    settlementStack.push(new SettlementWrapper(neighborSettlement, neighborSettlementCommuteTime));
                }
            }
        }
        return reachableSettlementNames;
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
