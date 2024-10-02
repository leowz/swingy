package com.swingy.model;

public class Armor extends Artifact {
    private int increaseDefense = 20;

    public int getIncDefense() {
        return this.increaseDefense;
    }

    public String toString() {
        return "Artifact: Armor defense 20";
    }
}
