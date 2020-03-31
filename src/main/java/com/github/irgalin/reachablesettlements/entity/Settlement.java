package com.github.irgalin.reachablesettlements.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Represents any type of human settlement (city, town, village, etc.) and possible direct commutes from it.
 */
public class Settlement {

    /**
     * The settlement name, must be unique and certain,
     * for example: 'St. Petersburg, Russia', 'St. Petersburg, Florida, USA'.
     */
    private String name;

    /**
     * List of possible direct commutes from this settlement.
     */
    private List<Commute> commutes;

    public Settlement(@JsonProperty("name") String name, @JsonProperty("commutes") List<Commute> commutes) {
        this.name = name;
        this.commutes = commutes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Commute> getCommutes() {
        return commutes;
    }

    public void setCommutes(List<Commute> commutes) {
        this.commutes = commutes;
    }
}
