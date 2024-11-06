package com.swingy.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Person {
    @NotNull(message = "Name should not be null")
    protected String name;

    @NotNull(message = "Level should not be null")
    @Size(min = 1, message = "level should be greater than 0")
    protected int level;

    @NotNull(message = "Ex should not be null")
    @Size(min = 0, message = "level should be greater than 0")
    protected int experience;

    @NotNull(message = "Attack should not be null")
    @Size(min = 0, max = 100, message = "Attack should be greater than 0 and smaller than 100")
    protected int attack;

    @NotNull(message = "Defense should not be null")
    @Size(min = 0, max = 100, message = "Defense should be greater than 0 and smaller than 100")
    protected int defense;

    @NotNull(message = "HP should not be null")
    @Size(min = 0, max = 100, message = "HP should be greater than 0 and smaller than 100")
    protected int hitPoints;

    public Person() {
        this.name = "";
        this.level = 1;
        this.experience = 0;
        this.attack = 0;
        this.defense = 0;
        this.hitPoints = 100;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public String getName() {
        return this.name;
    }

    public boolean isAlive() {
        if (this.hitPoints > 0)
            return true;
        else
            return false;
    }

    public int getDamageAfrerDefense(int damage, int defense) {
        if (defense > 70) {
            defense = 70;
        }
        int dad = damage * (100 - defense) / 100;
        return dad;
    }

    public void takeDamage(int damage) {
        int damageAfterDefense = this.getDamageAfrerDefense(damage, this.defense);
        System.out.println("Take damage: " + damageAfterDefense);
        this.hitPoints = this.hitPoints - damageAfterDefense;
        if (this.hitPoints < 0) {
            System.out.println(this.name + " is dead");
            this.hitPoints = 0;
        }
    }

    public int makeAttack() {
        int ad = this.attack;
        System.out.println("Make damage: " + ad);
        return ad;
    }
}
