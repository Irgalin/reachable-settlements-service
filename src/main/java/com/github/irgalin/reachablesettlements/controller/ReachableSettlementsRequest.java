package com.github.irgalin.reachablesettlements.controller;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class ReachableSettlementsRequest {

    @NotBlank
    @NotNull
    private String startingPointName;

    @Min(1)
    private int commuteTimeMin;

    public ReachableSettlementsRequest() {
    }

    public ReachableSettlementsRequest(@NotBlank String startingPointName, int commuteTimeMin) {
        this.startingPointName = startingPointName;
        this.commuteTimeMin = commuteTimeMin;
    }

    public String getStartingPointName() {
        return startingPointName;
    }

    public void setStartingPointName(@NotBlank String startingPointName) {
        this.startingPointName = startingPointName;
    }

    public int getCommuteTimeMin() {
        return commuteTimeMin;
    }

    public void setCommuteTimeMin(int commuteTimeMin) {
        this.commuteTimeMin = commuteTimeMin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReachableSettlementsRequest that = (ReachableSettlementsRequest) o;
        return commuteTimeMin == that.commuteTimeMin &&
                startingPointName.equals(that.startingPointName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startingPointName, commuteTimeMin);
    }
}

