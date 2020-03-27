package com.github.irgalin.reachablesettlements.entity;

public class Commute {

    private Settlement destinationSettlement;

    private int timeToCommute;

    public Commute(Settlement destinationSettlement, int timeToCommute) {
        this.destinationSettlement = destinationSettlement;
        this.timeToCommute = timeToCommute;
    }

    public Settlement getDestinationSettlement() {
        return destinationSettlement;
    }

    public int getTimeToCommute() {
        return timeToCommute;
    }
}
