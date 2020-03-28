package com.github.irgalin.reachablesettlements.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Commute {

    private String destPoint;

    private int time;

    public Commute(@JsonProperty("destPoint") String destPoint, @JsonProperty("time") int time) {
        this.destPoint = destPoint;
        this.time = time;
    }

    public String getDestPoint() {
        return destPoint;
    }

    public void setDestPoint(String destPoint) {
        this.destPoint = destPoint;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
