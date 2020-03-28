package com.github.irgalin.reachablesettlements.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Settlement {

    private String name;

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
