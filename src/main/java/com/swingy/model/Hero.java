package com.swingy.model;

public class Hero extends Person {
    private HeroClass heroClass;
    private Weapon weapon;
    private Armor armor;
    private Helm helm;

    public Hero(HeroClass heroClass) {
        super();
        this.heroClass = heroClass;
        this.attack = Utils.getRandomNumber(10, 30);
        this.defense = Utils.getRandomNumber(15, 20);
        if (heroClass == HeroClass.Assassin) {
            this.weapon = new Weapon();
            this.helm = new Helm();
        } else if (heroClass == HeroClass.Barbarian) {
            this.armor = new Armor();
            this.helm = new Helm();
        } else if (heroClass == HeroClass.Knight) {
            this.weapon = new Weapon();
            this.armor = new Armor();
            this.helm = new Helm();
        }
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return String.format("Hero(%s) level(%d) hp(%d) attack(%d) defense(%d) weapon(%s) Armor(%s) Helm(%s)\n",
                this.name, this.level, this.hitPoints, this.attack, this.defense, this.weapon, this.armor, this.helm);
    }
}
