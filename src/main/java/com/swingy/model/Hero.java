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

    public String toString() {
        return String.format("Hero(%s) level(%d) hp(%d) attack(%d) defense(%d) weapon(%s) Armor(%s) Helm(%s)",
                this.name, this.level, this.hitPoints, this.attack, this.defense, this.weapon, this.armor, this.helm);
    }
}
