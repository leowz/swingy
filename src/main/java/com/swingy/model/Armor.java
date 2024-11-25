package com.swingy.model;

import org.hibernate.validator.constraints.Range;
import jakarta.validation.constraints.NotBlank;

public class Armor extends Artifact {
    @NotBlank(message = "Defense cannot be null")
    @Range(min = 1, max = 70, message = "Defense should be between 1 and 70")
    private int increaseDefense = 20;

    public Armor() {
        int randomness = Utils.getRandomNumber(-7, 8);
        increaseDefense = 20 + randomness;
    }

    public int getIncreaseDefense() {
        return increaseDefense;
    }

    public void setIncreaseDefense(int increaseDefense) {
        this.increaseDefense = increaseDefense;
    }

    public String toString() {
        return "Armor defense: " + this.increaseDefense;
    }
}
