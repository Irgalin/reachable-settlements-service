package com.github.irgalin.reachablesettlements.entity;

import java.util.List;

public class Settlement {

    private long id;

    private String name;

    private List<Commute> commuteList;

    public Settlement(long id, String name, List<Commute> commuteList) {
        this.id = id;
        this.name = name;
        this.commuteList = commuteList;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Commute> getCommuteList() {
        return commuteList;
    }

}
