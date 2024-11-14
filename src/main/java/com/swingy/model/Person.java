package com.swingy.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, // Use a name-based identifier for the type
        include = JsonTypeInfo.As.PROPERTY, // Include the type information as a property in JSON
        property = "type" // The property name that indicates the type
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Hero.class, name = "hero"),
        @JsonSubTypes.Type(value = Villain.class, name = "villain")
})

public class Person {
    @NotNull(message = "Name should not be null")
    @Size(min = 3, max = 10, message = "Name should be between 3 and 10 letters")
    protected String name;

    @NotNull(message = "Level should not be null")
    @Min(value = 1, message = "level should be greater than 0")
    protected int level;

    @NotNull(message = "Ex should not be null")
    @Min(value = 0, message = "level should be greater than 0")
    protected int experience;

    @NotNull(message = "Attack should not be null")
    @Range(min = 0, max = 100, message = "Attack should be greater than 0 and smaller than 100")
    protected int attack;

    @NotNull(message = "Defense should not be null")
    @Range(min = 0, max = 100, message = "Defense should be greater than 0 and smaller than 100")
    protected int defense;

    @NotNull(message = "HP should not be null")
    @Range(min = 0, max = 100, message = "HP should be greater than 0 and smaller than 100")
    protected int hitPoints;

    public Person() {
    }

    public Person(boolean newPerson) {
        if (newPerson) {
            this.name = "";
            this.level = 1;
            this.experience = 0;
            this.attack = 0;
            this.defense = 0;
            this.hitPoints = 100;
        }
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public String getName() {
        return this.name;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getAttack() {
        return attack;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getDefense() {
        return defense;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getExperience() {
        return experience;
    }

    public void setHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    @JsonIgnore
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
