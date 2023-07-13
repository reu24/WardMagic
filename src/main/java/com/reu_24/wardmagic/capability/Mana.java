package com.reu_24.wardmagic.capability;

public class Mana implements IMana {
    protected float maxMana;
    protected float mana;

    @Override
    public float getMaxMana() {
        return maxMana;
    }

    @Override
    public void setMaxMana(float maxMana) {
        this.maxMana = maxMana;
    }

    @Override
    public float getMana() {
        return mana;
    }

    @Override
    public float addMana(float mana) {
        float gives = Math.min(this.mana + mana, maxMana) - this.mana;
        if (Float.isNaN(gives)) {
            gives = mana;
        }
        this.mana += gives;
        roundMana();
        return gives;
    }

    @Override
    public float removeMana(float mana) {
        float removes = Math.max(this.mana - mana, 0) - this.mana;
        if (Float.isNaN(removes)) {
            removes = -mana;
        }
        this.mana += removes;
        roundMana();
        return -removes;
    }

    protected void roundMana() {
        mana = (float)Math.floor(mana * 100.0f) / 100.0f;
    }

    @Override
    public void setMana(float mana) {
        this.mana = mana;
    }
}
