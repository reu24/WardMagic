package com.reu_24.wardmagic.capability;

public interface IMana {

    float getMaxMana();
    void setMaxMana(float maxMana);

    float getMana();
    float addMana(float mana);
    float removeMana(float mana);
    void setMana(float mana);
}
