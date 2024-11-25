package com.swingy.model;

import jakarta.validation.constraints.NotNull;

public class Villain extends Person {
    @NotNull(message = "Villain Class should not be null")
    private VillainClass villainClass;

    @NotNull(message = "Artifact should not be null")
    private Artifact itemOwned;

    public Villain() {
        super();
    }

    public Villain(boolean random) {
        super(true);
        if (random) {
            this.villainClass = VillainClass.values()[Utils.getRandomNumber(0, VillainClass.values().length)];
            int arti = Utils.getRandomNumber(0, 10);
            if (arti % 3 == 0) {
                this.itemOwned = new Helm();
            } else if (arti % 3 == 1) {
                this.itemOwned = new Weapon();
            } else if (arti % 3 == 2) {
                this.itemOwned = new Armor();
            }
            int addition = 0;
            if (this.villainClass == VillainClass.elite) {
                addition = 5;
            } else if (this.villainClass == VillainClass.boss) {
                addition = 10;
            }
            this.attack = Utils.getRandomNumber(5 + addition, 25 + addition);
            this.defense = Utils.getRandomNumber(10 + addition, 15 + addition);
            if (villainClass == VillainClass.normal) {
                this.experience = 500 + Utils.getRandomNumber(-120, 120);
            } else if (villainClass == VillainClass.elite) {
                this.experience = 800 + Utils.getRandomNumber(-150, 150);
            } else if (villainClass == VillainClass.boss) {
                this.experience = 1000 + Utils.getRandomNumber(-100, 300);
            }

        }
    }

    public VillainClass getVillainClass() {
        return villainClass;
    }

    public void setVillainClass(VillainClass villainClass) {
        this.villainClass = villainClass;
    }

    public void setItemOwned(Artifact itemOwned) {
        this.itemOwned = itemOwned;
    }

    public Artifact getItemOwned() {
        return itemOwned;
    }

    static public Villain[] villainsFactory(int nbr) {
        Villain[] villains = new Villain[nbr]; // Create an array of Villain references
        for (int i = 0; i < nbr; i++) {
            villains[i] = new Villain(true); // Instantiate each Villain
        }
        return villains;
    }

    public String toString() {
        return String.format("villain(%s) hp(%d) attack(%d), defense(%d) item(%s)", this.villainClass, this.hitPoints,
                this.attack, this.defense, this.itemOwned);
    }
}
