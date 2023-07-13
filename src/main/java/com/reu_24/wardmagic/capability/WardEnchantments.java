package com.reu_24.wardmagic.capability;

import java.util.HashSet;
import java.util.Set;

public class WardEnchantments implements IWardEnchantments {
    protected Set<WardEnchantment> wardEnchantments = new HashSet<>();

    @Override
    public Set<WardEnchantment> getEnchantments() {
        return wardEnchantments;
    }

    @Override
    public void addWardEnchantment(WardEnchantment enchantment) {
        wardEnchantments.add(enchantment);
    }

    @Override
    public void setEnchantments(Set<WardEnchantment> enchantments) {
        wardEnchantments = enchantments;
    }
}
