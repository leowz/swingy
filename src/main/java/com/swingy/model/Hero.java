package com.swingy.model;

import jakarta.validation.constraints.NotNull;

public class Hero extends Person {
    @NotNull(message = "Hero Class can not be null")
    private HeroClass heroClass;

    private Weapon weapon;
    private Armor armor;
    private Helm helm;

    public Hero(HeroClass heroClass) {
        super();
        this.heroClass = heroClass;
        if (heroClass == HeroClass.Assassin) {
            this.weapon = new Weapon();
            this.helm = new Helm();
            this.attack = Utils.getRandomNumber(20, 45);
            this.defense = Utils.getRandomNumber(5, 13);
        } else if (heroClass == HeroClass.Barbarian) {
            this.armor = new Armor();
            this.helm = new Helm();
            this.attack = Utils.getRandomNumber(10, 30);
            this.defense = Utils.getRandomNumber(20, 30);
        } else if (heroClass == HeroClass.Knight) {
            this.weapon = new Weapon();
            this.armor = new Armor();
            this.helm = new Helm();
            this.attack = Utils.getRandomNumber(15, 25);
            this.defense = Utils.getRandomNumber(15, 20);
        }
    }

    public void takeDamage(int damage) {
        int damageAfterDefense = this.getDamageAfrerDefense(damage,
                this.defense + (this.armor != null ? this.armor.getIncDefense() : 0));
        int damageAfterHitHelm = damageAfterDefense - (this.helm != null ? this.helm.getIncHp() : 0);
        System.out.println("takeDamage damage: " + damageAfterHitHelm);
        if (damageAfterHitHelm <= 0)
            return;
        else {
            this.hitPoints = this.hitPoints - damageAfterHitHelm;
            if (this.hitPoints < 0) {
                this.hitPoints = 0;
            }
        }
    }

    public int makeAttack() {
        int ad = this.attack + (weapon != null ? weapon.addedAttackPoints() : 0);
        System.out.println("Make damage: " + ad);
        return ad;
    }

    public void showItems() {
        System.out.println("Hero equipements: ");
        if (this.weapon != null)
            System.out.println(weapon);
        if (this.armor != null)
            System.out.println(armor);
        if (this.helm != null)
            System.out.println(helm);
    }

    public void equipeAritifact(Artifact item) {
        if (item instanceof Weapon) {
            this.weapon = (Weapon) item;
        }
        if (item instanceof Armor) {
            this.armor = (Armor) item;
        }
        if (item instanceof Helm) {
            this.helm = (Helm) item;
        }
    }

    public int getLevel(int currentLevel, int exp) {
        int level = currentLevel;
        while (exp > (level * 1000) + ((level - 1) * (level - 1) * 450)) {
            level++;
        }
        return level;
    }

    public void updateStats(int hp, int attack, int defense) {
        this.hitPoints += hp;
        if (this.hitPoints > 100)
            this.hitPoints = 100;
        this.attack += attack;
        if (this.attack > 100)
            this.attack = 100;
        this.defense += defense;
        if (this.defense > 100)
            this.defense = 100;
    }

    public void gainExperience(int exp) {
        System.out.println("Hero gained experence: " + exp);
        this.experience += exp;
        int nextLevel = getLevel(this.level, this.experience);
        System.out.println("nextLevel " + nextLevel);
        if (this.level < nextLevel) {
            System.out.println("Hero promot from level: " + this.level + " to level: " + nextLevel);
            System.out.println("Hero regain 30 hp due to the level up!");
            this.updateStats(30, 2, 1);
            this.level = nextLevel;
        }
    }

    public String toString() {
        return String.format("Hero(%s) level(%d) exp(%d) hp(%d) attack(%d) defense(%d) weapon(%s) Armor(%s) Helm(%s)",
                this.name, this.level, this.experience, this.hitPoints, this.attack, this.defense, this.weapon,
                this.armor, this.helm);
    }
}
