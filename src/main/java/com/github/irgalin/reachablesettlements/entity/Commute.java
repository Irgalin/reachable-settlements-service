package com.github.irgalin.reachablesettlements.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

/**
 * Represents commute to specific settlement.
 */
public class Commute {

    private String destPointName;

    private int time;

    public Commute(@JsonProperty("destPointName") String destPointName, @JsonProperty("time") int time) {
        this.destPointName = destPointName;
        this.time = time;
    }

    public String getDestPointName() {
        return destPointName;
    }

    public void setDestPointName(@NotNull String destPointName) {
        this.destPointName = destPointName;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
