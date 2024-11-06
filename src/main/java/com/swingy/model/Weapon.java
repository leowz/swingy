package com.swingy.model;

import jakarta.validation.constraints.NotBlank;

public class Weapon extends Artifact {
    @NotBlank(message = "attack should not be blank")
    private int increaseAttack;

    public Weapon() {
        int randomness = Utils.getRandomNumber(-5, 5);
        increaseAttack = 10 + randomness;
    }

    public String toString() {
        return "Artifact: Weapon attack 10";
    }

    public int addedAttackPoints() {
        return this.increaseAttack;
    }
}
