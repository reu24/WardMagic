package com.reu_24.wardmagic.capability;

import java.util.Set;

public interface IWardEnchantments {
    Set<WardEnchantment> getEnchantments();
    void addWardEnchantment(WardEnchantment enchantment);
    void setEnchantments(Set<WardEnchantment> enchantments);
}
