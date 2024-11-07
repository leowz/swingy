package com.swingy.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class Armor extends Artifact {
    @NotBlank(message = "Defense cannot be null")
    @Size(min = 1, max = 70, message = "Defense should be between 1 and 70")
    private int increaseDefense = 20;

    public Armor() {
        int randomness = Utils.getRandomNumber(-7, 8);
        increaseDefense = 20 + randomness;
    }

    public int getIncDefense() {
        return this.increaseDefense;
    }

    public String toString() {
        return "Artifact: Armor defense: " + this.increaseDefense;
    }
}
