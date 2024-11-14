package com.swingy.model;

import jakarta.validation.constraints.PositiveOrZero;

public class Weapon extends Artifact {
    @PositiveOrZero(message = "attack should not be blank")
    private int increaseAttack;

    public Weapon() {
        int randomness = Utils.getRandomNumber(-5, 5);
        increaseAttack = 10 + randomness;
    }

    public int getIncreaseAttack() {
        return increaseAttack;
    }

    public void setIncreaseAttack(int increaseAttack) {
        this.increaseAttack = increaseAttack;
    }

    public String toString() {
        return "Artifact: Weapon attack: " + this.increaseAttack;
    }
}
