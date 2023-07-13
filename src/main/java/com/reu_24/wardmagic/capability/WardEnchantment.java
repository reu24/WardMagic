package com.reu_24.wardmagic.capability;

public class WardEnchantment {
    protected final int wardId;
    protected final int level;

    public WardEnchantment(int wardId, int level) {
        this.wardId = wardId;
        this.level = level;
    }

    public int getWardId() {
        return wardId;
    }

    public int getLevel() {
        return level;
    }
}
