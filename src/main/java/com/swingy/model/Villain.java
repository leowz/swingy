package com.swingy.model;

enum VillainClass {
    normal,
    elite,
    boss,
}

public class Villain extends Person {
    private VillainClass villainClass;
    private Artifact itemOwned;

    public Villain() {
        super();
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
    }

    static public Villain[] villainsFactory(int nbr) {
        Villain[] villains = new Villain[nbr]; // Create an array of Villain references
        for (int i = 0; i < nbr; i++) {
            villains[i] = new Villain(); // Instantiate each Villain
        }
        return villains;
    }

    public String toString() {
        return String.format("villain(%s) hp(%d) attack(%d), defense(%d) item(%s)", this.villainClass, this.hitPoints,
                this.attack, this.defense, this.itemOwned);
    }
}
