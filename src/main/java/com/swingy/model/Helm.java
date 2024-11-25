package com.swingy.model;

import jakarta.validation.constraints.PositiveOrZero;

public class Helm extends Artifact {
    @PositiveOrZero(message = "attack should not be blank")
    private int increaseHitPoint;

    public Helm() {
        int randomness = Utils.getRandomNumber(-2, 5);
        increaseHitPoint = 5 + randomness;
    }

    public void setIncreaseHitPoint(int increaseHitPoint) {
        this.increaseHitPoint = increaseHitPoint;
    }

    public int getIncreaseHitPoint() {
        return increaseHitPoint;
    }

    public String toString() {
        return "Helm HP: " + this.increaseHitPoint;
    }
}
