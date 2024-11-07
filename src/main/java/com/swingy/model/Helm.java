package com.swingy.model;

import jakarta.validation.constraints.NotBlank;

public class Helm extends Artifact {
    @NotBlank(message = "attack should not be blank")
    private int increaseHitPoint;

    public Helm() {
        int randomness = Utils.getRandomNumber(-2, 5);
        increaseHitPoint = 5 + randomness;
    }

    public int getIncHp() {
        return this.increaseHitPoint;
    }

    public String toString() {
        return "Artifact: Helm HP: " + this.increaseHitPoint;
    }
}
