package com.swingy.model;

public class Person {
    protected String name;
    protected int level;
    protected int experience;
    protected int attack;
    protected int defense;
    protected int hitPoints;

    public Person() {
        this.name = "";
        this.level = 1;
        this.experience = 0;
        this.attack = 0;
        this.defense = 0;
        this.hitPoints = 100;
    }
}
